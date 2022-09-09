package dataAnalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {

    private static final String folder = "experiments/";
    private static final String extension = ".csv";

    public static void create(String filename) {

        try {

            String pathname = folder + filename + extension;
            File file = new File(pathname);

            if (file.delete())
                System.out.println("A previous version of file " + filename + " was deleted");

            if (!file.createNewFile())
                throw new IOException();

        } catch (IOException e) {
            System.out.println("An error occurred during the creation of " + filename);
            e.printStackTrace();
        }
    }

    public static void saveDataToFile(String filename, List<Long> timesList, List<Long> tardinessList) {

        create(filename);

        if (timesList.size() != tardinessList.size()) {
            System.out.println("invalid input");
            return;
        }

        try {
            FileWriter writer = new FileWriter(folder + filename + extension);
            writer.write("Job_Number,Time,Tardiness\n");

            for (int i = 0; i < timesList.size(); i++) {
                writer.write(i + "," + timesList.get(i) + "," + tardinessList.get(i) + "\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Failed to write to file " + filename);
            e.printStackTrace();
        }

    }

}
