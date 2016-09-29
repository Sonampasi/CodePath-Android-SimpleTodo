package com.codepath.simpletodo;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;

/**
 * Created by Sonam on 9/27/2016.
 */

public class EditTaskDialogFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText etName;
    private Button btnSave;
    Task task;
    private EditText etDueDate;
    String priority;
    private RadioGroup rgPriority;
    private RadioButton rbHigh,rbMedium,rbLow;

    public interface EditTaskDialogListener {
        void onFinishEditDialog(Task task, int position);
    }

    public EditTaskDialogFragment(){

    }

    public static EditTaskDialogFragment newInstance(String s, Task task_obj, int position){
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", s);
        args.putInt("id", task_obj.id);
        args.putString("name", task_obj.name);
        args.putString("priority", task_obj.priority);
        args.putString("due_date", task_obj.due_date);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etName = (EditText) view.findViewById(R.id.etEditName);
        etDueDate = (EditText) view.findViewById(R.id.etEditDueDate);
        etDueDate.setKeyListener(null);
        rgPriority = (RadioGroup) view.findViewById(R.id.editRadioGroup);
        rbHigh = (RadioButton) view.findViewById(R.id.rbEditHigh);
        rbMedium = (RadioButton) view.findViewById(R.id.rbEditMedium);
        rbLow = (RadioButton) view.findViewById(R.id.rbEditLow);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        String name = getArguments().getString("name");
        String due_date = getArguments().getString("due_date");
        priority = getArguments().getString("priority");
        getDialog().setTitle(title);
        etName.setText(name);
        etDueDate.setText(due_date);
        switch (priority) {
            case "High":
                rbHigh.setChecked(true);
                break;
            case "Medium":
                rbMedium.setChecked(true);
                break;
            case "Low":
                rbLow.setChecked(true);
                break;
        }
        /* Attach CheckedChangeListener to radio group */
        rgPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(null!=rb && checkedId > -1){
                    priority = rb.getText().toString();
                }

            }
        });
        // Show a datepicker when the dateButton is clicked
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance( EditTaskDialogFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        btnSave = (Button) view.findViewById(R.id.btnEditTask);

        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        etDueDate.setText(date);
    }

    @Override
    public void onClick(View v) {
        int id =  getArguments().getInt("id");
        int position = getArguments().getInt("position");
        String name = etName.getText().toString();
        String due_date = etDueDate.getText().toString();
        task = new Task(id,name,priority,due_date);
        EditTaskDialogListener activity = (EditTaskDialogListener) getActivity();
        activity.onFinishEditDialog(task,position);
        dismiss();
    }

}
