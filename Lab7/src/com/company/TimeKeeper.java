package com.company;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class TimeKeeper extends Thread {

    int sec;
    public boolean running = true;

    public TimeKeeper(String name, int sec) {
        super(name);
        this.setDaemon(true);
        this.sec = sec;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": counting");
        long start = System.currentTimeMillis();
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long elapsedTime = System.currentTimeMillis() - start;
            if (sec != 600) // pentru a juca manual am facut o exceptie in TimeKeeper pentru nu a afisa timpul ce trece pt timeLimit 600
                System.out.println(elapsedTime / 100);
            if (elapsedTime / 1000 >= sec) {
                System.out.println(Thread.currentThread().getName() + ": Time Out");
                return;
            }
        }
    }
}

