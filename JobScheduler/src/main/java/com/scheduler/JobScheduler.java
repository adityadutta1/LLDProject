package com.scheduler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

public class JobScheduler {
    private final ExecutorService executorService;
    private final Queue<Job> jobQueue;
    private final Set<Job> allJobs;
    private final AtomicBoolean isRunning;

    public JobScheduler(int numberOfThreads) {
        this.executorService = Executors.newFixedThreadPool(numberOfThreads);
        this.jobQueue = new PriorityQueue<>();
        this.allJobs = new HashSet<>();
        this.isRunning = new AtomicBoolean(true);

        // Start the scheduler thread
        Thread schedulerThread = new Thread(this::processJobs);
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }

    public void submitJob(Job job) {
        synchronized (jobQueue) {
            jobQueue.offer(job);
            allJobs.add(job);
            jobQueue.notifyAll();
        }
    }

    private void processJobs() {
        while (isRunning.get()) {
            Job job = null;
            synchronized (jobQueue) {
                while (jobQueue.isEmpty() && isRunning.get()) {
                    try {
                        jobQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (!isRunning.get()) {
                    break;
                }

                // Find the highest priority job with completed dependencies
                for (Job candidate : jobQueue) {
                    if (candidate.areDependenciesComplete()) {
                        job = candidate;
                        jobQueue.remove(job);
                        break;
                    }
                }
            }

            if (job != null) {
                final Job finalJob = job;
                executorService.submit(() -> {
                    try {
                        finalJob.execute();
                    } catch (Exception e) {
                        System.err.println("Job " + finalJob.getName() + " failed: " + e.getMessage());
                    }
                });
            }
        }
    }

    public void shutdown() {
        isRunning.set(false);
        synchronized (jobQueue) {
            jobQueue.notifyAll();
        }
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

    public Set<Job> getAllJobs() {
        return new HashSet<>(allJobs);
    }
}