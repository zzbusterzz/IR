/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestExc;

import TestExc.Runtime.ColumnType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author KillerMech
 * https://bz.apache.org/bugzilla/show_bug.cgi?id=61572#c13
 * https://stackoverflow.com/questions/50071996/an-illegal-reflective-access-operation-has-occurred-apache-poi
 * for warning related to apache poi 
 * An illegal reflective access operation has occurred
 * 
 */
public class ExcelReaderWriter {

    public static int ReadExcel(String path, HashMap<String, List<String>> map, HashMap<String, ColumnType> attribType, List<String> keyPosition, HashMap<String, HashMap<Double, Integer>> modeCount) {
        try {
            File file = new File(path);  //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  

            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  

            wb.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
            DataFormatter fmt = new DataFormatter();

            // for(int sn=0; sn<wb.getNumberOfSheets(); sn++) {
            // XSSFSheet sheet = wb.getSheetAt(sn);
            for (int rn = sheet.getFirstRowNum(); rn <= sheet.getLastRowNum(); rn++) {
                Row row = sheet.getRow(rn);
                if (row == null) {
                    // There is no data in this row, handle as needed
                } else {
                    // Row "rn" has data
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        Cell cell = row.getCell(cn);
                        if (cell == null) {
                            // This cell is empty/blank/un-used, handle as needed
                            map.get(keyPosition.get(cn)).add("");
                        } else {
                            String cellStr = fmt.formatCellValue(cell);
                            switch (cell.getCellTypeEnum()) {
                                case STRING:    //field that represents string cell type  
                                    if (rn == 0) {
                                        String key = cell.getStringCellValue();
                                        map.put(key, new ArrayList<>());
                                        modeCount.put(key, new HashMap<>());
                                        attribType.put(key, ColumnType.UNKNOWN);

                                        keyPosition.add(key);
                                    } else {
                                        if (attribType.get(keyPosition.get(cn)) != ColumnType.STRING) {
                                            attribType.put(keyPosition.get(cn), ColumnType.STRING);
                                        }
                                        map.get(keyPosition.get(cn)).add(cell.getStringCellValue());
                                    }
                                    System.out.print(cell.getStringCellValue() + "\t\t\t");
                                    break;
                                case NUMERIC:    //field that represents number cell type  
                                    double no = cell.getNumericCellValue();

                                    String key = keyPosition.get(cn);

                                    HashMap<Double, Integer> interMap = modeCount.get(key);

                                    System.out.print(no + "\t\t\t");

                                    ColumnType tp = attribType.get(key);
                                    if (tp == ColumnType.UNKNOWN) {
                                        attribType.put(key, ColumnType.NUMERIC);
                                    }

                                    if (interMap.containsKey(no)) {
                                        interMap.put(no, interMap.get(no) + 1);
                                    } else {
                                        interMap.put(no, 1);
                                    }

                                    map.get(keyPosition.get(cn)).add(cellStr);
                                    break;
                                default:
//                                     map.get(keyPosition.get(cn)).add("");
                            }
                        }
                    }
                }
            }
            return sheet.getLastRowNum();
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            //e.printStackTrace();
        }
        return 0;
    }

    public static void WriteExcel(String path, HashMap<String, List<String>> map, HashMap<String, ColumnType> attribType, List<String> keyPosition, int rowCount) {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("Sheet1");

        //Create row object
        XSSFRow row;

        for (int i = 0; i < rowCount; i++) {
            int cellid = 0;
            row = spreadsheet.createRow(i);
            for (String key : keyPosition) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue(map.get(key).get(i));
            }
        }

        //Write the workbook in file system
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(path));
            workbook.write(out);
            out.close();
            System.out.println("Writesheet.xlsx written successfully");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelReaderWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelReaderWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
