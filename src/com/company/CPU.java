package com.company;

import java.util.Queue;

public class CPU extends Thread{
    int id;
    public CPU (int id) {
        this.id = id;
    }
    @Override
    public void run() {
        System.out.println("Using CPU " + id);

        FCFS();
    }

    public void FCFS() {
        while(!Main.readyQueue.isEmpty()) {
            try {
                Task t;
                t = (Task) Main.readyQueue.poll();
                t.threadExecutor();
            } catch (NullPointerException e) {
                System.out.println("Task does not exist.");
            }
        }

    }

}
