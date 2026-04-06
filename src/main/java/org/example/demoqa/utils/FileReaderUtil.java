package org.example.demoqa.utils;

import io.qameta.allure.Step;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileReaderUtil {

    private static Properties properties;
    static {
        properties = new Properties();
        try {
            String path = "src/test/resources/App.properties";
            FileInputStream fileInputStream = new FileInputStream(path);
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
}
