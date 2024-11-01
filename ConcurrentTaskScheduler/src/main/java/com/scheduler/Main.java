package com.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Create scheduler with max 3 parallel tasks
        ConcurrentTaskScheduler scheduler = new ConcurrentTaskScheduler(3);

        // Create sample tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task1", 2000));
        tasks.add(new Task("Task2", 3000));
        tasks.add(new Task("Task3", 1000));
        tasks.add(new Task("Task4", 4000));
        tasks.add(new Task("Task5", 2000));

        try {
            // Submit all tasks
            List<Future<TaskResult>> futures = scheduler.submitTasks(tasks);

            // Wait for and print results
            for (Future<TaskResult> future : futures) {
                try {
                    TaskResult result = future.get();
                    System.out.println(result.getResult() + " - Success: " + result.isSuccess());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error getting task result: " + e.getMessage());
                }
            }
        } finally {
            // Shutdown the scheduler
            scheduler.shutdown();
        }
    }
}