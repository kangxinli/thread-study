package com.sample.thread.demo.threadandobject;

public class WaitAndNotify1 {
	
	public static Object object = new Object();

    static class Thread0 extends Thread {

        @Override
        public void run() {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "开始执行了===");
                try {
                	// 释放同步锁，其他线程通过notify()或者notifyAll()唤醒重新竞争获得锁
                    object.wait();
                    
                    // sleep不会释放线程自身的锁
                	// Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁。");
            }
        }
    }

    static class Thread1 extends Thread {

        @Override
        public void run() {
            synchronized (object) {
                object.notify();
                System.out.println("线程" + Thread.currentThread().getName() + "调用了notify()");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread0 thread0 = new Thread0();
        Thread1 thread1 = new Thread1();
        //保证notify先执行
        thread0.start();
        Thread.sleep(200);
        thread1.start();
    }

}
