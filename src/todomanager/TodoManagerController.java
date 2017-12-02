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
    ArrayList<CheckBox> et = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (RandomAccessFile input = new RandomAccessFile("Todo.txt", "r")) {
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
            
            ArrayList<String> srt = new ArrayList<>(allInfo.get(0).getTaskRun());
            for(String s: srt){
                CheckBox c = new CheckBox(s);
                rt.add(c);
            }
            todoTaskOne.getChildren().addAll(rt);
            
            ArrayList<String> set = new ArrayList<>(allInfo.get(0).getTaskRun());
            for(String s: set){
                CheckBox c = new CheckBox(s);
                c.setSelected(true);
                et.add(c);
            }
            todoTaskTwo.getChildren().addAll(et);
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found!");
        } catch (IOException ex) {
            System.out.println("Find some error!");
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
            allInfo.add(new Todo(todo, new ArrayList<>(), new ArrayList<>()));
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
            CheckBox c = new CheckBox(task);
            todoTaskOne.getChildren().add(c);
            for(Todo s: allInfo){
                if(selectedTitle.equals(s.getTodoTitle())){
                    ArrayList<String> d = new ArrayList<>();
                    d.addAll(s.getTaskRun());
                    d.add(task);
                    todoTaskOne.getChildren().clear();
                    for(String a: d){
                        CheckBox l = new CheckBox(a);
                        todoTaskOne.getChildren().add(l);
                        l.setOnAction(click -> {
                            System.out.println(l.getText());
                            todoTaskTwo.getChildren().add(l);
                        });
                    }
                }
            }
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
                ArrayList<String> set = new ArrayList<>(s.getTaskEnd());
                todoTaskTwo.getChildren().clear();
                for(String d: set){
                    CheckBox c = new CheckBox(d);
                    c.setSelected(true);
                    todoTaskTwo.getChildren().add(c);
                    
                    c.setOnAction(click -> {
                    System.out.println(c.getText());
                    todoTaskOne.getChildren().add(c);
                    });
                }
                break;
            }
        }

    }

    /*@FXML
    private void onActionTaskCompleted(MouseEvent event) {
    ArrayList<CheckBox> prt = new ArrayList<>(rt);
    for(CheckBox c: prt) {
    c.setOnAction(click -> {
    rt.remove(c);
    et.add(c);
    todoTaskOne.getChildren().clear();
    todoTaskTwo.getChildren().clear();
    todoTaskOne.getChildren().addAll(rt);
    todoTaskTwo.getChildren().addAll(et);
    });
    }
    }
    
    @FXML
    private void onActionSendUncompleted(MouseEvent event) {
    ArrayList<CheckBox> prt = new ArrayList<>(et);
    for(CheckBox c: prt) {
    c.setOnAction(click -> {
    et.remove(c);
    rt.add(c);
    todoTaskTwo.getChildren().clear();
    todoTaskOne.getChildren().clear();
    todoTaskOne.getChildren().addAll(rt);
    todoTaskTwo.getChildren().addAll(et);
    });
    }
    }*/

    
}
