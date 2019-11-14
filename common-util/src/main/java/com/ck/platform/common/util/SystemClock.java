package com.ck.platform.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 系统时钟
 * <p>
 * 问题：单线程执行 System.currentTimeMillis()，比多线程并发执行 System.currentTimeMillis() 快了许多倍。
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
    private static final SystemClock MILLIS_CLOCK = new SystemClock(1);
    private final long precision;
    private final AtomicLong now;

    private SystemClock(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    public static SystemClock millisClock() {
        return MILLIS_CLOCK;
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "system.clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now.get();
    }

}
