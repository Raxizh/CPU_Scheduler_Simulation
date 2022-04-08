package com.company;

public class CPU extends Thread {
    int id;

    public CPU(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        if(Main.cmdLineInput1.equals("1")) {
            //barrier
            try {
                Main.mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.cpuCount++;
            System.out.print("");
            Main.mutex.release();
            if (Main.cpuCount == Main.numCores) {
                System.out.println("Now releasing dispatchers.");
                Main.sem.release(Main.numCores);
            }

            //barrier
            FCFSDispatcher();
        }
        else if (Main.cmdLineInput1.equals("2")) {
            try {
                Main.mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.cpuCount++;
            System.out.print("");
            Main.mutex.release();
            System.out.println("CPU " + getCPUId());
            if (Main.cpuCount == Main.numCores) {
                System.out.println("Now releasing dispatchers.");
                Main.sem.release(Main.numCores);
            }
            try {
                RoundRobinDispatcher();
            } catch(Exception e) {System.exit(1);}
        }
        else if (Main.cmdLineInput1.equals("3")) {
            try {
                Main.mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.cpuCount++;
            System.out.print("");
            Main.mutex.release();
            if (Main.cpuCount == Main.numCores) {
                System.out.println("Now releasing dispatchers.");
                Main.sem.release(Main.numCores);
            }
            try {
                NSFJDispatcher(); }
            catch(IndexOutOfBoundsException e) {
                System.out.println("Ready queue is empty...");
            }

        }
        else if (Main.cmdLineInput1.equals("4")) {
            PSJFDispatcher();
        }
    }

    public int getCPUId() {
        return this.id;
    }

    public void FCFSDispatcher() {
        try {
            System.out.println("Dispatcher " + getCPUId() + "   |  Running FCFS algorithm");
            Main.sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!Main.readyQueue.isEmpty()) {
            try {
                Task t;
                Main.CPUaccess.acquire();
                t = (Task) Main.readyQueue.poll();
                System.out.println("Task " + t.getThreadId() + " using Dispatcher " + getCPUId() + "\n");
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                for(int i = 0; i < t.getMaxBurstTime(); i++) {
                    System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                }
                Main.CPUaccess.release();
                System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");

            } catch (Exception e) {

                System.exit(1);
            }
        }
        Main.sem.release();

    }

    public void RoundRobinDispatcher() throws NullPointerException{
        try {
            Main.sem.acquire();
            System.out.println("Dispatcher " + getCPUId() + "   |  Running RR algorithm, Time Quantum = " + Main.timeQuantum);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        int timeTemp = Main.timeQuantum;
        while (!Main.readyQueue.isEmpty()) {
            try {
                Task t;
                Main.CPUaccess.acquire();
                t = (Task) Main.readyQueue.poll();
                if(t == null) {
                    Main.CPUaccess.release();
                    continue;
                }
                System.out.println("Task " + t.getThreadId() + " using Dispatcher " + getCPUId() + "\n");
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                Main.CPUaccess.release();
                for(int i = 0; i < timeTemp; i++) {
                    System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                    if(t.getCurrentBurstTime() == t.getMaxBurstTime())
                        break;
                }
                if(t.getCurrentBurstTime() != t.getMaxBurstTime()){
                    Main.CPUaccess.acquire();
                    Main.readyQueue.add(t);
                    Main.CPUaccess.release();
                }
                else {
                    System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void NSFJDispatcher() throws IndexOutOfBoundsException{
        try {
            System.out.println("Dispatcher " + getCPUId() + "   |  Running Non Preemptive - Shortest Job First");
            Main.sem.acquire();
        }
        catch(Exception e) {e.printStackTrace();}
        while (!Main.readyQueue.isEmpty()) {
            try {
                int shortestTaskTemp = 100;
                Task t;
                Main.CPUaccess.acquire();
                int index = 0;
                for (int i = 0; i < Main.readyQueue.size(); i++) {
                    if (((Task) Main.readyQueue.get(i)).getMaxBurstTime() < shortestTaskTemp) {
                        shortestTaskTemp = ((Task) Main.readyQueue.get(i)).getMaxBurstTime();
                        index = i;
                    }
                }
                t = (Task) Main.readyQueue.remove(index);
                System.out.println("Task " + t.getThreadId() + " using Dispatcher " + getCPUId() + "\n");
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                for (int i = 0; i < t.getMaxBurstTime(); i++) {
                    System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                    if (t.getCurrentBurstTime() >= t.getMaxBurstTime())
                        break;
                }
                Main.CPUaccess.release();
                System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
            }
            catch(Exception e) {
                System.exit(1);}

        }
        Main.sem.release();
    }

    public void PSJFDispatcher() {

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
                int random = Main.Randomizer.generate(3, 10); //Determines how often task will be created (30% chance)
                System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                if (Main.readyQueue.size() >= 25) {
                    random = 0;
                }
                if (random == 10) {
                    Main.addTaskThread();
                }
                if (((Task) Main.readyQueue.getLast()).getMaxBurstTime() < t.getMaxBurstTime()) {
                    t = (Task) Main.readyQueue.getLast();
                    System.out.println("Task " + t.getThreadId() + " has preempted with a burst time of " + t.getMaxBurstTime());
                }
                if (t.getCurrentBurstTime() >= t.getMaxBurstTime()) {
                    Main.readyQueue.remove(t);
                    System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
                    break;
                }
            }
        }

        System.exit(1);
    }
}
