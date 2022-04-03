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

    public void taskInfoDisplay() {
        try {
            Main.currentlyRunning.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentBurstTime++;
        System.out.println("Task - " + id + "    |    " + "On burst " + currentBurstTime + " Max burst " + burstTime);
        Main.currentlyRunning.release();
    }
}
