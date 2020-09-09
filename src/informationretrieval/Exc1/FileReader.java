/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package informationretrieval.Exc1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 1
 */
public class FileReader {

    public String folderPath = "D:/IR";

    long fileCount = 0;
    public List<String> fileNames;

    public void ReadFile() {

        fileNames = new ArrayList();

        File directoryPath = new File(folderPath);
        String contents[] = directoryPath.list();
        fileCount = contents.length;
        System.out.println("File Count : " + fileCount);
        for (int i = 0; i < contents.length; i++) {
            fileNames.add(contents[i]);
            //   System.out.println(contents[i]);
        }
    }

    public long getFileCount() {
        return fileCount;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

}
