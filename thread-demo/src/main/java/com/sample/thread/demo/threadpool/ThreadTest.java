package com.sample.thread.demo.threadpool;

public class ThreadTest implements Runnable {
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(300);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
