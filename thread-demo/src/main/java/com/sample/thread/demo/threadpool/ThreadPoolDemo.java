package com.sample.thread.demo.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ThreadPoolDemo {

	public static void base() {
		LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>(5);
		RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, queue, handler);

		for (int i = 0; i < 16; i++) {
			threadPool.execute(new Thread(new ThreadTest(), "线程" + i));
			System.out.println("线程池中活跃的线程数： " + threadPool.getPoolSize());

			if (queue.size() > 0) {
				System.out.println("-----队列中的阻塞线程数" + queue.size());
			}
		}
		threadPool.shutdown();
	}

	public static void threadFactoryBuilder() {
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);
		
		// guava提供的ThreadFactoryBuilder来创建线程池
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
				queue, namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		
		for (int i = 0; i < 16; i++) {
			threadPool.execute(new Thread(new ThreadTest(), "线程" + i));
			System.out.println("线程池中活跃的线程数： " + threadPool.getPoolSize());

			if (queue.size() > 0) {
				System.out.println("-----队列中的阻塞线程数" + queue.size());
			}
		}
		threadPool.shutdown();
	}

	public static void fiexedThreadPool() {
		// 默认队列 LinkedBlockingQueue是一个用链表实现的有界阻塞队列，最大长度为Integer.MAX_VALUE。
		ExecutorService executor = Executors.newFixedThreadPool(5);

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			executor.execute(new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// do nothing
					}
				}
			});
		}
	}

	public static void cachedThreadPool() {

		/**
		 * 创建一个可缓存的线程池，调用execute 将重用以前构造的线程（如果线程可用）。
		 * 如果没有可用的线程，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
		 */
		ExecutorService cachedExecutor = Executors.newCachedThreadPool();

		exec(cachedExecutor);
	}

	public static void singleThreadExecutor() {
		// 创建一个单线程化的Executor
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

		exec(singleThreadExecutor);
	}

	public static void scheduledThreadPool() {

		/**
		 * 创建一个支持定时及周期性的任务执行的线程池
		 */
		ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

		// 线程数
		final int threads = 10;
		// 用于计数线程是否执行完成
		CountDownLatch countDownLatch = new CountDownLatch(threads);

		for (int i = 0; i < threads; i++) {

			final int second = i;
			scheduledExecutor.schedule(new Runnable() {

				@Override
				public void run() {
					System.out.println("延迟" + second + "秒");
					countDownLatch.countDown();
				}

			}, i, TimeUnit.SECONDS);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---- end ----");
		scheduledExecutor.shutdown();

	}

	private static void exec(ExecutorService executor) {

		// 线程数
		final int threads = 10;
		// 用于计数线程是否执行完成
		CountDownLatch countDownLatch = new CountDownLatch(threads);

		for (int i = 0; i < threads; i++) {
			executor.execute(() -> {
				try {
					System.out.println(Thread.currentThread().getName());
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---- end ----");
		executor.shutdown();
	}

	public static void main(String[] args) {

		// 创建线程池
		// base();
		
		// guava提供的ThreadFactoryBuilder来创建线程池
		threadFactoryBuilder();

		// 创建固定线程数的线程池
		// 指定参数运行 -Xmx8m -Xms8m
		// fiexedThreadPool();

		// 创建可缓存的线程池
		// cachedThreadPool();

		// 创建单线程化的Executor
		// singleThreadExecutor();

		// 创建定时及周期性的任务执行的线程池
		// scheduledThreadPool();
	}

}
