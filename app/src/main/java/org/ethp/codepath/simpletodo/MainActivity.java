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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_ITEM_CODE = 10;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();

        readItems();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        if (!newItemText.isEmpty()) {
            itemsAdapter.add(newItemText);
        }
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        intent.putExtra("item_index", pos);
                        intent.putExtra("item", items.get(pos));
                        startActivityForResult(intent, EDIT_ITEM_CODE);
                    }
                }
        );

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
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
        try {
            items = new ArrayList<String>(FileUtils.readLines(getTodoFile()));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getTodoFile(), items);
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            String item = resultData.getExtras().getString("item");
            int itemIndex = resultData.getExtras().getInt("item_index", 0);

            if (item.isEmpty()) {
                items.remove(itemIndex);
            }
            else
            {
                items.set(itemIndex, item);
            }

            itemsAdapter.notifyDataSetChanged();
            writeItems();

            Toast.makeText(this, "Item edited.", Toast.LENGTH_SHORT).show();
        }
    }
}
