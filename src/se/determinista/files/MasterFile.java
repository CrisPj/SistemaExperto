/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.determinista.files;

import se.determinista.tree.IndexTree;
import se.determinista.tree.Rule;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Andrés
 */
public class MasterFile {

    public static String FILE_EXTENSION = ".kdb";

    private RandomAccessFile file;
    private IndexFile index;
    private IndexTree IndexTree;
    private String path;

    /**
     * @param _name
     * @param _permissions
     */
    public MasterFile(String _name, String _permissions) {
        createFile(_name, _permissions);
    }

    public void createFile(String _name, String _permissions) {
        try {
            path = _name;
            file = new RandomAccessFile(_name + FILE_EXTENSION, _permissions);
            index = new IndexFile(_name + IndexFile.FILE_EXTENSION, _permissions);
        } catch (Exception ex) {
        }
    }

    /**
     * Sets a new Rule in the file, you must consider the size of each record.
     *
     * @param _rule
     */
    public void newRecord(Rule _rule) {
        StringBuffer buffer;
        try {
            file.seek(file.length());
            index.newRecord(_rule.getId(), file.getFilePointer());
            // System.out.println("Memoria, papu :'v :" + file.getFilePointer());
            file.writeByte(_rule.getId());
            for (int i = 0; i < Rule.RECORDS_QUANTITY; i++) {
                try {
                    buffer = new StringBuffer(_rule.getRecords()[i]);
                } catch (Exception ex) {
                    buffer = new StringBuffer();
                }
                buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
                file.writeChars(buffer.toString());
            }
            buffer = new StringBuffer(_rule.getConsequent());
            buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
            file.writeChars(buffer.toString());
        } catch (Exception ex) {

        }
    }

    /**
     * You should specify the ID of the rule about you want to know its information.
     *
     * @param _ruleNumber
     * @return The Rule with the specified ID if it exists
     */
    public Rule getRule(byte _ruleNumber) {
        Rule rule = new Rule();
        String[] recordsArray = new String[Rule.RECORDS_QUANTITY];
        char[] currRecord = new char[Rule.SINGULAR_RECORD_SIZE];

        try {
            if (_ruleNumber > 0) {
                file.seek(IndexTree.getRuleMemoryAddress(_ruleNumber));
                rule.setId(file.readByte());
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currRecord[i] = file.readChar();
                    }
                    recordsArray[recordNumber] = new String(currRecord);
                }
                rule.setRecords(recordsArray);

                for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                    currRecord[i] = file.readChar();
                }
                rule.setConsq(new String(currRecord));
            }
        } catch (Exception ex) {
            System.out.println("Error, la regla no existe en la base de conocimientos : " + ex.getMessage());
        }
        return rule;
    }

    /**
     * This method ask the user new Rules using the io file of the keyboard
     */
    public void insertNewRules() {
        if (new java.io.File(path + MasterFile.FILE_EXTENSION).exists()) {
            String input = null;
            do {
                System.out.println("Ingrese una nueva regla con el formato ID-Ant1,,Ant3,...,Ant5-Consecuente \n O  \"x\" para salir");
                input = new Scanner(System.in).next();
                if (!input.equals("x"))
                    try {
                        newRecord(castToRule(input));
                    } catch (Exception ex) {
                        System.out.println("Regla mal formada");
                    }
            } while (!input.equals("x"));
        } else {
            createFile("E:\\knowledgebase", "rw");
        }
    }

    /**
     * This method generates a Rule based on the formated input, malformed input will throw exception.
     *
     * @param _input
     * @return A new Rule
     * @throws Exception
     */
    private Rule castToRule(String _input) throws Exception {
        return new Rule(Byte.parseByte(_input.split("-")[0]), _input.split("-")[1].split(","), _input.split("-")[2]);
    }

    public ArrayList<Rule> getAllRulesRecords() {
        ArrayList<Rule> rules = new ArrayList<>();
        String[] recordsArray;
        char[] currRecord;
        try {
            file.seek(0);
            Rule rule;
            do {
                rule = new Rule();
                recordsArray = new String[Rule.RECORDS_QUANTITY];
                currRecord = new char[Rule.SINGULAR_RECORD_SIZE];
                rule.setId(file.readByte());
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currRecord[i] = file.readChar();
                    }
                    recordsArray[recordNumber] = new String(currRecord);
                }
                rule.setRecords(recordsArray);
                file.skipBytes(Rule.SINGULAR_RECORD_SIZE * 2);
                rules.add(rule);
            } while (true);
        } catch (Exception ex) {
            //System.out.println("\nAll rules set\n");
        }
        return rules;
    }

    /**
     * This method shows in terminal all the rules contained in the MasterFile
     */
    public void printAllRules() {
        byte ruleId;
        String[] recordsArray = new String[Rule.RECORDS_QUANTITY];
        char[] currCharacteristic = new char[Rule.SINGULAR_RECORD_SIZE];
        boolean EOF = false;

        try {
            file.seek(0);
            do {
                ruleId = file.readByte();
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currCharacteristic[i] = file.readChar();
                    }
                    recordsArray[recordNumber] = new String(currCharacteristic);
                }
                for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                    currCharacteristic[i] = file.readChar();
                }
                System.out.println("ID: " + ruleId + " " + getRecords(recordsArray) + "-> " + new String(currCharacteristic));
            } while (!EOF);
        } catch (Exception ex) {
            System.out.println("\nFinished reading MasterFile\n");
            EOF = true;
        }
    }

    public void showIndex() {
        index.showIndex();
    }

    public String getRecords(String[] _records) {
        int counter = 0;
        String records = "";
        for (String record : _records) {
            if (counter < Rule.RECORDS_QUANTITY) {
                records += record + ",";
            } else {
                records += record;
            }
        }
        return records;
    }

    /**
     * This method generates the binary tree according to each rule number within the IndexFile
     */
    public void generateTree() {
        IndexTree = new IndexTree();
        IndexTree.generateIndexTree();
    }

    /**
     * Closes the stream of the file
     */
    public void close() {
        try {
            this.close();
        } catch (Exception ex) {
            System.out.println("" + ex.getMessage());
        }
    }

}
