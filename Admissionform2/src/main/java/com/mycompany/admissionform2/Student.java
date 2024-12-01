package com.mycompany.admissionform2;

import java.io.*;
import java.util.ArrayList;

public class Student {

    private static final String FILE_NAME = "admission_data.ser";

    // Save data to file
    public static void saveData(String[] rowData) {
        ArrayList<String[]> data = loadData(); // Load existing data
        data.add(rowData); // Add new row
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load data from file
    public static ArrayList<String[]> loadData() {
        ArrayList<String[]> data = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            data = (ArrayList<String[]>) in.readObject();
        } catch (FileNotFoundException e) {
            // File not found is acceptable; it might not exist yet
            System.err.println("Data file not found. A new one will be created.");
        } catch (IOException e) {
            System.err.println("Error reading data file: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Data file contains invalid format: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    // Delete data from file (based on row index)
    public static void deleteData(int rowIndex) {
        ArrayList<String[]> data = loadData();
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.remove(rowIndex); // Remove the specified row
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                out.writeObject(data);
            } catch (IOException e) {
                System.err.println("Error deleting data: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid row index: " + rowIndex);
        }
    }

    // Update data in file (based on row index)
    public static void updateData(int rowIndex, String[] updatedRowData) {
        ArrayList<String[]> data = loadData();
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.set(rowIndex, updatedRowData); // Update the specified row
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                out.writeObject(data);
            } catch (IOException e) {
                System.err.println("Error updating data: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid row index: " + rowIndex);
        }
    }
}
