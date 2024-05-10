package com.example.os;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;


public class Controller {
    @FXML
    private Label myLabel;
    @FXML
    private TextField numberTextfield;
    @FXML
    private TextField quantumTextField;
    @FXML
    private Button myButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void Enter(ActionEvent event){
       try{
           int numberOfProcesses = Integer.parseInt(numberTextfield.getText());
           int totalTimeQuantum = Integer.parseInt(quantumTextField.getText());
           if(numberOfProcesses < 1){
               numberTextfield.setText("");
               myLabel.setText("Please Enter Number Greater Than 0!");
           } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene2.fxml"));
            root = loader.load();
            Controller2 controller2 = loader.getController();
            controller2.displayNumberOfProcesses(numberOfProcesses, totalTimeQuantum);
               stage = (Stage)((Node)event.getSource()).getScene().getWindow();
               scene = new Scene(root,800,400);
               stage.setScene(scene);
               stage.show();
           }
       } catch (Exception e) {
           myLabel.setText("Please Enter A number");
       }
    }
}