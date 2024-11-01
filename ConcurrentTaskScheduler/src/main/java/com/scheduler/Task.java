package com.scheduler;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// Task class representing a unit of work
class Task {
    private final String id;
    private final String name;
    private final int duration;

    public Task(String name, int duration) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}

