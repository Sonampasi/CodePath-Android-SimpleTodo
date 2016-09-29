package com.codepath.simpletodo;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText etName;
    EditText etDueDate;
    RadioGroup radioGroup;
    RadioButton rbHigh;
    String priority,name,due_date;
    TextInputLayout inputLayoutName,inputLayoutDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDueDate = (TextInputLayout) findViewById(R.id.input_layout_due_date);
        etName = (EditText) findViewById(R.id.etName);
        etDueDate = (EditText) findViewById(R.id.etDueDate);
        etDueDate.setKeyListener(null);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbHigh  = (RadioButton) findViewById(R.id.rbHigh);
        priority = rbHigh.getText().toString();
        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateTaskActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMinDate(now);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }

        });

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

    public void onAddTask(View view) {

        submitForm();

        if (!validateName()) {
            return;
        }
        else if (!validateDueDate()) {
            return;
        }
        else{
            name = etName.getText().toString();
            due_date = etDueDate.getText().toString();
            // Prepare data intent
            Intent data = new Intent();
            // Pass relevant data back as a result
            data.putExtra("name", name); // ints work too
            data.putExtra("priority", priority);
            data.putExtra("due_date", due_date);
            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish();
            return;
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

    }

    private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_task_name));
            etName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateDueDate() {
        if (etDueDate.getText().toString().trim().isEmpty()) {
            inputLayoutDueDate.setError(getString(R.string.err_due_date));
            etDueDate.requestFocus();
            return false;
        } else {
            inputLayoutDueDate.setErrorEnabled(false);
        }
        return true;
    }

}
