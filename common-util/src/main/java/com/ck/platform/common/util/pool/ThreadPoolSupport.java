package com.ck.platform.common.util.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 * <p>
 * 注：通过 poolName 来区分不同场景的线程池，保证业务隔离。
 *
 * @author chenck
 * @date 2020/9/22 13:44
 */
public class ThreadPoolSupport {

    protected static Logger logger = LoggerFactory.getLogger(ThreadPoolSupport.class);

    /**
     * 线程池集合
     * <key,value> key表示线程池名称,value表示线程池
     */
    private final static Map<String, ThreadPoolExecutor> POOL_MAP = new ConcurrentHashMap<>(16);

    private final static String DEF_POOL_NAME = "custom_pool";
    private final static int DEF_CORE_POOL_SIZE = 10;
    private final static int DEF_MAXIMUM_POOL_SIZE = 30;
    private final static long DEF_KEEPALIVE_TIME_SECONDS = 30;
    private final static int DEF_QUEUE_CAPACITY = 5000;

    /**
     * 获取 ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor getPool() {
        return ThreadPoolSupport.getPool(DEF_POOL_NAME, DEF_CORE_POOL_SIZE, DEF_MAXIMUM_POOL_SIZE, DEF_KEEPALIVE_TIME_SECONDS, DEF_QUEUE_CAPACITY);
    }

    /**
     * 获取 ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor getPool(String poolName) {
        return ThreadPoolSupport.getPool(poolName, DEF_CORE_POOL_SIZE, DEF_MAXIMUM_POOL_SIZE, DEF_KEEPALIVE_TIME_SECONDS, DEF_QUEUE_CAPACITY);
    }

    /**
     * 获取 ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor getPool(int corePoolSize, int maximumPoolSize, long keepAliveTimeSeconds, int queueCapacity) {
        return ThreadPoolSupport.getPool(DEF_POOL_NAME, DEF_CORE_POOL_SIZE, DEF_MAXIMUM_POOL_SIZE, DEF_KEEPALIVE_TIME_SECONDS, DEF_QUEUE_CAPACITY);
    }

    /**
     * 获取 ThreadPoolExecutor 实例
     */
    public static ThreadPoolExecutor getPool(String poolName, int corePoolSize, int maximumPoolSize, long keepAliveTimeSeconds, int queueCapacity) {
        ThreadPoolExecutor pool = POOL_MAP.get(poolName);
        if (null != pool) {
            return pool;
        }
        synchronized (ThreadPoolSupport.class) {
            // 双重检查
            pool = POOL_MAP.get(poolName);
            if (null != pool) {
                return pool;
            }
            pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTimeSeconds, TimeUnit.SECONDS,
                    new LinkedBlockingQueue(queueCapacity), new NamedThreadFactory(poolName + "_task_"), new MyAbortPolicy(poolName));
            return pool;
        }
    }

    public static class MyAbortPolicy implements RejectedExecutionHandler {
        private String poolName;

        public MyAbortPolicy(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            logger.error("[" + poolName + "][队列溢出，执行拒绝策略]Task " + r.toString() + " rejected from " + e.toString(), e);
        }
    }
}
