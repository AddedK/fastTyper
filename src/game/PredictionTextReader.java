/**
 * The purpose of this class is to read textFiles and generate prediction strings to the Game class
 */

package game;

import java.io.*;

public class PredictionTextReader {
    private String directoryOfPredictionFiles; // The directory where it looks for text files
    private String[] listOfPredictionFileNames;
    private int currentPredictionFileIndex;
    private boolean hasMoreFiles;

    public PredictionTextReader(String directoryOfPredictionFiles) {
        this.directoryOfPredictionFiles = directoryOfPredictionFiles;
        this.currentPredictionFileIndex = 0;
        this.listOfPredictionFileNames = getFilesFromDirectory();
        this.hasMoreFiles = true;

    }

    public String textFileToPrediction() {
        String fileName = this.listOfPredictionFileNames[this.currentPredictionFileIndex];
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                sb.append(line);
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length()-1); // Delete last unnecessary newline.
            String fileContent = sb.toString();
            return fileContent;
        }
        catch (Exception e) {
            return "";
        }
    }

    public void incrementPredictionFileIndex() {
        if (this.currentPredictionFileIndex < this.listOfPredictionFileNames.length-1) {
            this.currentPredictionFileIndex++;
        } else {
            this.hasMoreFiles = false;
        }

    }


    public String[] getFilesFromDirectory() {
        File folder = new File(this.directoryOfPredictionFiles);
        File[] listOfFiles = folder.listFiles();
        String[] listOfPredictionFileNames = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfPredictionFileNames[i] = this.directoryOfPredictionFiles + "/" + listOfFiles[i].getName();
            }
        }
        return listOfPredictionFileNames;
    }

    /**
     * Getter and setters
     */
    public int getCurrentPredictionFileIndex() {
        return currentPredictionFileIndex;
    }

    public void setCurrentPredictionFileIndex(int currentPredictionFileIndex) {
        this.currentPredictionFileIndex = currentPredictionFileIndex;
    }

    public boolean hasMoreFiles(){
        return this.hasMoreFiles;
    }

    public static void main(String[] args) throws FileNotFoundException {
        PredictionTextReader ptr = new PredictionTextReader("src/game/predictionFiles1");

        for (int i = 0; i < ptr.listOfPredictionFileNames.length ; i++) {
            System.out.println(ptr.listOfPredictionFileNames[i]);
        }
        System.out.println(ptr.textFileToPrediction());
        System.out.println(ptr.textFileToPrediction());

    }
}
