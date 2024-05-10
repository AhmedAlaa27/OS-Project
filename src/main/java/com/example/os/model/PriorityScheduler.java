package com.example.os.model;

import java.util.ArrayList;

public class PriorityScheduler {
    public ArrayList<Process> processes = new ArrayList();
    public ArrayList<Process> processes2 = new ArrayList();
    public ArrayList<Process> processes3 = new ArrayList();
    private int currentTime = 0;
    public double totalTurnaroundTime = 0.0;
    public double totalWaitingTime = 0.0;
    public double totalResponseTime = 0.0;

    public PriorityScheduler() {
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public void runScheduler() {
        while (!this.processes.isEmpty()) {
            Process selectedProcess = findNextProcess();

            if (selectedProcess == null) {
                this.currentTime++;
            } else {
                executeProcess(selectedProcess);
            }
        }
    }

    private Process findNextProcess() {
        Process selectedProcess = null;
        int minPriority = Integer.MAX_VALUE;

        for (Process process : this.processes) {
            if (process.getArrivalTime() <= this.currentTime && process.getPriority() < minPriority) {
                minPriority = process.getPriority();
                selectedProcess = process;
            }
        }

        return selectedProcess;
    }

    private void executeProcess(Process selectedProcess) {
        if (selectedProcess.getStartTime() == -1) {
            selectedProcess.setStartTime(this.currentTime);
            selectedProcess.setEndTime(selectedProcess.getBurstTime() + selectedProcess.getStartTime());
            this.currentTime += selectedProcess.getBurstTime();
            this.processes3.add(new Process(selectedProcess.getProcessId(), selectedProcess.getStartTime(), selectedProcess.getEndTime()));
        }

        completeProcessExecution(selectedProcess);
    }

    private void completeProcessExecution(Process selectedProcess) {
        selectedProcess.calculateTurnaroundTime();
        selectedProcess.calculateWaitingTime();
        selectedProcess.calculateResponseTime();
        this.totalTurnaroundTime += selectedProcess.getTurnaroundTime();
        this.totalWaitingTime += selectedProcess.getWaitingTime();
        this.totalResponseTime += selectedProcess.getResponseTime();
        this.processes2.add(selectedProcess);
        this.processes3.add(new Process(selectedProcess.getProcessId(), selectedProcess.getStartTime(), selectedProcess.getEndTime()));
        this.processes.remove(selectedProcess);
    }

}
