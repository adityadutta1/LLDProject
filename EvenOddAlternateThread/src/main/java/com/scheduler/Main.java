package com.scheduler;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
        public static void main(String[] args) {
            // Change this value to print different number of integers
            int maxCount = 10;

            // Create the shared printer object
            NumberPrinter printer = new NumberPrinter(maxCount);

            // Create even and odd threads
            Thread evenThread = new EvenThread(printer);
            Thread oddThread = new OddThread(printer);

            // Start both threads
            oddThread.start();
            evenThread.start();

            // Wait for both threads to complete
            try {
                oddThread.join();
                evenThread.join();
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }

            System.out.println("Printing completed!");
        }

}