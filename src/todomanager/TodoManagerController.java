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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private ListView<?> todoTaskOne;
    @FXML
    private ListView<?> todoTaskTwo;
    @FXML
    private TextField newTask;
    
    ArrayList<String> titleArray = new ArrayList<>();
    ObservableList<String> todoArrayList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String title;
        ArrayList<String> taskR = new ArrayList<>();
        ArrayList<String> taskE = new ArrayList<>();
        ArrayList<Todo> todoL = new ArrayList<>();
        try {
            RandomAccessFile input = new RandomAccessFile("Todo.txt", "r");
            while (true) {                
                title = input.readLine();
                if(title == null) break;
                input.readLine();
                String line;
                while (true) {
                    line = input.readLine();
                    if ("##".equals(line)) {
                        break;
                    }
                    taskR.add(line);
                }
                while (true) {
                    line = input.readLine();
                    if ("###".equals(line)) {
                        break;
                    }
                    taskE.add(line);
                }
                todoL.add(0, new Todo(title, taskR, taskE));
            }
            for(Todo s: todoL) {
                titleArray.add(0, s.getTodoTitle());
                System.out.println(s.getTodoTitle());
            }
            todoArrayList.addAll(titleArray);
            todoList.setItems(todoArrayList);
            
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
            output.writeBytes(todo + "\n#\n##\n###");
            newTodo.setText("");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TodoManagerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionNewTask(ActionEvent event) {
    }
    
}
