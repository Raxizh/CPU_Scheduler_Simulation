package com.company;

import java.util.Iterator;
import java.util.Queue;

public class CPU extends Thread {
    int id;
    Iterator iterator;

    public CPU(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Using CPU " + id);
        //FCFS();
        //RoundRobin();
        NSFJ();
    }

    public int getCPUId() {
        return this.id;
    }

    public void FCFS() {
        while (!Main.readyQueue.isEmpty()) {
            try {
                Task t;
                t = (Task) Main.readyQueue.poll();
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                for(int i = 0; i < t.getMaxBurstTime(); i++) {
                    t.taskInfoDisplay();
                }
                System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
            } catch (NullPointerException e) {
                System.out.println("Task does not exist.");
            }
        }
    }

    public void RoundRobin() {
        int timeTemp = Main.timeQuantum;
        while (!Main.readyQueue.isEmpty()) {
            try {
                Task t;
                t = (Task) Main.readyQueue.poll();
                for(int i = 0; i < timeTemp; i++) {
                    t.taskInfoDisplay();
                    if(t.getCurrentBurstTime() == t.getMaxBurstTime())
                        break;
                }
                if(t.getCurrentBurstTime() != t.getMaxBurstTime())
                    Main.readyQueue.offer(t);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void NSFJ () {
        while (!Main.readyQueue.isEmpty()) {
            int shortestTaskTemp = 100;
            Task t;
            int index = 0;
            for(int i = 0; i < Main.readyQueue.size(); i++){
                if(((Task) Main.readyQueue.get(i)).getMaxBurstTime() < shortestTaskTemp) {
                    shortestTaskTemp = ((Task) Main.readyQueue.get(i)).getMaxBurstTime();
                    index = i;
                }
            }
            t = (Task) Main.readyQueue.get(index);
            for(int i = 0; i < t.getMaxBurstTime(); i++) {
                t.taskInfoDisplay();
                if(t.getCurrentBurstTime() == t.getMaxBurstTime())
                    break;
            }
            System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
            Main.readyQueue.remove(index);

        }
    }
}
