package com.scheduler;

public class Main {
    public static void main(String[] args) {
        // Create a scheduler with 3 threads
        JobScheduler scheduler = new JobScheduler(3);

        // Create some example jobs
        Job job1 = new Job("Job 1", 1, () -> {
            System.out.println("Executing Job 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Job job2 = new Job("Job 2", 2, () -> {
            System.out.println("Executing Job 2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Job job3 = new Job("Job 3", 3, () -> {
            System.out.println("Executing Job 3");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Add dependencies
        job3.addDependency(job1);
        job3.addDependency(job2);

        // Submit jobs
        scheduler.submitJob(job1);
        scheduler.submitJob(job2);
        scheduler.submitJob(job3);

        // Wait for some time to let jobs execute
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown the scheduler
        scheduler.shutdown();

        // Print final status
        System.out.println("\nFinal job status:");
        for (Job job : scheduler.getAllJobs()) {
            System.out.println(job + " - Execution time: " + job.getExecutionTime() + "ms");
        }
    }
}