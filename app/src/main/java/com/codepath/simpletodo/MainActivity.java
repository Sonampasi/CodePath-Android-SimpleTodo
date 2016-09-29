package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskDialogListener{

    TodoDatabaseHelper databaseHelper;
    Task task;
    ArrayList<Task> taskList;
    ListAdapter adapter;
    ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task = new Task();
        // Get singleton instance of database
       databaseHelper = TodoDatabaseHelper.getInstance(this);

        // Get all posts from database
        populateTaskList();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Task task_obj = adapter.getItem(position);
                showEditDialog(task_obj,position);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent i = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivityForResult(i, 200);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private void populateTaskList() {
        taskList = databaseHelper.getAllTasks();
        // Construct the data source
        final ArrayList<Task> arrayOfTasks = taskList;
        // Create the adapter to convert the array to views
        adapter = new ListAdapter(this, arrayOfTasks);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(adapter);
        listView.setDivider(null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 200) {
            Task newTask = new Task();
            newTask.id = getTaskId();
            newTask.name = data.getExtras().getString("name");
            newTask.priority =data.getExtras().getString("priority");
            newTask.due_date =data.getExtras().getString("due_date");
            databaseHelper.addTask(newTask);
            adapter.add(newTask);
            Toast.makeText(this, "New task created!!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void showEditDialog(Task task_obj,int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance("Edit Task",task_obj,position);
        editTaskDialogFragment.show(getFragmentManager(), "fragment_edit_name");
    }

    @Override
    public void onFinishEditDialog(Task task, int position) {
        task.name= task.getName();
        databaseHelper.updateTask(task);
        taskList.set(position,task);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }
}
