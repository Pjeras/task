package lt.jasinskas.swdb_task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Program which checks the folder and its subfolders for the files with the same content and lists them if any

public class Main2 {

    public static void main(String[] args) throws IOException {

        sameContentFiles(new File("C:\\Users\\pjera\\OneDrive\\Desktop\\test"));

    }

    public static void sameContentFiles(File folderPath) throws IOException {

        if (!folderPath.isDirectory()) {
            throw new IllegalArgumentException("The path passed as argument in the sameContentFiles() method must be directory");
        }
        //getting the list of the files in the folder
        File[] listFiles = folderPath.listFiles();
        List<Set<String>> filePathSets = new ArrayList<>();

        for (int i = 0; i < listFiles.length; i++) {
            File tempFile = getFile(listFiles[i]);

            if (tempFile == null) {
                continue;
            }
            //using set as collection to avoid duplicates later when adding            
            Set<String> set = new HashSet<>();

            for (int j = i + 1; j < listFiles.length; j++) {

                Set<String> compareResult = comparing(tempFile, listFiles[j], filePathSets);
                set.addAll(compareResult);
            }
            if (!set.isEmpty()) {
                filePathSets.add(set);
            }
        }

        //printing out the result
        if (!filePathSets.isEmpty()) {
            for (int i = 0; i < filePathSets.size(); i++) {
                System.out.println("Number " + (i + 1) + " set of files with the same content: " + filePathSets.get(i).toString());
                System.out.println("--------------------------------------------------------------------------");
            }
        } else {
            System.out.println("There is no duplicate files in: " + folderPath.getPath());
        }

    }

    public static Set<String> comparing(File file1, File file2, List<Set<String>> filePathSets) throws IOException {

        File receivedFile1 = file1;
        File receivedFile2 = null;
        File[] listFiles = null;
        Set<String> set = new HashSet<>();

        byte[] f1 = Files.readAllBytes(receivedFile1.toPath());

        if (file2.isFile()) {
            listFiles = new File[]{file2};
        } else {
            listFiles = file2.listFiles();
        }    
            for (int i = 0; i < listFiles.length; i++) {

                if (listFiles[i].isDirectory()) {
                    Set<String> compareResult = comparing(file1, listFiles[i], filePathSets);
                    set.addAll(compareResult);
                    continue;
                }
                receivedFile2 = listFiles[i];
                byte[] f2 = Files.readAllBytes(receivedFile2.toPath());

                if (Arrays.equals(f1, f2)) {
                    if (duplicateCheck(receivedFile1.toPath().toString(), listFiles[i].toPath().toString(), filePathSets) == false) {
                        set.add(receivedFile1.toPath().toString());
                        set.add(listFiles[i].toPath().toString());
                    }
                }
            }
        return set;
    }

    public static File getFile(File receivedFile) {

        File foundFile = null;
        if (receivedFile.isFile()) {
            foundFile = receivedFile;
        } else {
            File[] newListFiles = receivedFile.listFiles();

            for (int i = 0; i < newListFiles.length; i++) {
                if (newListFiles[i].isDirectory()) {
                    foundFile = getFile(newListFiles[i]);
                } else {
                    foundFile = newListFiles[i];
                }
            }
        }
        return foundFile;
    }

    //this method is used to check duplicates in cases when there are more than two duplicates, so the second duplicate would not create new set with third duplicate etc.
    public static boolean duplicateCheck(String firstPath, String secondPath, List<Set<String>> filePathSets) {

        boolean isDuplicate = false;

        for (int i = 0; i < filePathSets.size(); i++) {
            if (filePathSets.get(i).contains(firstPath) || filePathSets.get(i).contains(secondPath)) {
                isDuplicate = true;
            }
        }

        return isDuplicate;
    }
}
