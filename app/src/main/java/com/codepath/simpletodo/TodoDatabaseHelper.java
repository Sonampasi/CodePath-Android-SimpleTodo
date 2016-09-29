package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by Sonam on 9/24/2016.
 */

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TASKS = "tasks";

    // Task Table Columns
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TASK_PRIORITY = "priority";
    private static final String KEY_TASK_DUE_DATE = "due_date";

    private static final String TAG = MainActivity.class.getName();

    private static TodoDatabaseHelper sInstance;

    public static synchronized TodoDatabaseHelper getInstance(MainActivity context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_tasks_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TASK_NAME + " TEXT," +
                KEY_TASK_PRIORITY + " TEXT," +
                KEY_TASK_DUE_DATE + " DATETIME" +
                ")";
        db.execSQL(CREATE_tasks_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    // Insert a task into the database
    public void addTask(Task task) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_NAME, task.name);
            values.put(KEY_TASK_PRIORITY, task.priority);
            values.put(KEY_TASK_DUE_DATE, task.due_date);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add Task to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get all tasks in the database
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        // SELECT * FROM tasks
        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM tasks",
                        TABLE_TASKS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task();
                    newTask.id = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID));
                    newTask.name = cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME));
                    newTask.priority = cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY));
                    newTask.due_date = cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE_DATE));
                    tasks.add(newTask);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get Tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }
    // Update the task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, task.name);
        values.put(KEY_TASK_PRIORITY, task.priority);
        values.put(KEY_TASK_DUE_DATE, task.due_date);

        // Updating task name for task with that task id
        return db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[] { String.valueOf(task.getID()) });
    }
    // Delete task in the database
    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task.getID()) });
        db.close();
    }

}
