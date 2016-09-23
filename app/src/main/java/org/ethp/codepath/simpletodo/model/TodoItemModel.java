package org.ethp.codepath.simpletodo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import static android.R.attr.category;
import static android.R.attr.name;

/**
 * Created by eddie_thp on 9/22/16.
 */
@Table(name = "TodoItems")
public class TodoItemModel extends Model {

    @Column(name = "listIdx")
    private int index;

    @Column(name = "task")
    private String task;

    public TodoItemModel() {
        super();
    }

    public TodoItemModel(int index, String task) {
        super();
        this.index = index;
        this.task = task;
    }

    public void setTask(String task)
    {
        this.task = task;
    }

    public String toString()
    {
        return task;
    }
}
