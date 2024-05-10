package com.example.os;

import com.example.os.model.PriorityScheduler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.os.model.Process;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.os.model.GanttChart.ExtraData;
import com.example.os.model.GanttChart;

import java.io.IOException;

public class Controller2 {

    @FXML
    Label numberLabel;
    @FXML
    VBox container;

    private int numberOfProcesses;
    private int totalTimeQuantum;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean valid = true;

    private ArrayList<Process> processes3;

    TextField[] arrivalTime = new TextField[1000];
    TextField[] burstTime = new TextField[1000];
    TextField[] priority = new TextField[1000];

    public void displayNumberOfProcesses(int numberOfProcesses, int totalTimeQuantum) {
        this.numberOfProcesses = numberOfProcesses;
        this.totalTimeQuantum = totalTimeQuantum;
        HBox hBox = new HBox(10);
        numberLabel.setText("Number of Procesess You Entered: " + numberOfProcesses);
        hBox.getChildren().add(numberLabel);
        hBox.setPadding(new Insets(15));
        container.getChildren().add(hBox);

        for (int i = 0; i < numberOfProcesses; i++) {
            HBox hBox1 = new HBox(10);
            TextField arrivalTime = new TextField();
            TextField burstTime = new TextField();
            TextField priority = new TextField();
            hBox1.getChildren().addAll(new Label("Processes Details: " + (i + 1)), new Label("arrivalTime"), arrivalTime, new Label("burstTime"), burstTime, new Label("priority"), priority);
            this.arrivalTime[i] = arrivalTime;
            this.burstTime[i] = burstTime;
            this.priority[i] = priority;

            hBox1.setPadding(new Insets(10));
            container.getChildren().add(hBox1);
        }
    }
    private boolean isNumeric(String text){
        try {
            int num = Integer.parseInt(text);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public void simulation() {
        PriorityScheduler scheduler = new PriorityScheduler();
        boolean valid = true; // Initialize valid flag outside the loop
        for (int i = 0; i < this.numberOfProcesses; i++) {
            String arrivalTime = this.arrivalTime[i].getText();
            String burstTime = this.burstTime[i].getText();
            String priority = this.priority[i].getText();
            if (arrivalTime.isEmpty() || burstTime.isEmpty() || priority.isEmpty() ||
                    !arrivalTime.matches("\\d+") || !burstTime.matches("\\d+") || !priority.matches("\\d+")) {
                numberLabel.setText("Please, Enter valid data :D");
                valid = false;
                break; // Break out of the loop if any input is invalid
            } else {
                scheduler.addProcess(new Process(i + 1, Integer.parseInt(arrivalTime), Integer.parseInt(burstTime), Integer.parseInt(priority)));
            }
        }

        if (valid) {
            container.getChildren().clear();
            scheduler.runScheduler();
            for (Process process : scheduler.processes2) {
                process.calculateTurnaroundTime();
                process.calculateWaitingTime();
                process.calculateResponseTime();
                HBox hbox2 = new HBox(10);
                hbox2.getChildren().addAll(
                        new Label("process" +process.getProcessId()+":"),
                        new Label("Waiting Time: "+process.getWaitingTime()), // Fixed typo in label text
                        new Label("Response Time: "+process.getResponseTime()),
                        new Label("TurnAround Time: "+process.getTurnaroundTime())
                );
                hbox2.setPadding(new Insets(15));
                container.getChildren().add(hbox2);
            }
            int numCompletedProcesses = this.numberOfProcesses - scheduler.processes.size();
            double avgTurnaroundTime = scheduler.totalTurnaroundTime / numCompletedProcesses;
            double avgWaitingTime = scheduler.totalWaitingTime / numCompletedProcesses;
            double avgResponseTime = scheduler.totalResponseTime / numCompletedProcesses;
            this.processes3 = scheduler.processes3;

            HBox hbox2 = new HBox(10);
            hbox2.getChildren().addAll(
                    new Label("Average Turnaround Time: " + avgTurnaroundTime),
                    new Label("Average Waiting Time: " + avgWaitingTime),
                    new Label("Average Response Time: " + avgResponseTime)
            );
            hbox2.setPadding(new Insets(15));
            container.getChildren().add(hbox2);
            Button b = new Button("Show Gantt Chart");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        showGanttchart(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            container.getChildren().add(b);
        }
    }


    public void showGanttchart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ganttchart.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Gantt Chart Sample");

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(this.totalTimeQuantum);
        xAxis.setTickUnit(1);
        final GanttChart<Number,String> chart = new GanttChart<Number,String>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.BLUE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.GREEN);
        yAxis.setTickLabelGap(10);

        chart.setTitle("Gantt Chart");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        XYChart.Series[] series = new XYChart.Series[this.processes3.size()];
        for (int y = 0; y < this.processes3.size(); y++) {
            series[y] = new XYChart.Series();
            String machine = "Processes" + this.processes3.get(y).getProcessId();
            series[y].getData().add(new XYChart.Data(
                    this.processes3.get(y).getStartTime(),
                    machine,
                    new ExtraData(this.processes3.get(y).getEndTime() - this.processes3.get(y).getStartTime(), "status-red")
            ));
            chart.getData().addAll(series[y]);
        }


        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());

        Scene scene  = new Scene(chart,620,350);
        stage.setScene(scene);
        stage.show();
    }
}

