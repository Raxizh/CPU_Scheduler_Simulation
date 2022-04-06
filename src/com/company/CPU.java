package com.company;


public class CPU extends Thread {
    int id;

    public CPU(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Using CPU " + id);
        if(Main.cmdLineInput1.equals("1")) {
            FCFS();
        }
        else if (Main.cmdLineInput1.equals("2")) {
            RoundRobin();
        }
        else if (Main.cmdLineInput1.equals("3")) {
            NSFJ();
        }
        else if (Main.cmdLineInput1.equals("4")) {
            PSJF();
        }

    }

    public int getCPUId() {
        return this.id;
    }

    public void FCFS() {
        while (!Main.readyQueue.isEmpty()) {
            Main.printQueue();
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
                Main.printQueue();
                Task t;
                t = (Task) Main.readyQueue.poll();
                System.out.println("\n");
                for(int i = 0; i < timeTemp; i++) {
                    t.taskInfoDisplay();
                    if(t.getCurrentBurstTime() == t.getMaxBurstTime())
                        break;
                }
                if(t.getCurrentBurstTime() != t.getMaxBurstTime())
                    Main.readyQueue.offer(t);
                else {
                    System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void NSFJ () {
        Main.printQueue();
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
            Main.printQueue();

        }
    }

    public void PSJF() {
        Main.printQueue();
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
                int random = Main.Randomizer.generate(3,10);
                t.taskInfoDisplay();
                if(Main.readyQueue.size() >= 25) {
                    random = 0;
                }
                if(random == 10) {
                    Main.addTaskThread();
                    Main.printQueue();
                }
                if(((Task) Main.readyQueue.getLast()).getMaxBurstTime() < t.getMaxBurstTime()) {
                    t = (Task) Main.readyQueue.getLast();
                    System.out.println("Task " + t.getThreadId() + " has preempted with a burst time of " + t.getMaxBurstTime());
                }
                if(t.getCurrentBurstTime() >= t.getMaxBurstTime()) {
                    Main.readyQueue.remove(t);
                    System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
                    Main.printQueue();
                    break;
                }

            }


        }
    }
}
