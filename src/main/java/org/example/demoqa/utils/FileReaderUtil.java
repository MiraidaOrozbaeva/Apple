package org.example.demoqa.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileReaderUtil {

    private static Properties properties;
    static {
        try {
            // Загружаем через classpath, а не по относительному пути
            InputStream inputStream = FileReaderUtil.class
                    .getClassLoader()
                    .getResourceAsStream("App.properties");
            if (inputStream == null) {
                throw new RuntimeException("App.properties not found in classpath");
            }
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            String path = "resources/App.properties";
//            FileInputStream fileInputStream = new FileInputStream(path);
//            properties = new Properties();
//            properties.load(fileInputStream);
//            fileInputStream.close();
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public static String getValue(String key){
        return properties.getProperty(key.trim());
    }
}
