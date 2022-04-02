package com.company;

public class Task extends Thread{
    @Override
    public void run() {
        try {
            Main.queue.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " has entered...");
    }
}
