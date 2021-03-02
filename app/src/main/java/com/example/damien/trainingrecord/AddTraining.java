package com.example.damien.trainingrecord;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class AddTraining extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout layout_date;
    TextInputLayout layout_time;
    TextInputLayout layout_employeeID;
    TextInputLayout layout_employeeName;
    TextInputLayout layout_department;
    TextInputLayout layout_title;
    TextInputLayout layout_trainer;
    EditText ETDate;
    EditText ETTime;
    EditText ETEmployeeID;
    ImageView backButton;
    Button btnScan;
    Spinner mSpinner;

    private int[] departmentID;
    private String[] departmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        layout_date = findViewById(R.id.textInputDate);
        layout_time = findViewById(R.id.textInputTime);
        layout_employeeID = findViewById(R.id.textInputEmployeeID);
        layout_employeeName = findViewById(R.id.textInputEmployeeName);
        layout_department = findViewById(R.id.textInputDepartment);
        layout_title = findViewById(R.id.textInputTitle);
        layout_trainer = findViewById(R.id.textInputTrainer);
        ETDate = findViewById(R.id.etDate);
        ETTime = findViewById(R.id.etTime);
        ETEmployeeID = findViewById(R.id.etEmployeeID);
        backButton = findViewById(R.id.btnBack);
        btnScan = findViewById(R.id.btnScan);
        mSpinner = findViewById(R.id.spinnerDepartment);

        new Background(Background.FETCH_DEPARTMENT).execute();

        ETDate.setInputType(InputType.TYPE_NULL);
        ETDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ETDate.clearFocus();
                if (!hasFocus){
                    return;
                }
                showDateDialog(ETDate);
            }
        });

        ETTime.setInputType(InputType.TYPE_NULL);
        ETTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ETTime.clearFocus();
                if (!hasFocus){
                    return;
                }
                showTimeDialog(ETTime);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnScan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            ETEmployeeID.setText("" + scanContent);
        }
        else{
            ETEmployeeID.setText("");
            Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDateDialog(final EditText ETDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(calendar.getTime());

                ETDate.setText(date);
            }
        };
        new DatePickerDialog(AddTraining.this, dateSetListener, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(final EditText ETTime) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                int hour = hourOfDay % 12;
                String setAMPM = "";

                if (hour == 0) {
                    hour = 12;
                }

                if (hourOfDay > 12) {
                    setAMPM = "PM";
                } else {
                    setAMPM = "AM";
                }

                if (minute < 10) {
                    ETTime.setText(hour + ":0" + minute + " " + setAMPM);
                } else {
                    ETTime.setText(hour + ":" + minute + " " + setAMPM);
                }
            }
        };
        new TimePickerDialog(AddTraining.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private boolean validateDate() {
        String dateInput = layout_date.getEditText().getText().toString().trim();

        if (dateInput.isEmpty()){
            layout_date.setError("This field cannot be empty!");
            return false;
        } else {
            layout_date.setError(null);
            return true;
        }
    }

    private boolean validateTime() {
        String timeInput = layout_time.getEditText().getText().toString().trim();

        if (timeInput.isEmpty()){
            layout_time.setError("This field cannot be empty!");
            return false;
        } else {
            layout_time.setError(null);
            return true;
        }
    }

    private boolean validateEmployeeID() {
        String empIDInput = layout_employeeID.getEditText().getText().toString().trim();

        if (empIDInput.isEmpty()){
            layout_employeeID.setError("This field cannot be empty!");
            return false;
        } else {
            layout_employeeID.setError(null);
            return true;
        }
    }

    private boolean validateEmployeeName() {
        String empNameInput = layout_employeeName.getEditText().getText().toString().trim();

        if (empNameInput.isEmpty()){
            layout_employeeName.setError("This field cannot be empty!");
            return false;
        } else {
            layout_employeeName.setError(null);
            return true;
        }
    }

    private boolean validateTitle() {
        String titleInput = layout_title.getEditText().getText().toString().trim();

        if (titleInput.isEmpty()){
            layout_title.setError("This field cannot be empty!");
            return false;
        } else {
            layout_title.setError(null);
            return true;
        }
    }

    private boolean validateTrainer() {
        String trainerInput = layout_trainer.getEditText().getText().toString().trim();

        if (trainerInput.isEmpty()){
            layout_trainer.setError("This field cannot be empty!");
            return false;
        } else {
            layout_trainer.setError(null);
            return true;
        }
    }

    public void btnAddTraining_onClicked(View view) {
        if (!validateDate() | !validateTime() | !validateEmployeeID() | !validateEmployeeName() | !validateTitle() | !validateTrainer()) {
            return;
        } else {
            String dateInput = layout_date.getEditText().getText().toString().trim();
            String timeInput = layout_time.getEditText().getText().toString().trim();
            String empIDInput = layout_employeeID.getEditText().getText().toString().trim();
            String empNameInput = layout_employeeName.getEditText().getText().toString().trim();
            String departmentInput = mSpinner.getSelectedItem().toString().trim();
            String titleInput = layout_title.getEditText().getText().toString().trim();
            String trainerInput = layout_trainer.getEditText().getText().toString().trim();

            String[] splitTime = timeInput.split(" ");
            String[] splitTime2 = splitTime[0].split(":");
            int addTime = 0;

            if (splitTime[1].equals("PM")){
                addTime = 12 + Integer.parseInt(splitTime2[0]);
            } else {
                addTime = Integer.parseInt(splitTime2[0]);
            }

            timeInput = String.valueOf(addTime) + ":" + splitTime2[1];

            Background bg = new Background(Background.ADD_TRAINING);
            bg.execute(dateInput, timeInput, empIDInput, empNameInput, departmentInput, titleInput, trainerInput);
        }
    }

    public class Background extends AsyncTask<String, Void, ResultSet> {
        private static final String LIBRARY = "com.mysql.jdbc.Driver";
        private static final String USERNAME = "sql12387699";
        private static final String DB_NAME = "sql12387699";
        private static final String PASSWORD = "UMmjeekHxr";
        private static final String SERVER = "sql12.freemysqlhosting.net";

        public static final int FETCH_DEPARTMENT = 30;
        public static final int ADD_TRAINING = 40;

        private int method;
        private Connection conn;
        private PreparedStatement stmt;
        private ProgressDialog progressDialog;

        public Background(int method) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            this.method = method;
        }

        @Override
        protected void onPostExecute(ResultSet result) {
            super.onPostExecute(result);

            try {
                switch (this.method) {
                    case FETCH_DEPARTMENT:
                        result.last();
                        int totalRow = result.getRow();
                        result.first();
                        departmentID = new int[totalRow];
                        departmentName = new String[totalRow];
                        for (int i = 0; i < totalRow; i++) {
                            departmentID[i] = result.getInt(1);
                            departmentName[i] = result.getString(2);
                            result.next();
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, departmentName);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner.setAdapter(arrayAdapter);
                        break;

                    case ADD_TRAINING:
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                }
            }
            catch (Exception e) {
                Log.e("ERROR BACKGROUND", e.getMessage());
                Toast.makeText(AddTraining.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            finally {
//                progressDialog.hide();
                try { result.close(); } catch (Exception e) { /* ignored */ }
                closeConn();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddTraining.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Processing data");
//            progressDialog.show();
        }

        @Override
        protected ResultSet doInBackground(String... strings) {
            conn = connectDB();
            ResultSet result = null;

            if (conn == null) {
                return null;
            }
            try {
                String query = "";
                switch (this.method) {
                    case FETCH_DEPARTMENT:
                        query = "SELECT id, name FROM department";
                        stmt = conn.prepareStatement(query);
                        return stmt.executeQuery();

                    case ADD_TRAINING:
                        query = "insert into training_history (date, time, employee_id, employee_name, department, title, trainer) values (?, ?, ?, ?, ?, ?, ?)";
                        stmt = conn.prepareStatement(query);
                        stmt.setString(1, strings[0]);
                        stmt.setString(2, strings[1]);
                        stmt.setInt(3, Integer.parseInt(strings[2]));
                        stmt.setString(4, strings[3]);
                        stmt.setString(5, strings[4]);
                        stmt.setString(6, strings[5]);
                        stmt.setString(7, strings[6]);
                        stmt.executeUpdate();
                }
            }
            catch (Exception e) {
                Log.e("ERROR MySQL Statement", e.getMessage());
            }
            return result;
        }


        private Connection connectDB(){
            try {
                Class.forName(LIBRARY);
                return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DB_NAME, USERNAME, PASSWORD);
            } catch (Exception e) {
                Log.e("Error on Connection", e.getMessage());
                return null;
            }
        }

        public void closeConn() {
            try {
                stmt.close();
            } catch (Exception e) {
                /* ignored */
            }
            try {
                conn.close();
            } catch (Exception e) { /* ignored */ }
        }
    }
}
