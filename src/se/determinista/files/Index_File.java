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
public class Index_File {

    private RandomAccessFile file;

    public static byte KEY_SIZE = Rule.ID_SIZE;
    public static String FILE_EXTENSION = ".idx" ;

    /**
     *
     * @param _name
     * @param _permissions
     */
    public Index_File(String _name, String _permissions)
    {
        try{
            file = new RandomAccessFile(_name, _permissions);
        }catch(Exception ex){}
    }

    public void newRecord(byte _id,long _memoryAddress)
    {
        try{
            file.writeByte(_id);
            file.writeLong(_memoryAddress);
        }catch(Exception ex)
        {
            System.out.println("Error:" + ex.getMessage());
        }
    }

}
