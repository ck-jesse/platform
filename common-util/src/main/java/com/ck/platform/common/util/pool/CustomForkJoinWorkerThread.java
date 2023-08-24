package com.ck.platform.common.util.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * 自定义ForkJoinWorkerThread
 * 1、支持自定义线程名字
 *
 * @author chenck
 * @date 2023/5/6 13:49
 */
public class CustomForkJoinWorkerThread extends ForkJoinWorkerThread {

    protected static Logger logger = LoggerFactory.getLogger(CustomForkJoinWorkerThread.class);

    protected CustomForkJoinWorkerThread(ForkJoinPool pool, String threadName) {
        super(pool);
        setPriority(Thread.NORM_PRIORITY); // 设置线程优先级
        setDaemon(false); // 设置是否为守护线程
        setName(threadName);
    }

    /**
     * 线程终止时，执行的清理动作
     */
    @Override
    protected void onTermination(Throwable exception) {
        super.onTermination(exception);
        if (logger.isDebugEnabled()) {
            logger.debug("Performs cleanup associated with termination of this worker thread, pool={}", this.getPool().toString());
        }
    }
}
