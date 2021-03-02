package com.example.damien.trainingrecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ViewTrainingDetails extends AppCompatActivity {
    TextView TVTrainingDetailsID;
    TextView TVTrainingDetailsDate;
    TextView TVTrainingDetailsTime;
    TextView TVTrainingDetailsEmployeeID;
    TextView TVTrainingDetailsEmployeeName;
    TextView TVTrainingDetailsDepartment;
    TextView TVTrainingDetailsTitle;
    TextView TVTrainingDetailsTrainer;
    ImageView backButton;
    Button btnExportFile;

    private String trainingDetailsID;
    private String trainingDetailsDate;
    private String trainingDetailsTime;
    private String trainingDetailsEmpID;
    private String trainingDetailsEmpName;
    private String trainingDetailsDepartment;
    private String trainingDetailsTitle;
    private String trainingDetailsTrainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_training_details);

        TVTrainingDetailsID = findViewById(R.id.tvTrainingDetailsID);
        TVTrainingDetailsDate = findViewById(R.id.tvTrainingDetailsDate);
        TVTrainingDetailsTime = findViewById(R.id.tvTrainingDetailsTime);
        TVTrainingDetailsEmployeeID = findViewById(R.id.tvTrainingDetailsEmpID);
        TVTrainingDetailsEmployeeName = findViewById(R.id.tvTrainingDetailsEmpName);
        TVTrainingDetailsDepartment = findViewById(R.id.tvTrainingDetailsDepartment);
        TVTrainingDetailsTitle = findViewById(R.id.tvTrainingDetailsTitle);
        TVTrainingDetailsTrainer = findViewById(R.id.tvTrainingDetailsTrainer);
        backButton = findViewById(R.id.btnBack);
        btnExportFile = findViewById(R.id.btnExportFile);

        Intent i = getIntent();
        trainingDetailsID = i.getStringExtra("trainingID");
        trainingDetailsDate = i.getStringExtra("trainingDate");
        trainingDetailsTime = i.getStringExtra("trainingTime");
        trainingDetailsEmpID = i.getStringExtra("trainingEmployeeID");
        trainingDetailsEmpName = i.getStringExtra("trainingEmployeeName");
        trainingDetailsDepartment = i.getStringExtra("trainingDepartment");
        trainingDetailsTitle = i.getStringExtra("trainingTitle");
        trainingDetailsTrainer = i.getStringExtra("trainingTrainer");

        TVTrainingDetailsID.setText(trainingDetailsID);
        TVTrainingDetailsDate.setText(trainingDetailsDate);
        TVTrainingDetailsTime.setText(trainingDetailsTime);
        TVTrainingDetailsEmployeeID.setText(trainingDetailsEmpID);
        TVTrainingDetailsEmployeeName.setText(trainingDetailsEmpName);
        TVTrainingDetailsDepartment.setText(trainingDetailsDepartment);
        TVTrainingDetailsTitle.setText(trainingDetailsTitle);
        TVTrainingDetailsTrainer.setText(trainingDetailsTrainer);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnExportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportFile();
            }
        });
    }

    public static String MD5(String password) {
        byte[] bytes = password.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {}
        byte[] hashed_password = md.digest(bytes);
        StringBuilder sb = new StringBuilder();

        for (byte b: hashed_password) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private void exportFile() {
        Random random = new Random();
        int randomNum = random.nextInt(90000) + 10000;
        String hashFileName = MD5(String.valueOf(randomNum));

        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();
        CellStyle cellStyle2 = wb.createCellStyle();

        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle.setTopBorderColor(HSSFColor.ORANGE.index);
        cellStyle.setBottomBorderColor(HSSFColor.ORANGE.index);
        cellStyle.setLeftBorderColor(HSSFColor.ORANGE.index);
        cellStyle.setRightBorderColor(HSSFColor.ORANGE.index);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        cellStyle2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        cellStyle2.setTopBorderColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle2.setBottomBorderColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle2.setLeftBorderColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle2.setRightBorderColor(HSSFColor.LIGHT_ORANGE.index);
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // create sheet
        Sheet sheet = null;
        sheet = wb.createSheet("Training Record");

        // column and row
        Row row = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        Row row3 = sheet.createRow(2);
        Row row4 = sheet.createRow(3);
        Row row5 = sheet.createRow(4);
        Row row6 = sheet.createRow(5);
        Row row7 = sheet.createRow(6);
        Row row8 = sheet.createRow(7);

        cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue(trainingDetailsID);
        cell.setCellStyle(cellStyle2);

        cell = row2.createCell(0);
        cell.setCellValue("Date");
        cell.setCellStyle(cellStyle);

        cell = row2.createCell(1);
        cell.setCellValue(trainingDetailsDate);
        cell.setCellStyle(cellStyle2);

        cell = row3.createCell(0);
        cell.setCellValue("Time");
        cell.setCellStyle(cellStyle);

        cell = row3.createCell(1);
        cell.setCellValue(trainingDetailsTime);
        cell.setCellStyle(cellStyle2);

        cell = row4.createCell(0);
        cell.setCellValue("Employee ID");
        cell.setCellStyle(cellStyle);

        cell = row4.createCell(1);
        cell.setCellValue(trainingDetailsEmpID);
        cell.setCellStyle(cellStyle2);

        cell = row5.createCell(0);
        cell.setCellValue("Employee Name");
        cell.setCellStyle(cellStyle);

        cell = row5.createCell(1);
        cell.setCellValue(trainingDetailsEmpName);
        cell.setCellStyle(cellStyle2);

        cell = row6.createCell(0);
        cell.setCellValue("Department");
        cell.setCellStyle(cellStyle);

        cell = row6.createCell(1);
        cell.setCellValue(trainingDetailsDepartment);
        cell.setCellStyle(cellStyle2);

        cell = row7.createCell(0);
        cell.setCellValue("Title");
        cell.setCellStyle(cellStyle);

        cell = row7.createCell(1);
        cell.setCellValue(trainingDetailsTitle);
        cell.setCellStyle(cellStyle2);

        cell = row8.createCell(0);
        cell.setCellValue("Trainer");
        cell.setCellStyle(cellStyle);

        cell = row8.createCell(1);
        cell.setCellValue(trainingDetailsTrainer);
        cell.setCellStyle(cellStyle2);

        sheet.setColumnWidth(0, (10*400));
        sheet.setColumnWidth(1, (10*400));

        File file = new File(getExternalFilesDir(null), hashFileName + ".xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getApplicationContext(), "Export file successuflly!", Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Export file failed!", Toast.LENGTH_SHORT).show();

            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
