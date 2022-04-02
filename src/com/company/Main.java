package com.company;

import java.util.concurrent.Semaphore;

public class Main {
    //To run: Navigate to project 3 folder in cmd prompt, use the following command:
    //        java -cp out/production/untitled104 com.company.Main S # (spaces between inputs) (replace forward slashes with back slashes)
    //TODO: rename the .iml file to "Project_3"
    static int timeQuantum;
    static Semaphore queue = new Semaphore(1);

    public static void main(String[] args) {
        try {
            if (args[0].equals("S") || args[0].equals("s")) {
                if (args[1].equals("1")) {
                    createTaskThreads();
                    System.out.println("FirstComeFirstServe, S1");
                }
                else if (args[1].equals("2")) {
                    timeQuantum = Integer.parseInt(args[2]);
                    if (timeQuantum > 1 && timeQuantum <= 10)
                        System.out.println("RR, time quantum: " + timeQuantum + " S2#");
                    else
                        System.out.println("Quantum time must be > 1 and < 11...");
                }
                else if (args[1].equals("3"))
                    System.out.println("Non-preemptive SJF, S3");
                else if (args[1].equals("4"))
                    System.out.println("Preemptive SJF, S4");
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
        for(int i = 0; i < T; i++) {
            new Task().start();
        }

    }

    public static void Dispatcher() {
        System.out.println("In the dispatcher...");
    }
}
