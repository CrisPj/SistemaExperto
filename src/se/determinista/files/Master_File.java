/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.determinista.files;

import java.io.RandomAccessFile;

/**
 *
 * @author Andrés
 */
public class Master_File {

    private RandomAccessFile file;
    private Index_File index;
    public static String FILE_EXTENSION = ".kdb" ;

    /**
     *
     * @param _name
     * @param _permissions
     */
    public Master_File(String _name, String _permissions)
    {
        try{
            file = new RandomAccessFile(_name+FILE_EXTENSION, _permissions);
            index = new Index_File(_name+Index_File.FILE_EXTENSION, _permissions);
        }catch(Exception ex){}
    }

    /**
     * Sets a new Rule in the file, you must consider the size of each record.
     * @param _rule
     */
    public void newRecord(Rule _rule)
    {
        StringBuffer buffer;
        try{
            file.seek(file.length());
            index.newRecord(_rule.getId(),file.getFilePointer());
            System.out.println("Memoria, papu :'v :"+file.getFilePointer());
            file.writeByte(_rule.getId());
            for ( int i = 0 ; i < Rule.RECORDS_QUANTITY ; i++ )
            {
                try{
                    buffer = new StringBuffer(_rule.getRecords()[i]);
                }catch(Exception ex)
                {
                    buffer = new StringBuffer();
                }
                buffer.setLength(Rule.SINGULAR_RECORD_SIZE);
                file.writeChars(buffer.toString());
            }
            buffer = new StringBuffer(_rule.getConsequent());
            buffer.setLength(Rule.CONSEQUENT_SIZE);
            file.writeChars(buffer.toString());
        }catch(Exception ex)
        {
            
        }
    }

    /**
     * You should specify the number of the rule about you want to know its information.
     * @param _ruleNumber
     */
    public Rule readRule(byte _ruleNumber)
    {
        Rule rule = new Rule();
        String[] recordsArray = new String[Rule.RECORDS_QUANTITY];
        char[] consequent = new char[Rule.CONSEQUENT_SIZE];
        try{
            boolean ban=true;
            file.seek(0);
            int recordCounter = 1;
            if(_ruleNumber>0) {
                do {
                    byte ruleNumber = file.readByte();
                    if (ruleNumber == _ruleNumber)
                        ban = !ban;
                    else {
                        //System.out.println(""+(Rule.RECORD_SIZE*recordCounter));
                        file.seek(Rule.RECORD_SIZE*recordCounter); //Desplazamiento del puntero dentro del archivo
                        recordCounter++; //Incremento del número de registro leído
                    }
                } while (ban);

                char[] currRecord = new char[Rule.SINGULAR_RECORD_SIZE];
                for (int recordNumber = 0; recordNumber < Rule.RECORDS_QUANTITY ; recordNumber++) {
                    for (int i = 0; i < Rule.SINGULAR_RECORD_SIZE; i++) {
                        currRecord[i] = file.readChar();
                    }
                    recordsArray[recordNumber] = new String(currRecord);
                }

                for (int i = 0; i < Rule.CONSEQUENT_SIZE; i++) {
                    consequent[i] = file.readChar();
                }

                rule.setId(_ruleNumber);
                rule.setRecords(recordsArray);
                rule.setConsq(new String(consequent));
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error, la regla no existe en la base de conocimientos : "+ex.getMessage());
        }
        return rule;
    }

    public void close()
    {
     try
     {
         this.close();
     }catch (Exception ex)
     {
         System.out.println(""+ex.getMessage());
     }
    }

    public boolean exists()
    {
        return this.exists();
    }
}
