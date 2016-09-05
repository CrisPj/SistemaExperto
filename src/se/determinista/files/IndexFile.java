/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.determinista.files;

import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @author Andrés
 */
public class IndexFile {

    public static String FILE_EXTENSION = ".idx";
    private RandomAccessFile file;

    /**
     * @param _name
     * @param _permissions
     */
    public IndexFile(String _name, String _permissions) {
        try {
            file = new RandomAccessFile(_name, _permissions);
        } catch (Exception ex) {
        }
    }

    /**
     * Writes a new record to the Index, specifying the rule ID and its memory address
     *
     * @param _id
     * @param _memoryAddress
     */
    public void newRecord(byte _id, long _memoryAddress) {
        try {
            file.seek(file.length());
            file.writeByte(_id);
            file.writeLong(_memoryAddress);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

    /**
     * Returns all the addresses of the rules
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getRulesAddresses() {
        ArrayList<String> rules = new ArrayList<>();

        try {
            file.seek(0);
            do {
                rules.add(file.readByte() + "-" + file.readLong());
            } while (true);
        } catch (Exception ex) {
            System.out.println("Fin del índice: " + rules.size());
        }

        return rules;
    }

    /**
     * Shows in terminal the Index for the rules contained in the MasterFile
     */
    public void showIndex() {
        boolean EOF = false;

        try {
            file.seek(0);
            do {
                System.out.println("ID: " + file.readByte() + "-->" + file.readLong());
            } while (!EOF);
        } catch (Exception ex) {
            System.out.println("\nFinished reading IndexFile\n");
        }
    }
}
