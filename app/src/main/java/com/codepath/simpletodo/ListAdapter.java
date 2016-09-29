package com.codepath.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Sonam on 9/25/2016.
 */

public class ListAdapter extends ArrayAdapter<Task> {

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView priority;
        TextView dueDate;
        ImageView deleteTask;

    }

    TodoDatabaseHelper databaseHelper = new TodoDatabaseHelper(this.getContext());
    public ListAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_todo, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.tvPriority);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            viewHolder.deleteTask = (ImageView) convertView.findViewById(R.id.action_delete);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(task.name);
        viewHolder.priority.setText(task.priority);
        viewHolder.dueDate.setText(task.due_date);
        if(task.priority.equals("High")){
            viewHolder.priority.setTextColor(Color.RED);
        }
        else if(task.priority.equals("Medium")){
            viewHolder.priority.setTextColor(ContextCompat.getColor(getContext(),R.color.colorDarkYellow));
        }
        else {
            viewHolder.priority.setTextColor(Color.GREEN);
        }
        // Attach the click event handler
        viewHolder.deleteTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(task);
                databaseHelper.deleteTask(task);
                notifyDataSetChanged();
                return false;
            }

        });

        // Return the completed view to render on screen
        return convertView;
    }
}
