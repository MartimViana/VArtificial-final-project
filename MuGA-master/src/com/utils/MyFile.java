//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::     Antonio Manso                        Luis Correia                   ::
//::     manso@ipt.pt                   Luis.Correia@ciencias.ulisboa.pt     ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::                                                                         ::
//::     Instituto Politécnico de Tomar                                      ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                             (c) 2019    ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package com.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 22/mar/2016, 11:47:12
 *
 * @author zulu - computer
 */
public class MyFile {

    /**
     * gets the path of one filename
     *
     * @param fullFilename full name of the file ( teste.txt )
     * @return path ( x:/dir/ )
     */
    public static String getPath(String fullFilename) {
        File file = new File(fullFilename);
        fullFilename = file.getAbsolutePath(); // get full path
        file = new File(fullFilename);

        if (file.isDirectory()) {
            return file.getAbsolutePath() + File.separatorChar;
        }
        return file.getParent() == null
                ? "." + File.separatorChar
                : file.getParent() + File.separatorChar;
    }

    /**
     * create the path of one filename
     *
     * @param fullFilename full name of the file ( teste.txt )
     * @return path ( x:/dir/ )
     */
    public static void createPath(String fullFilename) {
        File file = new File(fullFilename);
        fullFilename = file.getAbsolutePath();
        file = new File(fullFilename);

        if (file.isDirectory()) {
            file.mkdirs();
        } else {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * gets the file name
     *
     * @param fullFilename full path ( c:/dir/file.txt)
     * @return name (file.txt)
     */
    public static String getFileName(String fullFilename) {
        File file = new File(fullFilename);
        if (file.isDirectory()) {
            return "";
        }
        String path = file.getAbsolutePath();
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    /**
     * gets the file name
     *
     * @param fullFilename full path ( c:/dir/file.txt)
     * @return name (file.txt)
     */
    public static String getFullFileName(String fullFilename) {
        File file = new File(fullFilename);
        return file.getAbsolutePath();
    }

    /**
     * gets only the file name
     *
     * @param fullFilename full path ( c:/dir/file.txt)
     * @return only the name (file)
     */
    public static String getFileNameOnly(String fullFilename) {
        String fileName = getFileName(fullFilename);
        if (fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * gets only the file name
     *
     * @param fullFilename full path ( c:/dir/file.txt)
     * @return only the name (file)
     */
    public static String getSimpleNameDotExtension(String fullFilename) {
        String fileName = getFileName(fullFilename);
        if (fileName.contains(File.separator)) {
            return fileName.substring(fileName.lastIndexOf(File.separator));
        }
        return fileName;
    }

    /**
     * gets only the extension of the file name
     *
     * @param fullFilename full path ( c:/dir/file.txt)
     * @return extension (txt)
     */
    public static String getFileExtension(String fullFilename) {
        String fileName = getFileName(fullFilename);
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

     public static String changeFileNameExtension(String fileName, String newExtension) {

        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName + "." + newExtension;
    }

    /**
     * read all bytes in the text file
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException ex) {
            // Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * read all bytes in the text file
     *
     * @param fileName
     * @return
     */
    public static void saveToFile(String text, String fileName) {
        File file = new File(fileName);
        file = new File(file.getAbsolutePath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(text);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1) {
                outputStream.write(buf, 0, readLen);
            }
            return outputStream.toByteArray();
        }
    }

    public static String readResource(String resource) {
        try {
            InputStream inputStream = MyFile.class.getResourceAsStream(resource);
            return new String(readAllBytes(inputStream));
        } catch (Exception ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }

    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 201603221147L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2016  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
