package org.ethp.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.R.attr.data;

public class EditItemActivity extends AppCompatActivity {

    private int itemIndex;
    private EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItem = (EditText) findViewById(R.id.etItem);

        Intent intent = getIntent();
        itemIndex = intent.getIntExtra("item_index", 0);
        etItem.setText(intent.getStringExtra("item"));
    }

    public void onSaveItem(View view) {
        Intent itemToSave = new Intent();
        itemToSave.putExtra("item", etItem.getText().toString());
        itemToSave.putExtra("item_index", itemIndex);
        setResult(RESULT_OK, itemToSave);
        finish();
    }
}
