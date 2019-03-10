package com.beehive.riki.report;

import com.pancabudi.technic.serviceRequestOrder.SROReportData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public File pihSroReport(String fileName, HashMap<String, List<SROReportData>> reportData) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        String completeName = "SRO-REPORT_"+fileName;
        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = this.cellStyle(workbook, HorizontalAlignment.CENTER, true);
        CellStyle cellStyle = this.cellStyle(workbook, HorizontalAlignment.LEFT, false);

        reportData.forEach(
                (key, val)->{
                    XSSFSheet sheet = workbook.createSheet(key);

                    int rowNum = 0;
                    String[] headers = {"NO","TGL LAPOR","JAM LAPOR","NAMA PELAPOR","NAMA MESIN","KELUHAN","HASIL", null, "NAMA TEKNISI","TGL SELESAI", "JAM SELESAI","SPAREPART","KETERANGAN"};

                    Row header = sheet.createRow(rowNum++);

                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = header.createCell(i);
                        cell.setCellValue(headers[i]);

                        if(headers[i] != null){
                            cell.setCellStyle(headerStyle);
                        }
                    }

                    for (SROReportData sro: val){
                        Row row = sheet.createRow(rowNum++);

                        int colNum = 0;

                        this.createCell(row,colNum++,rowNum - 1, cellStyle);
                        this.createCell(row,colNum++,dateFormatter.format(sro.getCreatedDate()), cellStyle);
                        this.createCell(row,colNum++,timeFormatter.format(sro.getCreatedDate()), cellStyle);
                        this.createCell(row,colNum++,sro.getRequester(), cellStyle);
                        this.createCell(row,colNum++,sro.getMachine(), cellStyle);
                        this.createCell(row,colNum++,sro.getComplaint(), cellStyle);
                        this.createCell(row,colNum++,sro.getResult(), cellStyle);

                        colNum++;

                        this.createCell(row,colNum++,sro.getHandler(),cellStyle);
                        this.createCell(row,colNum++,sro.getDoneTime()==null?"-":dateFormatter.format(sro.getDoneTime()),cellStyle);
                        this.createCell(row,colNum++,sro.getDoneTime()==null?"-":timeFormatter.format(sro.getDoneTime()),cellStyle);
                        this.createCell(row,colNum++,sro.getPart(),cellStyle);
                        this.createCell(row,colNum,sro.getHandling(),cellStyle);
                    }

                    for (int i = 0; i < headers.length; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
        );

        try{
            FileOutputStream output;
            File file = new File(completeName);
            output = new FileOutputStream(file);

            workbook.write(output);
            workbook.close();

            output.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String formatSecond(long millis){
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    @Override
    public File ceperSroReport(String fileName, HashMap<String, List<SROReportData>> reportData) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat fullDateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String completeName = "SRO-REPORT_"+fileName;
        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = this.cellStyle(workbook, HorizontalAlignment.CENTER, true);
        CellStyle cellStyle = this.cellStyle(workbook, HorizontalAlignment.LEFT, false);

        reportData.forEach(
                (key, val)->{
                    XSSFSheet sheet = workbook.createSheet(key);

                    int rowNum = 0;
                    String[] headers = {"NO","TGL LAPOR","JAM LAPOR","NAMA PELAPOR","NAMA MESIN","KELUHAN","HASIL", "WAKTU PERBAIKAN", null, "WAKTU TUNGGU", "RESPON PERTAMA","NAMA TEKNISI","TGL SELESAI", "JAM SELESAI","SPAREPART","KETERANGAN"};

                    Row header = sheet.createRow(rowNum++);
                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = header.createCell(i);
                        cell.setCellValue(headers[i]);

                        if(headers[i] != null){
                            cell.setCellStyle(headerStyle);
                        }
                    }

                    for (SROReportData sro: val){
                        Row row = sheet.createRow(rowNum++);

                        int colNum = 0;

                        this.createCell(row,colNum++,rowNum - 1, cellStyle);
                        this.createCell(row,colNum++,dateFormatter.format(sro.getCreatedDate()), cellStyle);
                        this.createCell(row,colNum++,timeFormatter.format(sro.getCreatedDate()), cellStyle);
                        this.createCell(row,colNum++,sro.getRequester(), cellStyle);
                        this.createCell(row,colNum++,sro.getMachine(), cellStyle);
                        this.createCell(row,colNum++,sro.getComplaint(), cellStyle);
                        this.createCell(row,colNum++,sro.getResult(), cellStyle);

                        long solvingTime = 0;
                        long waitTime = 0;

                        if(sro.getDoneTime() != null && sro.getFirstResponseDate() != null)
                            solvingTime = sro.getDoneTime().getTime() - sro.getFirstResponseDate().getTime();

                        if(sro.getFirstResponseDate() != null)
                            waitTime = sro.getFirstResponseDate().getTime() - sro.getCreatedDate().getTime();

                        if(solvingTime > 0){
                            this.createCell(row,colNum++,formatSecond(solvingTime),cellStyle);
                        }else{
                            this.createCell(row,colNum++,"-",cellStyle);
                        }

                        colNum++;

                        if(waitTime > 0){
                            this.createCell(row,colNum++,formatSecond(waitTime),cellStyle);
                        }else{
                            this.createCell(row,colNum++,"-",cellStyle);
                        }

                        this.createCell(row,colNum++,sro.getFirstResponseDate()==null?"-":fullDateFormatter.format(sro.getFirstResponseDate()),cellStyle);
                        this.createCell(row,colNum++,sro.getHandler(),cellStyle);
                        this.createCell(row,colNum++,sro.getDoneTime()==null?"-":dateFormatter.format(sro.getDoneTime()),cellStyle);
                        this.createCell(row,colNum++,sro.getDoneTime()==null?"-":timeFormatter.format(sro.getDoneTime()),cellStyle);
                        this.createCell(row,colNum++,sro.getPart(),cellStyle);
                        this.createCell(row,colNum,sro.getHandling(),cellStyle);

                        //baru

                    }

                    for (int i = 0; i < headers.length; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
        );

        try{
            FileOutputStream output;
            File file = new File(completeName);
            output = new FileOutputStream(file);

            workbook.write(output);
            workbook.close();

            output.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createCell(Row row, int i, Object obj, CellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellStyle(style);

        if(obj instanceof Integer)
            cell.setCellValue((Integer)obj);

        if(obj instanceof String)
            cell.setCellValue((String)obj);
    }

    private CellStyle cellStyle(XSSFWorkbook workbook, HorizontalAlignment horizontalAlignment, boolean isBold) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setBold(isBold);

        cellStyle.setFont(font);
        return cellStyle;
    }
}
