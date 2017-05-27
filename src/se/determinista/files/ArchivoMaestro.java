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
public class ArchivoMaestro
{
    public static String EXTENSION = ".master";
    private RandomAccessFile archivo;
    private ArchivoIndice index;
    private IndexTree IndexTree;
    private String path;

    /**
     * @param _name
     * @param _permissions
     */
    public ArchivoMaestro(String _name, String _permissions) {
        createFile(_name, _permissions);
    }

    /**
     * Create the Master and Index files with the specified paremeters
     *
     * @param _name
     * @param _permissions
     */
    private void createFile(String _name, String _permissions) {
        try {
            path = _name;
            archivo = new RandomAccessFile(_name + EXTENSION, _permissions);
            index = new ArchivoIndice(_name + ArchivoIndice.EXTENSION, _permissions);
        } catch (Exception ex) {
        }
    }

    /**
     * Sets a new Rule in the archivo, you must consider the size of each record.
     *
     * @param _rule
     */
    private void newRecord(Rule _rule) {
        StringBuffer buffer;
        try {
            archivo.seek(archivo.length());
            index.nuevoRegistro(_rule.getId(), archivo.getFilePointer());
            // System.out.println("Memoria, papu :'v :" + archivo.getFilePointer());
            archivo.writeByte(_rule.getId());
            for (int i = 0; i < Rule.RECORDS_QUANTITY; i++) {
                try {
                    buffer = new StringBuffer(_rule.getRecords()[i]);
                } catch (Exception ex) {
                    buffer = new StringBuffer();
                }
                buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
                archivo.writeChars(buffer.toString());
            }
            buffer = new StringBuffer(_rule.getConsequent());
            buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
            archivo.writeChars(buffer.toString());
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
                archivo.seek(IndexTree.getRuleMemoryAddress(_ruleNumber));
                rule.setId(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currRecord[i] = archivo.readChar();
                    }
                    recordsArray[recordNumber] = new String(currRecord);
                }
                rule.setRecords(recordsArray);

                for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                    currRecord[i] = archivo.readChar();
                }
                rule.setConsequent(new String(currRecord));
            }
        } catch (Exception ex) {
            System.out.println("Error, la regla no existe en la base de conocimientos : " + ex.getMessage());
        }
        return rule;
    }

    /**
     * This method ask the user new Rules using the io archivo of the keyboard
     */
    public void insertNewRules() {
        if (new java.io.File(path + ArchivoMaestro.EXTENSION).exists()) {
            String input = null;
            do {
                System.out.println("Ingrese una nueva regla con el formato ID-Ant1^Ant2^...^Ant5-Consecuente \n O  \"x\" para salir");
                input = new Scanner(System.in).next();
                if (!input.equals("x"))
                    try {
                        newRecord(castToRule(input));
                    } catch (Exception ex) {
                        System.out.println("Regla mal formada");
                    }
            } while (!input.equals("x"));
        } else {
            createFile("baseConocimiento", "rw");
            insertNewRules();
        }
    }

    /**
     * This method shows in terminal all the rules contained in the ArchivoMaestro
     */
    public void printAllRules() {
        byte ruleId;
        String[] recordsArray = new String[Rule.RECORDS_QUANTITY];
        char[] currCharacteristic = new char[Rule.SINGULAR_RECORD_SIZE];
        boolean EOF = false;

        try {
            archivo.seek(0);
            do {
                ruleId = archivo.readByte();
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currCharacteristic[i] = archivo.readChar();
                    }
                    recordsArray[recordNumber] = new String(currCharacteristic);
                }
                for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                    currCharacteristic[i] = archivo.readChar();
                }
                System.out.println("ID: " + ruleId + " " + getRecords(recordsArray) + "-> " + new String(currCharacteristic));
            } while (!EOF);
        } catch (Exception ex) {
            System.out.println("\nFinished reading ArchivoMaestro\n");
            EOF = true;
        }
    }

    /**
     * Shows the Index stored in ArchivoIndice to the user
     */
    public void showIndex() {
        index.mostrarIndice();
    }

    /**
     * Returns all the records contained in the Rule separated by comma
     * @param _records
     * @return
     */
    public String getRecords(String[] _records) {
        int counter = 0;
        String records = "";
        for (String record : _records) {
            if (counter < Rule.RECORDS_QUANTITY) {
                if (!record.trim().isEmpty()) {
                    records += record + "^";
                }
            } else {
                //1records += record;
            }
        }
        return records.substring(0, records.length() - 1);
    }

    /**
     * This method generates the binary tree according to each rule number within the ArchivoIndice
     */
    public void generateTree() {
        IndexTree = new IndexTree();
        IndexTree.generateIndexTree();
    }

    /**
     * This method generates a Rule based on the formated input, malformed input will throw exception.
     *
     * @param _input
     * @return A new Rule
     * @throws Exception
     */
    private Rule castToRule(String _input) throws Exception {
        return new Rule(Byte.parseByte(_input.split("-")[0]), _input.split("-")[1].split("\\^"), _input.split("-")[2]);
    }

    /**
     * Returns the records of all the rules
     *
     * @return
     */
    public ArrayList<Rule> getAllRulesRecords() {
        ArrayList<Rule> rules = new ArrayList<>();
        String[] recordsArray;
        char[] currRecord;
        try {
            archivo.seek(0);
            Rule rule;
            do {
                rule = new Rule();
                recordsArray = new String[Rule.RECORDS_QUANTITY];
                currRecord = new char[Rule.SINGULAR_RECORD_SIZE];
                rule.setId(archivo.readByte());
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currRecord[i] = archivo.readChar();
                    }
                    recordsArray[recordNumber] = new String(currRecord);
                }
                rule.setRecords(recordsArray);
                archivo.skipBytes(Rule.SINGULAR_RECORD_SIZE * 2);
                rules.add(rule);
            } while (true);
        } catch (Exception ex) {
            //System.out.println("\nAll rules set\n");
        }
        return rules;
    }


    public void deleteAllRules() {
        try {
            archivo.setLength(0);
            index.limpiarArchivo();
        } catch (Exception ex) {
        }
    }
}
