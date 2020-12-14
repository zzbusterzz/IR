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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author KillerMech
 */
public class ExcelReaderWriter {

    public static void ReadExcel(String path,  HashMap<String, List<String>> map, HashMap<String, ColumnType> attribType, List<String> keyPosition, HashMap<String, HashMap<Double, Integer>> modeCount, int rowno) {
        modeCount = new HashMap<>();
       
        
        try {
            File file = new File(path);//"C:\\demo\\employee.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            
//creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            
            
            
            
            wb.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
//            DataFormatter fmt = new DataFormatter();
//
//            for(int sn=0; sn<workbook.getNumberOfSheets(); sn++) {
//               Sheet sheet = workbook.getSheetAt(sn);
//               for (int rn=sheet.getFirstRowNum(); rn<=sheet.getLastRowNum(); rn++) {
//                  Row row = sheet.getRow(rn);
//                  if (row == null) {
//                     // There is no data in this row, handle as needed
//                  } else {
//                     // Row "rn" has data
//                     for (int cn=0; cn<row.getLastCellNum(); cn++) {
//                        Cell cell = row.getCell(cn);
//                        if (cell == null) {
//                          // This cell is empty/blank/un-used, handle as needed
//                        } else {
//                           String cellStr = fmt.formatCell(cell);
//                           // Do something with the value
//                        }
//                     }
//                  }
//               }
//            }
            
            
            
            rowno = sheet.getLastRowNum();
            
            for(int i = 0; i < rowno; i++){
                XSSFRow row = sheet.getRow(i);
                int colCount = row.getLastCellNum();
                for(int j = 0; j < colCount; j++){
                     if (row != null) {
                        XSSFCell cell = row.getCell(j, MissingCellPolicy.RETURN_NULL_AND_BLANK);
                        if (cell != null) {
                            switch (cell.getCellTypeEnum()) {
                                case BLANK:
                                    map.get(keyPosition.get(j)).add("");
                                    break;
                                case _NONE:
                                    map.get(keyPosition.get(j)).add("");
                                        break;

                                case STRING:    //field that represents string cell type  
                                    if(i == 0){
                                        String key = cell.getStringCellValue();
                                        map.put(key, new ArrayList<>());
                                        modeCount.put(key, new HashMap<>());
                                        attribType.put(key, ColumnType.UNKNOWN);

                                        keyPosition.add(key);
                                    }else{
                                        if(attribType.get(keyPosition.get(j)) != ColumnType.STRING)
                                            attribType.put(keyPosition.get(j), ColumnType.STRING);
                                        map.get(keyPosition.get(j)).add(cell.getStringCellValue());
                                    }
                                    System.out.print(cell.getStringCellValue() + "\t\t\t");
                                    break;
                                case NUMERIC:    //field that represents number cell type  
                                    double no = cell.getNumericCellValue();

                                    String key = keyPosition.get(j);

                                    HashMap<Double, Integer> interMap = modeCount.get(key);

                                    System.out.print(no + "\t\t\t");

                                    ColumnType tp = attribType.get(key);
                                    if(tp == ColumnType.UNKNOWN)
                                        attribType.put(key, ColumnType.NUMERIC);



                                    if(interMap.containsKey(no))
                                        interMap.put(no, interMap.get(no)+ 1);
                                    else
                                        interMap.put(no, 1);

                                    map.get(keyPosition.get(j)).add(no + "");
                                    break;
                                default:
                                     map.get(keyPosition.get(j)).add("");
                            }
                        }
                    }
                }
            }
            
           

//
//            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
//            while (itr.hasNext()) {
//                Row row = itr.next();
//                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
//                int i = 0;
//                while (cellIterator.hasNext()) {                    
//                    Cell cell = cellIterator.next();
//                    switch (cell.getCellTypeEnum()) {
//                        case BLANK:
//                            map.get(keyPosition.get(i)).add("");
//                            break;
//                        case _NONE:
//                            map.get(keyPosition.get(i)).add("");
//                                break;
//                                
//                        case STRING:    //field that represents string cell type  
//                            if(rowno == 0){
//                                String key = cell.getStringCellValue();
//                                map.put(key, new ArrayList<>());
//                                modeCount.put(key, new HashMap<>());
//                                attribType.put(key, ColumnType.UNKNOWN);
//                                
//                                keyPosition.add(key);
//                            }else{
//                                if(attribType.get(keyPosition.get(i)) != ColumnType.STRING)
//                                    attribType.put(keyPosition.get(i), ColumnType.STRING);
//                                map.get(keyPosition.get(i)).add(cell.getStringCellValue());
//                            }
//                            System.out.print(cell.getStringCellValue() + "\t\t\t");
//                            break;
//                        case NUMERIC:    //field that represents number cell type  
//                            double no = cell.getNumericCellValue();
//                            
//                            String key = keyPosition.get(i);
//                            
//                            HashMap<Double, Integer> interMap = modeCount.get(key);
//                            
//                            System.out.print(no + "\t\t\t");
//                            
//                            ColumnType tp = attribType.get(key);
//                            if(tp == ColumnType.UNKNOWN)
//                                attribType.put(key, ColumnType.NUMERIC);
//                            
//                           
//                            
//                            if(interMap.containsKey(no))
//                                interMap.put(no, interMap.get(no)+ 1);
//                            else
//                                interMap.put(no, 1);
//                            
//                            map.get(keyPosition.get(i)).add(no + "");
//                            break;
//                        default:
//                             map.get(keyPosition.get(i)).add("");
//                    }
//                    i++;
//                }
//                
//                rowno++;
//                System.out.println("");
 //           }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void WriteExcel(String path, HashMap<String, List<String>> map, HashMap<String, ColumnType> attribType, List<String> keyPosition, int rowCount) {
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("Sheet1");

        //Create row object
        XSSFRow row;
        
        for(int i = 0; i < rowCount; i++){
            int cellid = 0;
            row = spreadsheet.createRow(i++);
            for (String key : keyPosition) {
                Cell cell = row.createCell(cellid++);
                
                //if(attribType.get(key) == ColumnType.NUMERIC)
                  //  cell.setCellValue(  Integer.parse(map.get(key).get(i)) );
                //else    
                    cell.setCellValue(map.get(key).get(i));
            }
        }
    
    
        
        //Write the workbook in file system
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(path));
//"C:/poiexcel/Writesheet.xlsx"));
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
