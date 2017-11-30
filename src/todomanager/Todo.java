/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todomanager;

import java.util.ArrayList;

/**
 *
 * @author rashid
 */
public class Todo {
    private String todoTitle;
    private ArrayList<String> taskRun;
    private ArrayList<String> taskEnd;

    public Todo(String todoTitle, ArrayList<String> taskRun, ArrayList<String> taskEnd) {
        this.todoTitle = todoTitle;
        this.taskEnd = taskEnd;
        this.taskRun = taskRun;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public void setTaskEnd(ArrayList<String> taskEnd) {
        this.taskEnd = taskEnd;
    }

    public void setTaskRun(ArrayList<String> taskRun) {
        this.taskRun = taskRun;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public ArrayList<String> getTaskEnd() {
        return taskEnd;
    }

    public ArrayList<String> getTaskRun() {
        return taskRun;
    }

    

    @Override
    public String toString() {
        return "Todo{" + "todoTitle=" + todoTitle + ", taskEnd=" + taskEnd + ", taskRun=" + taskRun + '}';
    }
    
    
}
