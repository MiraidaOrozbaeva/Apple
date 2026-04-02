package org.example.demoqa.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileReaderUtil {

    private static Properties properties;
    static {
        try {
            properties = new Properties();
            ClassLoader classLoader = FileReaderUtil.class.getClassLoader();
            try (var inputStream = classLoader.getResourceAsStream("App.properties")) {
                if (inputStream == null) {
                    throw new FileNotFoundException("App.properties not found in classpath");
                }
                properties.load(inputStream);
            }
        } catch (IOException e) {
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
