package com.company;

public class Main {
    //To run: Navigate to project 3 folder in cmd prompt, use the following command:
    //        java -cp out/production/untitled104 com.company.Main S # (spaces between inputs) (replace forward slashes with back slashes)
    //TODO: rename the .iml file to "Project_3"

    public static void main(String[] args) {
        try {
            if (args[0].equals("S") || args[0].equals("s")) {
                if (args[1].equals("1"))
                    System.out.println("FirstComeFirstServe, S1");
                else if (args[1].equals("2"))
                    System.out.println("RR, time quantum: " + args[2] + " S2#");
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
        catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid input, please re-run...");
        }
    }
}
