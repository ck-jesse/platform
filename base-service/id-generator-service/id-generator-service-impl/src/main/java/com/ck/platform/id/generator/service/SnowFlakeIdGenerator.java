package com.ck.platform.id.generator.service;

import com.ck.platform.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 雪花算法ID生成器
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta seconds: The next 30 bits, represents delta seconds since a customer epoch(2019-12-01 00:00:00.000).
 * Supports about 17 years until to 2036-12-04 18:48:31
 * <li>worker id: The next 22 bits, represents the worker's id which assigns based on database, max id is about 209W
 * <li>sequence: The next 13 bits, represents a sequence within the same second, max for 8191/s<br><br>
 * <pre>{@code
 *  +------+----------------------+----------------+-----------+
 *  | sign |     delta seconds    | worker node id | sequence  |
 *  +------+----------------------+----------------+-----------+
 *    1bit          29bits              21bits         13bits
 * }</pre>
 * <p>
 * 注意：可根据实际的情况来调整不同区域的位数，已获得不同的最大使用年限、最大机器数等
 *
 * @author chenck
 * @date 2019/11/20 10:51
 */
@Slf4j
public class SnowFlakeIdGenerator {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 时间戳占用的位数
     */
    private final static int DELTA_SECONDS_BIT = 29;
    /**
     * 机器标识占用的位数
     */
    private final static int MACHINE_BIT = 21;
    /**
     * 序列号占用的位数
     */
    private final static int SEQUENCE_BIT = 13;

    /**
     * Total 64 bits
     */
    public static final int TOTAL_BITS = 1 << 6;

    /**
     * 开始计时时间（单位秒）
     * 默认: 2019-12-01 00:00:00（ms:1575129600000）
     */
    private String startEpochTimeStr = "2019-12-01 00:00:00";
    /**
     * 开始计时时间（单位秒）
     */
    private long startEpochSeconds = TimeUnit.MILLISECONDS.toSeconds(1575129600000L);

    /**
     * Bits for [sign-> second-> workId-> sequence]
     */
    private int signBits = 1;// 符号位数，固定长度1位，第一个bit位是标识部分，在java中由于long的最高位是符号位，正数是0，负数是1，一般生成的ID为正数，所以固定为0
    private final int timestampBits;// 时间戳的位数
    private final int machineIdBits;// 机器id的位数
    private final int sequenceBits;// 序列号的位数

    /**
     * Max value for machineId & sequence
     */
    private final long maxDeltaSeconds;// 最大增量秒
    private final long maxMachineId;// 最大机器id
    private final long maxSequence;// 时间单位下的最大序列号，本例中为秒，也就是1s中内生成的最大序列号

    /**
     * Shift for timestamp & machineId
     * 每一部分向左的位移
     */
    private final int timestampShift;// 时间戳的移位
    private final int machineIdShift;// 机器id的移位

    private long machineId;// 机器标识
    private long sequence = 0L; // 序列号
    private long lastStmp = -1L;// 上一次时间戳

    public SnowFlakeIdGenerator(int machineId) {
        this(DELTA_SECONDS_BIT, MACHINE_BIT, SEQUENCE_BIT, machineId);
    }

    public SnowFlakeIdGenerator(int timestampBits, int machineIdBits, int sequenceBits, int machineId) {
        // 确定分配的位数
        int allocateTotalBits = signBits + timestampBits + machineIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // 初始化bits
        this.timestampBits = timestampBits;
        this.machineIdBits = machineIdBits;
        this.sequenceBits = sequenceBits;

        // 初始化最大值
        // -1L ^ (-1L << SEQUENCE_BIT)
        this.maxDeltaSeconds = ~(-1L << timestampBits);
        this.maxMachineId = ~(-1L << machineIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // 初始化移位
        this.timestampShift = machineIdBits + sequenceBits;
        this.machineIdShift = sequenceBits;

        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.machineId = machineId;
        log.info("Initialized machineId={}, bits(1, {}, {}, {})", machineId, timestampBits, machineIdBits, sequenceBits);
        log.info("Initialized machineId={}, maxDeltaSeconds={}, maxMachineId={}, maxSequence={}", machineId, maxDeltaSeconds, maxMachineId,
                maxSequence);
        log.info("Initialized machineId={}, startEpochTime={}, maxTime={}", machineId, sdf.format(getStartDate()), sdf.format(getMaxUseDate()));
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long currentSecond = getNewCurrentSecond();
        if (currentSecond < lastStmp) {
            long refusedSeconds = lastStmp - currentSecond;
            throw new BizException("Clock moved backwards. Refusing for " + refusedSeconds + " seconds");
        }

        if (currentSecond == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & maxSequence;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currentSecond = getNextSecond();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        //上次生成ID的时间戳
        lastStmp = currentSecond;

        return (currentSecond - startEpochSeconds) << timestampShift //时间戳部分
                | machineId << machineIdShift            //机器标识部分
                | sequence;                            //序列号部分
    }

    /**
     * 阻塞到下一个秒，直到获得新的时间戳
     */
    private long getNextSecond() {
        long mill = getNewCurrentSecond();
        while (mill <= lastStmp) {
            mill = getNewCurrentSecond();
        }
        return mill;
    }

    /**
     * 获取以秒为单位的当前时间
     */
    private long getNewCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - startEpochSeconds > maxDeltaSeconds) {
            // 时间戳位已用尽
            throw new BizException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }
        return currentSecond;
    }

    /**
     * 开始计数时间
     */
    private Date getStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(TimeUnit.SECONDS.toMillis(startEpochSeconds)));
        return calendar.getTime();
    }

    /**
     * 最大使用时间
     */
    private Date getMaxUseDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(TimeUnit.SECONDS.toMillis(startEpochSeconds + maxDeltaSeconds)));
        return calendar.getTime();
    }

    /**
     * 解析id
     */
    public String parseId(long id) {
        long totalBits = TOTAL_BITS;

        // parse ID
        long sequence = (id << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (id << (timestampBits + signBits)) >>> (totalBits - machineIdBits);
        long deltaSeconds = id >>> (machineIdBits + sequenceBits);

        Date thatTime = new Date(TimeUnit.SECONDS.toMillis(startEpochSeconds + deltaSeconds));
        String thatTimeStr = sdf.format(thatTime);
        String idBinStr = Long.toBinaryString(id);
        // format as string
        return String.format("{\"id\":\"%d\",\"timestamp\":\"%s\",\"machineId\":\"%d\",\"sequence\":\"%d\",\"idBits\":\"%d\"}",
                id, thatTimeStr, workerId, sequence, idBinStr.length());
    }

    /**
     * 设置开始计时时间（秒）
     */
    public void setStartEpochSeconds(String startEpochTimeStr) {
        if (StringUtils.isNotBlank(startEpochTimeStr)) {
            this.startEpochTimeStr = startEpochTimeStr;
            try {
                this.startEpochSeconds = TimeUnit.MILLISECONDS.toSeconds(sdf.parse(startEpochTimeStr).getTime());
            } catch (ParseException e) {
                throw new IllegalArgumentException("解析开始计时时间（秒）异常", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // 初始时间计算
        SnowFlakeIdGenerator snowFlake = new SnowFlakeIdGenerator(1000001);
        snowFlake.setStartEpochSeconds("2019-12-01 00:00:00");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            long id = snowFlake.nextId();
            System.out.println(id + " " + " " + snowFlake.parseId(id));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("执行时间" + (endTime - startTime) + "ms");

    }

}