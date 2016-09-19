package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    int pos;
    ListView lvItems;
    EditText etNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView)findViewById(R.id.lvItems);

        lvItems.setAdapter(aToDoAdapter);
        etNewItem = (EditText)findViewById(R.id.etNewItem);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(getApplicationContext(), "Item Deleted !!!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("edit_item", lvItems.getItemAtPosition(position).toString());
                startActivityForResult(i, 200);
//                startActivity(i); // brings up the second activity
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 200) {
            String edit_text = data.getExtras().getString("edit_text");

            todoItems.set(pos, edit_text);
            aToDoAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item Updated!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateArrayItems(){
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1, todoItems);
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){
            todoItems = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        }catch (IOException e){

        }
    }

    public void onAddItem(View view) {
        aToDoAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item Added !!!", Toast.LENGTH_SHORT).show();
    }


}
