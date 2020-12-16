/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CAIIExc1;

import CAIIExc1.Runtime.ColumnType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
 * @author KillerMech https://bz.apache.org/bugzilla/show_bug.cgi?id=61572#c13
 * https://stackoverflow.com/questions/50071996/an-illegal-reflective-access-operation-has-occurred-apache-poi
 * for warning related to apache poi An illegal reflective access operation has
 * occurred
 *
 */
public class ExcelReaderWriter {

    public static int ReadExcel(String path, HashMap<String, List<String>> map, HashMap<String, ColumnType> attribType, List<String> keyPosition, HashMap<String, Integer> mideanCount) {
        HashMap<String, List<Integer>> localmideanCount = new HashMap<>();
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
                                        localmideanCount.put(key, new ArrayList<>());
                                        mideanCount.put(key, 0);//Initialise midean array to 0
                                        attribType.put(key, ColumnType.UNKNOWN);

                                        keyPosition.add(key);
                                    } else {
                                        String key = keyPosition.get(cn);
                                        String val = cell.getStringCellValue();
                                        if (attribType.get(key) != ColumnType.STRING) {
                                            attribType.put(key, ColumnType.STRING);
                                        }
                                        map.get(key).add(val);

//                                        if (key.equals("empName")) {
//                                            if (nameCount.containsKey(val)) {
//                                                nameCount.put(val, nameCount.get(val) + 1);//Increment name counter if found
//                                            } else {
//                                                nameCount.put(val, 1);//First entry then add 1
//                                            }
//                                        }
                                    }
                                    System.out.print(cell.getStringCellValue() + "\t\t\t");
                                    break;
                                case NUMERIC:    //field that represents number cell type  
                                    double no = cell.getNumericCellValue();

                                    String key = keyPosition.get(cn);

                                    List<Integer> mideanArr = localmideanCount.get(key);

                                    System.out.print(no + "\t\t\t");

                                    ColumnType tp = attribType.get(key);
                                    if (tp == ColumnType.UNKNOWN) {
                                        attribType.put(key, ColumnType.NUMERIC);
                                    }

                                    mideanArr.add(Integer.parseInt(cellStr));

                                    map.get(keyPosition.get(cn)).add(cellStr);
                                    break;
                                default:
//                                     map.get(keyPosition.get(cn)).add("");
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < keyPosition.size(); i++) {
                String key = keyPosition.get(i);
                if (attribType.get(key) == ColumnType.NUMERIC) {
                    List<Integer> mideanArr = localmideanCount.get(key);
                    Collections.sort(mideanArr);//Sort to small to large

                    int size = mideanArr.size();
                    if (size % 2 == 1) {//Odd no
                        double indexPos = Math.round(size / 2);
                        mideanCount.put(key, mideanArr.get((int) indexPos));
                    } else {//even no
                        int midIndex = size / 2;
                        //Get one index less than total so as to get two middlle values
                        int midVal1 = mideanArr.get(midIndex);
                        int midVal2 = mideanArr.get(midIndex - 1);

                        mideanCount.put(key, (midVal1 + midVal2) / 2);
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
            if (i == 0) {
                for (String key : keyPosition) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue(key);
                }
            } else {
                for (String key : keyPosition) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue(map.get(key).get(i - 1));
                }
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
