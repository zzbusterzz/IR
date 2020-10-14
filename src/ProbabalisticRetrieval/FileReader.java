/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProbabalisticRetrieval;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 1
 */
public class FileReader {

    public String folderPath = "D:/IR/Exc8_";

    long fileCount = 0;
    public List<String> fileNames;

    //No of documents
    private int N = 0;

    public void ReadFile() {

        fileNames = new ArrayList();

        File directoryPath = new File(folderPath);
        String contents[] = directoryPath.list();
        fileCount = contents.length;
        System.out.println("File Count : " + fileCount);

        for (int i = 0; i < contents.length; i++) {
            if (!contents[i].equals("TermWeight.txt") && !contents[i].equals("DocWeight.txt")) {
                N++;
            }
            fileNames.add(contents[i]);
        }
    }

    public int getNDocs() {
        return N;
    }

    public long getFileCount() {
        return fileCount;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

}
