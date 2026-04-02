package org.example.demoqa.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileReaderUtil {

    private static Properties properties;
    static {
        try {
            String path = "resources/App.properties";
            FileInputStream fileInputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return properties.getProperty(key.trim());
    }

    public static void main(String[] args) {
        System.out.println(getValue("browser"));
    }
}
