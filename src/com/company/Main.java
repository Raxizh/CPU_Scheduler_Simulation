package com.company;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {
    //To run: Navigate to project 3 folder in cmd prompt, use the following command:
    //        java -cp out/production/untitled104 com.company.Main S # (spaces between inputs) (replace forward slashes with back slashes)
    static int timeQuantum;
    static LinkedList readyQueue;
    static Semaphore CPUaccess = new Semaphore(1);
    static Semaphore currentlyRunning = new Semaphore(1);
    static int id;
    static int cpuCount;
    static String cmdLineInput1;
    static int numCores;
    static Semaphore sem = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);



    public static void main(String[] args) {
        try {
            if (args[0].equals("S") || args[0].equals("s")) {
                if (args[1].equals("1")) {
                    if(args.length < 3) {
                        numCores = 1;
                        cmdLineInput1 = args[1];
                        createTaskThreads();
                        System.out.println("FirstComeFirstServe, S1");
                    }
                    else if(args[2].equals("-C") || args[2].equals("-c")) {
                        cmdLineInput1 = args[1];
                        if(Integer.parseInt(args[3]) > 0 && Integer.parseInt(args[3]) < 5) {
                            numCores = Integer.parseInt(args[3]);
                            createCoreThreads(numCores);
                            cmdLineInput1 = args[1];
                        }
                        else {
                            System.out.println("Invalid input, cores must be > 0 & < 5, defaulting to one core...");
                            numCores = 1;
                            createCoreThreads(numCores);
                            cmdLineInput1 = args[1];
                        }
                    }

                }
                else if (args[1].equals("2")) {
                    if(args.length < 4){
                        timeQuantum = Integer.parseInt(args[2]);
                        numCores = 1;
                        cmdLineInput1 = args[1];
                        createTaskThreads();
                        if (timeQuantum > 1 && timeQuantum <= 10)
                            System.out.println("RR, time quantum: " + timeQuantum + " S2# " + "cores: 1");
                        else
                            System.out.println("Quantum time must be > 1 and < 11...");
                        }
                    else if(args[3].equals("-C") || args[3].equals("-c")){
                        if(Integer.parseInt(args[4]) > 0 && Integer.parseInt(args[4]) < 5) {
                            timeQuantum = Integer.parseInt(args[2]);
                            numCores = Integer.parseInt(args[4]);
                            cmdLineInput1 = args[1];
                            createCoreThreads(numCores);
                            if(timeQuantum > 1 && timeQuantum <=10)
                                System.out.println("RR, time quantum: " + timeQuantum + " S2# " + "cores: " + args[4]);

                        }
                        else {
                            System.out.println("Invalid input, cores must be > 0 & < 5, defaulting to one core...");
                            timeQuantum = Integer.parseInt(args[2]);
                            numCores = 1;
                            cmdLineInput1 = args[1];
                            createCoreThreads(numCores);
                            if(timeQuantum > 1 && timeQuantum <=10)
                                System.out.println("RR, time quantum: " + timeQuantum + " S2# " + "cores: " + args[4]);
                        }
                    }
                }
                else if (args[1].equals("3")) {
                    cmdLineInput1 = args[1];
                    createTaskThreads();
                    System.out.println("Non-preemptive SJF, S3");
                }
                else if (args[1].equals("4")) {
                    if (args[2].equals("-C") || args[2].equals("-c")) {
                        System.out.println("Invalid input, preemptive can only be single core...");
                    }
                    cmdLineInput1 = args[1];
                    createTaskThreads();
                    System.out.println("Preemptive SJF, S4");
                }
                else {
                    System.out.println("Invalid input, please re-run...");
                }

            } else {
                System.out.println("Invalid input, please re-run...");
            }
        }
        catch (Exception e) {
            System.out.println("Invalid input, please re-run...");
        }
    }

    public class Randomizer {
        public Randomizer() {
        }
        public static int generate(int min, int max) {
            return min + (int) (Math.random() * (double) (max-min+1));
        }
    }

    public static void createTaskThreads() {
        int T = Randomizer.generate(1,25);
        int bTime;
        readyQueue = new LinkedList();

        for(int i = 0; i < T; i++) {
            bTime = Randomizer.generate(1,50);
            Task t = new Task(i, bTime);
            readyQueue.add(t);
            System.out.println("Main Thread     |  Creating process thread " + i);
        }
        printQueue();
        id = readyQueue.size();
        new CPU(0).start();

    }
    public static void printQueue() {
        System.out.println("\n" +"------------ Ready Queue ------------");
        for(int i = 0; i < readyQueue.size(); i++) {
            System.out.println(((Task)readyQueue.get(i)).print());
        }
        System.out.println("------------------------------------" + "\n");

    }

    public static void addTaskThread() {
        if(id  <= 30) {
            int bTime;
            id++;
            bTime = Randomizer.generate(2, 10);
            Task t = new Task(id, bTime);
            readyQueue.add(t);
            printQueue();
        }

    }

}
