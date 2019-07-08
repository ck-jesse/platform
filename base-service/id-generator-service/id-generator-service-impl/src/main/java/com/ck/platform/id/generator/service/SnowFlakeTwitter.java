package com.ck.platform.id.generator.service;

import java.text.SimpleDateFormat;

/**
 * 雪花算法ID生成器
 *
 * @author chenck
 * @date 2019/7/8 11:56
 */
public class SnowFlakeTwitter {

    /**
     * 起始的时间戳
     */
    // private final static long START_STMP = 1546272000000L;// 2019-01-01
    // private final static long START_STMP = 1514736000000L;// 2018-01-01
    private final static long START_STMP = 1483200000000L;// 2017-01-01

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;  //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    // 结果31
    public final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    // 结果31
    public final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    // 结果4095 支持1毫秒产生4095个自增序列id
    public final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心
    private long machineId;    //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public SnowFlakeTwitter(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        //上次生成ID的时间戳
        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT      //数据中心部分
                | machineId << MACHINE_LEFT            //机器标识部分
                | sequence;                            //序列号部分
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {
        // 初始时间计算
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(START_STMP));
        System.out.println(sdf.parse("1990-01-01 00:00:00").getTime());


        long startTime = System.currentTimeMillis();
        SnowFlakeTwitter snowFlake = new SnowFlakeTwitter(0, 1);
        for (int i = 0; i < 1000; i++) {
            long id = snowFlake.nextId();
            String timeStr = Long.toBinaryString(id);
            System.out.println(id + " " + timeStr.length() + " " + timeStr);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("执行时间" + (endTime - startTime) + "ms");

    }

}