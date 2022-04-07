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
    public String print() {
        return "Task " + id + ", MB " + getMaxBurstTime() + ", CB " + getCurrentBurstTime();
    }

    @Override
    public void run() {
        //System.out.println("Thread " + id + " has entered with burst time of: " + burstTime);
    }

    public String taskInfoDisplay() {
        try {
            Main.currentlyRunning.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentBurstTime++;
        //System.out.print("Task - " + id + "    |    " + "On burst " + currentBurstTime + " Max burst " + burstTime);
        Main.currentlyRunning.release();
        return "Task - " + id + "    |    " + "On burst " + currentBurstTime + " Max burst " + burstTime;
    }
}
