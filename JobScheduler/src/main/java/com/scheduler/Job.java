package com.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Job implements Comparable<Job> {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final int id;
    private final String name;
    private final int priority;
    private final Runnable task;
    private JobStatus status;
    private final Set<Job> dependencies;
    private long executionTime;

    public Job(String name, int priority, Runnable task) {
        this.id = idGenerator.incrementAndGet();
        this.name = name;
        this.priority = priority;
        this.task = task;
        this.status = JobStatus.PENDING;
        this.dependencies = new HashSet<>();
        this.executionTime = 0;
    }

    public void addDependency(Job job) {
        dependencies.add(job);
    }

    public boolean areDependenciesComplete() {
        return dependencies.stream().allMatch(job -> job.getStatus() == JobStatus.COMPLETED);
    }

    public void execute() {
        try {
            status = JobStatus.RUNNING;
            long startTime = System.currentTimeMillis();
            task.run();
            executionTime = System.currentTimeMillis() - startTime;
            status = JobStatus.COMPLETED;
        } catch (Exception e) {
            status = JobStatus.FAILED;
            throw e;
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public JobStatus getStatus() { return status; }
    public Set<Job> getDependencies() { return new HashSet<>(dependencies); }
    public long getExecutionTime() { return executionTime; }

    @Override
    public int compareTo(Job other) {
        return Integer.compare(other.priority, this.priority); // Higher priority first
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id == job.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Job{id=%d, name='%s', priority=%d, status=%s}",
                id, name, priority, status);
    }
}