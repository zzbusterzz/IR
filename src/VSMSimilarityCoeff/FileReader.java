/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VSMSimilarityCoeff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 1
 */
public class FileReader {

    public String folderPath = "C:/IR/Exc6";

    long fileCount = 0;
    public List<String> fileNames;
    
    private int d = 0;
    public void ReadFile() {

        fileNames = new ArrayList();

        File directoryPath = new File(folderPath);
        String contents[] = directoryPath.list();
        fileCount = contents.length;
        System.out.println("File Count : " + fileCount);
        
        for (int i = 0; i < contents.length; i++) {
            if(!contents[i].equals("DFIDF.txt") && !contents[i].equals("TFXIDF.txt"))
                 d++;
            fileNames.add(contents[i]);
            //   System.out.println(contents[i]);
        }
    }
    
     public int getFileCountForD() {
        return d;
    }
    
    public long getFileCount() {
        return fileCount;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

}
