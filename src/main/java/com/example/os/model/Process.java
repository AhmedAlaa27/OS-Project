package com.example.os.model;

public class Process {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int startTime;
    private int endTime;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;

    public Process(int processId, int arrivalTime, int burstTime, int priority) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.startTime = -1;
        this.endTime = -1;
    }

    public Process(int processId, int startTime, int endTime) {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getProcessId() {
        return this.processId;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getBurstTime() {
        return this.burstTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return this.turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getResponseTime() {
        return this.responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void calculateTurnaroundTime() {
        this.turnaroundTime = this.endTime - this.arrivalTime;
    }

    public void calculateWaitingTime() {
        this.waitingTime = this.turnaroundTime - this.burstTime;
    }

    public void calculateResponseTime() {
        this.responseTime = this.startTime - this.arrivalTime;
    }
}
