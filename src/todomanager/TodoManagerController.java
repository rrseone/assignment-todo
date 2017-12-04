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
import javafx.scene.control.Label;
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
    @FXML
    private Label setNotification;
    
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<Todo> allInfo = new ArrayList<>();
    ObservableList<String> todoArrayList;
    String selectedTitle;
    ArrayList<CheckBox> rt = new ArrayList<>();
    ArrayList<CheckBox> et = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (RandomAccessFile input = new RandomAccessFile("Todo.txt", "rw")) {
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
                allInfo.add(new Todo(title, taskR, taskE));
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
                c.setOnAction(click -> {
                    rt.remove(c);
                    et.add(c);
                    allInfo.get(0).removeTaskRun(s);
                    allInfo.get(0).addTaskEnd(s);
                    taskRefresh();
                    fileRewrite(allInfo);
                });
            }
            
            ArrayList<String> set = new ArrayList<>(allInfo.get(0).getTaskEnd());
            for(String s: set){
                CheckBox c = new CheckBox(s);
                c.setSelected(true);
                et.add(c);
                c.setOnAction(click -> {
                    et.remove(c);
                    rt.add(c);
                    allInfo.get(0).removeTaskEnd(s);
                    allInfo.get(0).addTaskRun(s);
                    taskRefresh();
                    fileRewrite(allInfo);
                });
            }
            taskRefresh();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found!");
        } catch (IOException ex) {
            System.out.println("Find some error!");
        }
        
    }
    

    @FXML
    private void onActionNewTodo(ActionEvent event) {
        String todo = newTodo.getText();
        todoArrayList.add(todo);
        todoList.setItems(todoArrayList);
        selectedTitle = todo;
        allInfo.add(new Todo(todo, new ArrayList<>(), new ArrayList<>()));
        fileRewrite(allInfo);
        newTodo.setText("");
        rt.clear();
        et.clear();
        taskRefresh();
        setNotification.setText("Saved new todo.");
    }

    @FXML
    private void onActionNewTask(ActionEvent event) {
        String task = newTask.getText();
            
        for(Todo s: allInfo){
            if(selectedTitle.equals(s.getTodoTitle())){
                s.addTaskRun(task);
                ArrayList<String> d = new ArrayList<>(s.getTaskRun());
                ArrayList<String> y = new ArrayList<>(s.getTaskEnd());
                rt.clear();
                et.clear();
                for(String a: d){
                    CheckBox l = new CheckBox(a);
                    rt.add(l);
                    l.setOnAction(click -> {
                        rt.remove(l);
                        et.add(l);
                        s.removeTaskRun(a);
                        s.addTaskEnd(a);
                        fileRewrite(allInfo);
                        taskRefresh();
                    });
                }
                for(String a: y){
                    CheckBox l = new CheckBox(a);
                    l.setSelected(true);
                    et.add(l);
                    l.setOnAction(click -> {
                        et.remove(l);
                        rt.add(l);
                        s.removeTaskEnd(a);
                        s.addTaskRun(a);
                        fileRewrite(allInfo);
                        taskRefresh();
                    });
                }
                taskRefresh();
                break;
            }
        }
        fileRewrite(allInfo);
        newTask.setText("");
        setNotification.setText("Saved new task.");
    }

    @FXML
    private void onActionSelection(MouseEvent event) {
        selectedTitle = todoList.getSelectionModel().getSelectedItems().toString();
        selectedTitle = selectedTitle.substring(1, selectedTitle.length() - 1);
        for (Todo s: allInfo) {
            if(selectedTitle.equals(s.getTodoTitle())) {
                ArrayList<String> srt = new ArrayList<>(s.getTaskRun());
                rt.clear();
                et.clear();
                for(String d: srt){
                    CheckBox c = new CheckBox(d);
                    rt.add(c);
                    c.setOnAction(click -> {
                        rt.remove(c);
                        et.add(c);
                        s.removeTaskRun(d);
                        s.addTaskEnd(d);
                        fileRewrite(allInfo);
                        taskRefresh();
                    });
                }
                ArrayList<String> set = new ArrayList<>(s.getTaskEnd());
                for(String d: set){
                    CheckBox c = new CheckBox(d);
                    c.setSelected(true);
                    et.add(c);
                    c.setOnAction(click -> {
                        et.remove(c);
                        rt.add(c);
                        s.removeTaskEnd(d);
                        s.addTaskRun(d);
                        fileRewrite(allInfo);
                        taskRefresh();
                    });
                }
                taskRefresh();
                break;
            }
        }

    }

    public void taskRefresh() {
        todoTaskOne.getChildren().clear();
        todoTaskTwo.getChildren().clear();
        todoTaskTwo.getChildren().addAll(et);
        todoTaskOne.getChildren().addAll(rt);
    }
    public void fileRewrite(ArrayList<Todo> all) {
        try(RandomAccessFile rewrite = new RandomAccessFile("Todo.txt", "rw")){
            for (Todo info : all) {
                rewrite.writeBytes(info.getTodoTitle() + "\n#\n");
                for(String s: info.getTaskRun()) {
                    rewrite.writeBytes(s + "\n");
                }
                rewrite.writeBytes("##\n");
                for(String s: info.getTaskEnd()) {
                    rewrite.writeBytes(s + "\n");
                }
                rewrite.writeBytes("###\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnActionExit(ActionEvent event) {
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
    }

    @FXML
    private void onActionDelete(ActionEvent event) {
    }

    @FXML
    private void onActionAbout(ActionEvent event) {
    }
}
