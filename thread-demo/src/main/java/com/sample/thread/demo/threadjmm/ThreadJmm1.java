package com.sample.thread.demo.threadjmm;

public class ThreadJmm1 {
	
	private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread one = new Thread(new Runnable() {
            @Override
            public void run() {
                a=1;
                x=b;
            }
        });

        Thread two = new Thread(new Runnable() {
            @Override
            public void run() {
                b=1;
                y=a;
            }
        });

        one.start();
        two.start();

        one.join();
        two.join();

        System.out.println("x = "+x+",y = "+y);
    }

}
