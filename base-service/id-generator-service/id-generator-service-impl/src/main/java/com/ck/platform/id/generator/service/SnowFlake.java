package com.ck.platform.id.generator.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * snowflake 修改版本， 新snowflake 算法
 * ([0] -[41bit时间差]-[8bit 机器标识])*10000+random(9999)
 * （最后10进制4位 方便数据分析做随机样本抽取）
 * 性能：普通mac i5处理器，每秒10万并发
 *
 * @author chenck
 * @date 2019/7/8 10:11
 */
public class SnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private final static long MACHINE_BIT = 8;  //机器标识占用的位数

    /**
     * 每一部分的最大值
     */
    public final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long TIMESTMP_LEFT = MACHINE_BIT;

    private long machineId;    //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳
//    private long random = 0L; //序列号

    private Set<Integer> randomsInMill = new HashSet<Integer>();


    public SnowFlake(long machineId) {

        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {//时间回拨检测
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        sequence = 0L;

        //取9999内随机数
        int random = geUniqRandom(currStmp, lastStmp);

        lastStmp = currStmp;

        long id = (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | machineId;          //机器标识部分

        // add random number
        id = id * 10000 + random;
        return id;

    }

    /**
     * 生成1ms内不重复的随机数
     *
     * @return
     */
    private int geUniqRandom(long currStmp, long lastStmp) {

        int random_ = getRandom();

        if (currStmp == lastStmp) {
            //同一毫秒内，随机数发生碰撞检测
            int roop = 5;
            while (--roop > 0) {
                //如果发生碰撞，重复生成随机数
                if (randomsInMill.contains(random_)) {
                    random_ = getRandom();
                } else {//没有发生碰撞
                    randomsInMill.add(random_);
                    return random_;
                }
            }

            if (roop <= 0) {//尝试5次仍然发生碰撞
                throw new RuntimeException("random collision.  Refusing to generate id");
            }

            //this case never happen
            return random_;

        } else {//进入下一个毫秒

            randomsInMill.clear();
            randomsInMill.add(random_);
            return random_;
        }

    }

    private int getRandom() {
        Random randomObj = new java.util.Random();
        return randomObj.nextInt(9999);
    }


    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter("/tmp/snowflake.log"));

        long begin = System.currentTimeMillis();
        SnowFlake snowFlake = new SnowFlake(2);
        for (int i = 0; i < 10; i++) {
            Long l = snowFlake.nextId();
            String timeStr = Long.toBinaryString(l);
            System.out.println(l + " " + timeStr.length() + " " + timeStr);//            out.write(l.toString());
            out.write(l.toString());
            out.newLine();
        }

        out.close();
        long end = System.currentTimeMillis();
        System.out.println((end - begin) / 1000.0);
//        SnowFlakeOrig snowFlakeOrig = new SnowFlakeOrig(2,3);
//        for (int i = 0; i < 5; i++) {
//            Long l = snowFlakeOrig.nextId();
//            System.out.println(l+" "+Long.toBinaryString(l));
//        }


    }

}