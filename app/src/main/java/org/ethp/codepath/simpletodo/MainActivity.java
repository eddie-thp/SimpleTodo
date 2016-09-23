package org.ethp.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;
import org.ethp.codepath.simpletodo.model.TodoItemModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;
import static android.R.attr.data;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_ITEM_CODE = 10;

    private List<TodoItemModel> items;
    private ArrayAdapter<TodoItemModel> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<TodoItemModel>();

        readItems();

        itemsAdapter = new ArrayAdapter<TodoItemModel>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        if (!newItemText.isEmpty()) {
            TodoItemModel todoItem = new TodoItemModel(items.size(), newItemText);
            todoItem.save();

            itemsAdapter.add(todoItem);
        }
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        intent.putExtra("item_index", pos);
                        intent.putExtra("item", items.get(pos).toString());
                        startActivityForResult(intent, EDIT_ITEM_CODE);
                    }
                }
        );

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        TodoItemModel im = items.remove(pos);
                        im.delete();
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    private File getTodoFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        return todoFile;
    }

    private void readItems() {
            items = new Select()
                    .from(TodoItemModel.class).orderBy("listIdx ASC")
                    .execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            String item = resultData.getExtras().getString("item");
            int itemIndex = resultData.getExtras().getInt("item_index", 0);

            if (item.isEmpty()) {
                TodoItemModel im = items.remove(itemIndex);
                im.delete();
            }
            else
            {
                TodoItemModel im = items.get(itemIndex);
                im.setTask(item);
                im.save();
            }

            itemsAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Item edited.", Toast.LENGTH_SHORT).show();
        }
    }
}
