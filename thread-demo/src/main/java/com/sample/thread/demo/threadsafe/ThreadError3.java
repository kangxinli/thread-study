package com.sample.thread.demo.threadsafe;

import java.util.HashMap;
import java.util.Map;

public class ThreadError3 {
	
	private Map<String, String> states;

    public ThreadError3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                states = new HashMap<>();
                states.put("1", "周一");
                states.put("2", "周二");
                states.put("3", "周三");
                states.put("4", "周四");
            }
        }).start();
    }

    public Map<String, String> getStates() {
        return states;
    }

    public static void main(String[] args) throws InterruptedException {
    	ThreadError3 threadsError6 = new ThreadError3();
//        Thread.sleep(1000);
        System.out.println(threadsError6.getStates().get("1"));
    }

}
