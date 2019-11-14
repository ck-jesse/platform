package com.ck.platform.common.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 后台定时更新系统时钟
 * <p>
 * 问题：System.currentTimeMillis() 在并发调用或者特别频繁调用它的情况下性能很差
 * <p>
 * 1、为什么并发执行时这么慢呢？
 * HotSpot源码的hotspot/src/os/linux/vm/os_linux.cpp文件中，有一个javaTimeMillis()方法，这就是System.currentTimeMillis()的native实现。
 * 调用 gettimeofday() 需要从用户态切换到内核态；gettimeofday() 的表现受Linux系统的计时器（时钟源）影响，在HPET计时器下性能尤其差；
 * 系统只有一个全局时钟源，高并发或频繁访问会造成严重的争用。
 * HPET计时器性能较差的原因是会将所有对时间戳的请求串行执行。TSC计时器性能较好，因为有专用的寄存器来保存时间戳。缺点是可能不稳定，因为它是纯硬件的计时器，频率可变（与处理器的CLK信号有关）。
 * <p>
 * 2、如何解决这个问题？
 * 最常见的办法是用单个调度线程来按毫秒更新时间戳，相当于维护一个全局缓存。其他线程取时间戳时相当于从内存取，不会再造成时钟资源的争用，代价就是牺牲了一些精确度。
 *
 * @author chenck
 * @date 2019/11/14 11:14
 */
public class SystemClock {

    private final long precision;
    private final AtomicLong now;

    private SystemClock(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 定时更新当前毫秒时间戳
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "system_clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    public long precision() {
        return precision;
    }

    /**
     * 当前时间戳(ms)
     */
    private long currentTimeMillis() {
        return now.get();
    }

    /**
     * 当前时间戳(ms)
     */
    public static long now() {
        return instance().currentTimeMillis();
    }

    /**
     * 获取实例
     */
    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 静态内部类，实现延迟加载单利模式
     */
    private static class InstanceHolder {
        public static final SystemClock INSTANCE = new SystemClock(1L);

        private InstanceHolder() {
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int times = Integer.MAX_VALUE;

        // 单线程-定时更新时间戳并缓存
        //
        long start = System.currentTimeMillis();
        for (long i = 0; i < times; i++) {
            SystemClock.now();
        }
        long end = System.currentTimeMillis();
        System.out.println("SystemClock.now() => times = " + times + ", time = " + (end - start) + "ms");

        // 单线程-普通使用System.currentTimeMillis()
        // 效率很差
        long start2 = System.currentTimeMillis();
        for (long i = 0; i < times; i++) {
            System.currentTimeMillis();
        }
        long end2 = System.currentTimeMillis();
        System.out.println("System.currentTimeMillis() => times = " + times + ", time = " + (end2 - start2) + "ms");

        // 多线程--普通使用System.currentTimeMillis()
        // 效率很差
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(1000);
        for (long i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startLatch.await();
                        System.currentTimeMillis();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        endLatch.countDown();
                    }
                }
            }).start();
        }
        long beginTime = System.currentTimeMillis();
        startLatch.countDown();
        endLatch.await();
        long elapsedTime = System.currentTimeMillis() - beginTime;
        System.out.println("100 System.currentTimeMillis() parallel calls: " + elapsedTime + " ms");

    }
}
