import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by logvinov on 18.02.2015.
 */
public class CreateExcel {
    public void createExcelDoc(String inputText) throws IOException {
        Workbook wb = new HSSFWorkbook();
        ParsingHtml parsingHtml = new ParsingHtml(inputText);
        Sheet sheet = wb.createSheet(parsingHtml.getTitle());
        int numberOfRows = 0;
        int numberOfCells = 0;
        // Create a row and put some cells in it. Rows are 0 based.

        Row row = sheet.createRow((short)numberOfRows);
        // Create a cell and put a value in it.
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                parsingHtml.getNumberOfTDForMergeSells() - 1  //last column  (0-based)
        ));
        Cell cell = row.createCell(numberOfCells);
        cell.setCellValue(parsingHtml.getHeader());
        Font f = wb.createFont();
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(f);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

//        headerStyle.setFillBackgroundColor(HSSFColor.AQUA.index);
//        headerStyle.setFillForegroundColor(HSSFColor.ORANGE.index);

//        headerStyle.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
        cell.setCellStyle(headerStyle);
//
//        cs.setFont(f);
        numberOfCells++;
        numberOfRows++;
        System.out.println("Cells: " + numberOfCells + " Rows :" + numberOfRows);
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        CellStyle cs2 = wb.createCellStyle();
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        cs2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        cs2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        cs2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        cs2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        cs2.setWrapText(true);

        CellStyle rowStyle = wb.createCellStyle();
        rowStyle.setAlignment(CellStyle.ALIGN_CENTER);
        rowStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        rowStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        rowStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        rowStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        rowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        rowStyle.setFillPattern(headerStyle.SOLID_FOREGROUND);
        rowStyle.setWrapText(true);
//        CellStyle cs3 = wb.createCellStyle();
//        cs3.setFillForegroundColor(HSSFColor.BLUE.index);
        Document doc = Jsoup.parse(inputText);
        Elements tables = doc.getElementsByTag("table");
        int numberOfTable = 1;
        for (Element table : tables) {
            Elements trs = table.getElementsByTag("tr");
            for (Element tr : trs) {
                Row rowInTable = sheet.createRow(numberOfRows);
                System.out.println("Cells: " + numberOfCells + " Rows :" + numberOfRows);
                System.out.println("TR: " + tr.text() + "Attr: " + tr.attr("style"));
                numberOfCells = 0;
                int i =0;
                for (Element tds : tr.getElementsByTag("td")) {
                    System.out.println("TDDDDDDDDDD" + tds.text());
                        if (numberOfTable == 1) {
                            Cell cellInTable = rowInTable.createCell(numberOfCells);
                            cellInTable.setCellValue(tds.text());
                            cellInTable.setCellStyle(cs);

//                            cellInTable.setCellStyle(cs3);
                            System.out.println("TD1: " + tds.text());
                            numberOfCells++;
                        } else {
                            Cell cellInTable = rowInTable.createCell(numberOfCells);
                            cellInTable.setCellValue(tds.text());
                            if (tr.attr("style").length() != 0 ) {
                                cellInTable.setCellStyle(rowStyle);
                            } else {
                                cellInTable.setCellStyle(cs2);
                            }
                            if (sheet.getRow(1).getCell(i).getStringCellValue().length() > tds.text().length()) {
                                sheet.setColumnWidth(numberOfCells, sheet.getRow(1).getCell(numberOfCells).getStringCellValue().length() * 256);
                            }
                            System.out.println("TD2: " + tds.text());
                            numberOfCells++;
                        }
                }
                numberOfTable++;
                numberOfRows++;
            }
        }
        HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
        for (int i = 2; i < sheet.getLastRowNum(); i++)
        {
            for (int k = 0; k < sheet.getRow(i).getLastCellNum(); k++) {
                if (sheet.getRow(i).getCell(k).getStringCellValue().length() > sheet.getRow(1).getCell(k).getStringCellValue().length()) {
                    sheet.setColumnWidth(k, sheet.getRow(1).getCell(k).getStringCellValue().length() * 256 * 3);
                }
            }
        }
//        for (int i = 0; i < parsingHtml.getNumberOfTDForMergeSells(); i++) {
////            sheet.autoSizeColumn((short)i);
//
//        }


//        CellUtil.setAlignment(cell, wb, CellStyle.ALIGN_CENTER);

//        CellStyle cs = wb.createCellStyle();
//        Font f = wb.createFont();
////        cell.setCellStyle(Font.BOLDWEIGHT_BOLD);
//        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
//
//        cs.setFont(f);
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//        cs.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
//        cs.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
//        cs.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
//        cs.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
////        sheet.setDefaultColumnStyle(0, cs); //set bold for column 1
////        row.getCell(0).setCellStyle(cs);
//        cell.setCellStyle(cs);
//
//
//
//        // Or do it on one line.
//        row.createCell(1).setCellValue(1.2);
//        row.createCell(2).setCellValue(
//                createHelper.createRichTextString("This is a string"));
//        row.createCell(3).setCellValue(true);
//        Row row2 = sheet.createRow((short)1);
//        Cell cell2 = row2.createCell(0);
//        cell2.setCellValue(1);
//
//        // Or do it on one line.
//        row2.createCell(1).setCellValue("21/11/14");
//        row2.createCell(2).setCellValue(
//                createHelper.createRichTextString("This is a string"));
//        row2.createCell(3).setCellValue(true);
//
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(parsingHtml.getTitle() + ".xls");
        wb.write(fileOut);
        fileOut.close();
    }
}
