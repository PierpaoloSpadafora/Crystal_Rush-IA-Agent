package it.unical.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLogger {


    public static void logItemCooldowns(String filePath, String mineID, String radarID) {
        // Verifica se il file esiste, altrimenti lo crea
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write("MINE ID: " + mineID);
                writer.newLine();
                writer.write("RADAR ID: " + radarID);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura nel file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

    }
}
