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
                Main.sem.release();
            }
            RoundRobinDispatcher();
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
            System.out.println("CPU " + getCPUId());
            if (Main.cpuCount == Main.numCores) {
                System.out.println("Now releasing dispatchers.");
                Main.sem.release(Main.numCores); // release numofcores??
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
                System.out.println("Task does not exist.");
                System.exit(1);
            }
        }
        Main.sem.release();

    }

    public void RoundRobinDispatcher() {
        try {
            System.out.println("Dispatcher " + getCPUId() + "   |  Running RR algorithm, Time Quantum = " + Main.timeQuantum);
            Main.sem.acquire();
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
                System.out.println("Task " + t.getThreadId() + " using Dispatcher " + getCPUId() + "\n");
                Main.CPUaccess.release();
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                for(int i = 0; i < timeTemp; i++) {
                    System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                    if(t.getCurrentBurstTime() == t.getMaxBurstTime())
                        break;
                }
                if(t.getCurrentBurstTime() != t.getMaxBurstTime()){
                    Main.CPUaccess.acquire();
                    Main.readyQueue.offer(t);
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
        //System.out.println("------------- Using CPU " + getCPUId() + " -------------");
        //Main.printQueue();
        try {
            System.out.println("Dispatcher " + getCPUId() + "   |  Running Non Preemptive - Shortest Job First");
            Main.sem.acquire();
        }
        catch(Exception e) {e.printStackTrace();}
        while (!Main.readyQueue.isEmpty()) {
            try {
                int shortestTaskTemp = 100;
                Task t;
                //semaphore to find shortest before CPU 2
                Main.CPUaccess.acquire();
                /*if(Main.readyQueue.isEmpty())
                    break;*/
                int index = 0;
                for (int i = 0; i < Main.readyQueue.size(); i++) {
                    if (((Task) Main.readyQueue.get(i)).getMaxBurstTime() < shortestTaskTemp) {
                        shortestTaskTemp = ((Task) Main.readyQueue.get(i)).getMaxBurstTime();
                        index = i;
                    }
                }
                //t = (Task) Main.readyQueue.get(index);
                t = (Task) Main.readyQueue.remove(index);
                System.out.println("Task " + t.getThreadId() + " using Dispatcher " + getCPUId() + "\n");
                System.out.println("Task - " + t.getThreadId() + "    |    " + " MB=" + t.getMaxBurstTime() + ", CB=" + t.getCurrentBurstTime());
                for(int i = 0; i < t.getMaxBurstTime(); i++) {
                    System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                    if(t.getCurrentBurstTime() >= t.getMaxBurstTime())
                        break;
                }
                Main.CPUaccess.release();
                System.out.println("Task " + t.getThreadId() + " has finished executing" + "\n");
                if(Main.readyQueue.isEmpty()) {
                    System.out.println("******queue is empty");
                    //break;
                }
                if(index >= Main.readyQueue.size())
                    System.out.println("********index >= size");
                    //break;
                //Main.readyQueue.remove(index);
                }
            catch(Exception e) {
                System.out.println("index out of bounds");
                System.exit(1);}

        }
        Main.sem.release();
    }

    public void PSJFDispatcher() {
        System.out.println("------------- Using CPU " + getCPUId() + " -------------");
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
                System.out.println(t.taskInfoDisplay() + " On CPU " + getCPUId());
                if(Main.readyQueue.size() >= 25) {
                    random = 0;
                }
                if(random == 10) {
                    Main.addTaskThread();
                    //Main.printQueue();
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
