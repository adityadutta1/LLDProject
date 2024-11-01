package com.scheduler;


// Result class to store task execution results
class TaskResult {
    private final String taskId;
    private final String result;
    private final boolean success;

    public TaskResult(String taskId, String result, boolean success) {
        this.taskId = taskId;
        this.result = result;
        this.success = success;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }
}
