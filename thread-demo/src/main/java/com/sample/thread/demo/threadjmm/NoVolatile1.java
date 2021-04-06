package com.sample.thread.demo.threadjmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：     不适用于volatile的场景
 */
public class NoVolatile1 implements Runnable {
	
	volatile int a;
    AtomicInteger realA = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Runnable r =  new NoVolatile1();
        Thread thread1 = new Thread(r);
        Thread thread2 = new Thread(r);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(((NoVolatile1) r).a);
        System.out.println(((NoVolatile1) r).realA.get());
    }
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a++;
            realA.incrementAndGet();
        }
    }

}
