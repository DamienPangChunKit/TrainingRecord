package com.example.damien.trainingrecord;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class ViewTraining extends AppCompatActivity {
    TextInputLayout layout_date;
    TextInputLayout layout_time;
    TextInputLayout layout_employeeID;
    TextInputLayout layout_employeeName;
    TextInputLayout layout_title;
    TextInputLayout layout_trainer;
    EditText etEmployeeID;
    EditText etEmployeeName;
    EditText etTitle;
    EditText etTrainer;
    Spinner mSpinner;
    ImageView backButton;

    RecyclerView viewTrainingRV;
    viewTrainingAdapter mAdapter;

    public static final int REQUEST_CODE3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_training);

        layout_date = findViewById(R.id.textInputDate);
        layout_time = findViewById(R.id.textInputTime);
        layout_employeeID = findViewById(R.id.textInputEmployeeID);
        layout_employeeName = findViewById(R.id.textInputEmployeeName);
        layout_title = findViewById(R.id.textInputTitle);
        layout_trainer = findViewById(R.id.textInputTrainer);
        etEmployeeID = findViewById(R.id.etEmployeeID);
        etEmployeeName = findViewById(R.id.etEmployeeName);
        etTitle = findViewById(R.id.etTitle);
        etTrainer = findViewById(R.id.etTrainer);
        mSpinner = findViewById(R.id.spinnerDepartment);
        backButton = findViewById(R.id.btnBack);

        Background bg = new Background(Background.FETCH_TRAINING_HISTORY);
        viewTrainingRV = (RecyclerView) findViewById(R.id.trainingRecycler);
        mAdapter = new viewTrainingAdapter(bg);
        viewTrainingRV.setAdapter(mAdapter);
        viewTrainingRV.setLayoutManager(new LinearLayoutManager(ViewTraining.this));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class Background extends AsyncTask<String, Void, ResultSet> {
        private String LIBRARY = getString(R.string.db_library);
        private String USERNAME = getString(R.string.db_username);
        private String DB_NAME = getString(R.string.db_name);
        private String PASSWORD = getString(R.string.db_password);
        private String SERVER = getString(R.string.db_server);

        private Connection conn;
        private PreparedStatement stmt;
        private int method;

        public static final int FETCH_TRAINING_HISTORY = 1;

        public Background(int method) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            this.method = method;
        }

        @Override
        protected ResultSet doInBackground(String... strings) {
            conn = connectDB();
            ResultSet result = null;

            if (conn == null) {
                return null;
            }
            try {
                String query;
                switch(method){
                    case FETCH_TRAINING_HISTORY:
                        query = "SELECT id, date, time, employee_id, employee_name, department, title, trainer FROM training_history";
                        stmt = conn.prepareStatement(query);
                        result = stmt.executeQuery();
                        return result;
                }
            }
            catch (Exception e) {
                Log.e("ERROR MySQL Statement", e.getMessage());
            }
            return null;
        }

        private Connection connectDB() {
            try {
                Class.forName(LIBRARY);
                return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DB_NAME, USERNAME, PASSWORD);
            }
            catch (Exception e) {
                Log.e("Error on Connection", e.getMessage());
                return null;
            }
        }

        public void closeConn () {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    private class viewTrainingAdapter extends RecyclerView.Adapter<viewTrainingAdapter.viewTrainingHolder>{
        private LayoutInflater mInflater;
        private int itemCount;
        private Background bg;
        private ResultSet result;


        public viewTrainingAdapter(Background bg){
            this.bg = bg;
            updateResultSet();
            mInflater = LayoutInflater.from(ViewTraining.this);
        }

        class viewTrainingHolder extends RecyclerView.ViewHolder{
            TextView tvViewTrainingID;
            TextView tvViewTrainingTrainer;
            TextView tvViewTrainingTitle;
            TableLayout mTableLayout;

            final viewTrainingAdapter mAdapter;

            public viewTrainingHolder(@NonNull View itemView, viewTrainingAdapter adapter){
                super(itemView);
                tvViewTrainingID = (TextView) itemView.findViewById(R.id.tvViewTrainingID);
                tvViewTrainingTrainer = (TextView) itemView.findViewById(R.id.tvViewTrainingTrainer);
                tvViewTrainingTitle = (TextView) itemView.findViewById(R.id.tvViewTrainingTitle);
                mTableLayout = (TableLayout) itemView.findViewById(R.id.device_details_layout_table);

                this.mAdapter = adapter;
            }
        }

        @NonNull
        @Override
        public viewTrainingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View mItemView = mInflater.inflate(R.layout.view_training_row, viewGroup, false);
            return new viewTrainingHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull viewTrainingHolder viewTrainingHolder, int position) {
            try {
                result.first();
                for (int i = 0; i < position; i++) {
                    result.next();
                }

//                if (result.next()){
//                    mTableLayout.setVisibility(View.INVISIBLE);
//                }

                final String trainingID = result.getString(1);
                final String trainingDate = result.getString(2);
                final String trainingTime = result.getString(3);
                final String trainingEmployeeID = result.getString(4);
                final String trainingEmployeeName = result.getString(5);
                final String trainingDepartment = result.getString(6);
                final String trainingTitle = result.getString(7);
                final String trainingTrainer = result.getString(8);

                viewTrainingHolder.tvViewTrainingID.setText(trainingID);
                viewTrainingHolder.tvViewTrainingTrainer.setText(trainingTrainer);
                viewTrainingHolder.tvViewTrainingTitle.setText(trainingTitle);

                viewTrainingHolder.mTableLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ViewTraining.this, ViewTrainingDetails.class);
                        i.putExtra("trainingID", trainingID);
                        i.putExtra("trainingDate", trainingDate);
                        i.putExtra("trainingTime", trainingTime);
                        i.putExtra("trainingEmployeeID", trainingEmployeeID);
                        i.putExtra("trainingEmployeeName", trainingEmployeeName);
                        i.putExtra("trainingDepartment", trainingDepartment);
                        i.putExtra("trainingTitle", trainingTitle);
                        i.putExtra("trainingTrainer", trainingTrainer);
                        startActivityForResult(i, REQUEST_CODE3);
                    }
                });
            }
            catch (SQLException e) {
                Log.d("ERROR BIND VIEW", e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        private int getResultCount() {
            try {
                result.next();
                result.last();
                int count = result.getRow();
                result.first();
                return count;
            } catch (SQLException e) {

            }
            return 0;
        }

        public void updateResultSet() {
            try {
                bg.closeConn();
                bg = new Background(Background.FETCH_TRAINING_HISTORY);
                this.result = this.bg.execute().get();
                itemCount = getResultCount();
            } catch (ExecutionException e) {
                Log.e("ERROR EXECUTION", e.getMessage());
            } catch (InterruptedException e) {
                Log.e("ERROR INTERRUPTED", e.getMessage());
            }
        }
    }
}
