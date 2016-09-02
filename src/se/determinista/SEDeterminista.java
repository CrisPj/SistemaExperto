/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.determinista;

import se.determinista.files.Master_File;
import se.determinista.files.Rule;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Andrés
 */
public class SEDeterminista {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String path = "E:\\knowledgebase";
        Master_File _file = new Master_File(path,"rw");
        if(!new File(path+Master_File.FILE_EXTENSION).exists())
        {

            _file.newRecord(new Rule((byte)1,new String[]{"Tos","Fiebre","Escurrimiento","Prurito","Ardor"},"Gripe"));
            _file.newRecord(new Rule((byte)2,new String[]{"Tos","Arritmia","","",""},"Tosferina"));
            _file.newRecord(new Rule((byte)3,new String[]{"Tos","Fiebre","","",""},"Hemorroides"));
            _file.newRecord(new Rule((byte)4,new String[]{"Tos","Fiebre","Escurrimiento","",""},"Gripe"));
            _file.newRecord(new Rule((byte)5,new String[]{"Tos","Fiebre","Escurrimiento","Prurito",""},"Gripe"));
            _file.newRecord(new Rule((byte)6,new String[]{"Tos","Fiebre","Escurrimiento","Prurito","Ardor"},"Gripe"));
        }

        Scanner sc = new Scanner(System.in);
        byte number=0;
        do
        {
            System.out.println("Ingrese número de regla");
            number=sc.nextByte();
            Rule.printRule(_file.readRule(number));
        }while(number!=0);
        _file.close();

    }
}
