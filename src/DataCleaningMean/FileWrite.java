/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataCleaningMean;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author 1
 */
public class FileWrite {
  public static void Write(String fileName, String data){
    try {
      FileWriter myWriter = new FileWriter(fileName);
      myWriter.write(data);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}