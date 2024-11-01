package com.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// Main Task Scheduler class
public class ConcurrentTaskScheduler {
    private final ExecutorService executorService;
    private final int maxParallelTasks;
    private final Semaphore semaphore;

    public ConcurrentTaskScheduler(int maxParallelTasks) {
        this.maxParallelTasks = maxParallelTasks;
        this.executorService = Executors.newFixedThreadPool(maxParallelTasks);
        this.semaphore = new Semaphore(maxParallelTasks);
    }

    public Future<TaskResult> submitTask(Task task) {
        return executorService.submit(() -> executeTask(task));
    }

    public List<Future<TaskResult>> submitTasks(List<Task> tasks) {
        List<Future<TaskResult>> futures = new ArrayList<>();
        for (Task task : tasks) {
            futures.add(submitTask(task));
        }
        return futures;
    }

    private TaskResult executeTask(Task task) {
        try {
            semaphore.acquire(); // Acquire permit before execution
            System.out.println("Starting task: " + task.getName() + " (ID: " + task.getId() + ")");

            // Simulate task execution
            Thread.sleep(task.getDuration());

            return new TaskResult(
                    task.getId(),
                    "Task " + task.getName() + " completed successfully",
                    true
            );
        } catch (InterruptedException e) {
            return new TaskResult(
                    task.getId(),
                    "Task " + task.getName() + " failed: " + e.getMessage(),
                    false
            );
        } finally {
            semaphore.release(); // Release permit after execution
            System.out.println("Completed task: " + task.getName() + " (ID: " + task.getId() + ")");
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
