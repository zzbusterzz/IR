/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProbabalisticRetrieval;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 1
 */
public class Runtime {

    static VSMData vsm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        vsm = new VSMData();
        vsm.CreateIndex();

        List<String> filenamesref = vsm.getFileNamesref();

        Scanner sc = new Scanner(System.in);

        boolean run = true;

        while (run) {
            System.out.println("Enter Query(Type Exit to escape)");
            if (sc.hasNextLine()) {
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("Exit")) {
                    run = false;
                } else {
                    //check for brackets
                    input = input.toLowerCase();
                    String[] keywords = input.split(" ");

                    List<Integer> id = new ArrayList();
                    System.out.println("Enter Relevent Doc No ie 0,1,5. Document no not present will be removed");
                    if (sc.hasNextLine()) {
                        input = sc.nextLine();
                        String[] docID = input.split(",");

                        for (int i = 0; i < docID.length; i++) {
                            int index = Integer.parseInt(docID[i]);
                            if (filenamesref.size() > index) {
                                id.add(index);
                            }
                        }

                        Dictionary<String, Integer> query = new Hashtable();

                        for (int i = 0; i < keywords.length; i++) {
                            if (query.get(keywords[i]) == null) {
                                query.put(keywords[i], 1);
                            } else {
                                query.put(keywords[i], query.get(keywords[i]) + 1);
                            }
                        }

                        Integer[] arr = new Integer[id.size()];
                        arr = id.toArray(arr);

                        String fileNames = vsm.CalcDocSimilarity(query, arr);

                        if (fileNames == "") {
                            System.out.println("No values present");
                        } else {
                            System.out.println(fileNames);
                        }

                    }

                }
            }
        }
    }
}
