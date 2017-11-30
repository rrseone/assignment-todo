/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todomanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author rashid
 */
public class TodoManagerController implements Initializable {

    @FXML
    private ListView<String> todoList;
    @FXML
    private TextField newTodo;
    @FXML
    private VBox todoTaskOne;
    @FXML
    private VBox todoTaskTwo;
    @FXML
    private TextField newTask;
    
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<Todo> allInfo = new ArrayList<>();
    ObservableList<String> todoArrayList;
    String selectedTitle;
    ArrayList<CheckBox> rt = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            RandomAccessFile input = new RandomAccessFile("Todo.txt", "r");
            while (true) {  
                String title = input.readLine();
                //System.out.println(title);
                if(title == null) break;
                input.readLine();
                ArrayList<String> taskR = new ArrayList<>();
                String r;
                while (true) {
                    r = input.readLine();
                    if ("##".equals(r)) {
                        break;
                    }
                    //System.out.println(line);
                    taskR.add(r);
                }
                ArrayList<String> taskE = new ArrayList<>();
                String e;
                while (true) {
                    e = input.readLine();
                    if ("###".equals(e)) {
                        break;
                    }
                    //System.out.println(line);
                    taskE.add(e);
                }
                allInfo.add(0, new Todo(title, taskR, taskE));
            }
            for(Todo s: allInfo) {
                titleArray.add(s.getTodoTitle());
            }
            
            selectedTitle = allInfo.get(0).getTodoTitle();
            todoArrayList = FXCollections.observableArrayList(titleArray);
            todoList.setItems(todoArrayList);
            
            //selectedRunTask = FXCollections.observableArrayList(allInfo.get(0).getTaskRun());
            ArrayList<String> srt = new ArrayList<>(allInfo.get(0).getTaskRun());
            for(String s: srt){
                CheckBox c = new CheckBox(s);
                rt.add(c);
                
                /*c.setOnAction(click -> {
                System.out.println(c.getText());
                todoTaskTwo.getChildren().add(c);
                });*/
            }
            todoTaskOne.getChildren().addAll(rt);
            ArrayList<String> set = new ArrayList<>(allInfo.get(0).getTaskRun());
            for(String s: set){
                CheckBox c = new CheckBox(s);
                c.setSelected(true);
                todoTaskTwo.getChildren().add(c);
                
                c.setOnAction(click -> {
                    System.out.println(c.getText());
                    todoTaskOne.getChildren().add(c);
                });
            }
            /*selectedEndTask = FXCollections.observableArrayList(allInfo.get(0).getTaskEnd());
            todoTaskTwo.setItems(selectedEndTask);*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void onActionNewTodo(ActionEvent event) {
        try {
            RandomAccessFile output = new RandomAccessFile("Todo.txt", "rw");
            String todo = newTodo.getText();
            output.seek(output.length());
            output.writeBytes(todo + "\n#\n##\n###\n");
            todoArrayList.add(0, todo);
            todoList.setItems(todoArrayList);
            newTodo.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionNewTask(ActionEvent event) {
        String task = newTask.getText();
        try {
            RandomAccessFile output = new RandomAccessFile("Todo.txt", "rw");
            String t;
            while(true) {
                t = output.readLine();
                if(selectedTitle.equals(t)) {
                    output.readLine();
                    break;
                }
            }
            output.writeBytes(task + "\n");
            newTask.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionSelection(MouseEvent event) {
        selectedTitle = todoList.getSelectionModel().getSelectedItems().toString();
        selectedTitle = selectedTitle.substring(1, selectedTitle.length() - 1);
        for (Todo s: allInfo) {
            if(selectedTitle.equals(s.getTodoTitle())) {
                ArrayList<String> srt = new ArrayList<>(s.getTaskRun());
                todoTaskOne.getChildren().clear();
                for(String d: srt){
                    CheckBox c = new CheckBox(d);
                    todoTaskOne.getChildren().add(c);
                    
                    c.setOnAction(click -> {
                        System.out.println(c.getText());
                        todoTaskTwo.getChildren().add(c);
                    });
                }
                todoTaskTwo.getChildren().clear();
                for(String d: srt){
                    CheckBox c = new CheckBox(d);
                    c.setSelected(true);
                    todoTaskTwo.getChildren().add(c);
                    
                    c.setOnAction(click -> {
                        System.out.println(c.getText());
                        todoTaskOne.getChildren().add(c);
                    });
                }
                /*selectedRunTask = FXCollections.observableArrayList(s.getTaskRun());
                todoTaskOne.setItems(selectedRunTask);
                selectedEndTask = FXCollections.observableArrayList(s.getTaskEnd());
                todoTaskTwo.setItems(selectedEndTask);*/
                break;
            }
        }

    }
    
}
