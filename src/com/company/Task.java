package com.company;

public class Task extends Thread{
    int id;
    int burstTime;
    int currentBurstTime;
    public Task(int id, int burstTime) {
        this.id = id;
        this.burstTime = burstTime;
    }
    public int getCurrentBurstTime() {
        return currentBurstTime;
    }
    public int getMaxBurstTime() {
        return burstTime;
    }
    public int getThreadId () {
        return id;
    }

    @Override
    public void run() {
        //System.out.println("Thread " + id + " has entered with burst time of: " + burstTime);
    }

    public void threadExecutor() {
        try {
            Main.currentlyRunning.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Executing thread " + id);
        /*for(int i = 0; i < burstTime; i++) {
            System.out.println("Thread - " + id + " current burst - " + currentBurstTime);
            currentBurstTime++;
        }*/
        System.out.println("Thread - " + id + " - finished executing");
        Main.currentlyRunning.release();
    }
}
