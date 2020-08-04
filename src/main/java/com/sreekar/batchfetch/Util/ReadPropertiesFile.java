package com.sreekar.batchfetch.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertiesFile {
    private static ReadPropertiesFile readPropertiesFile;
    private static Properties properties;
    private static final String propertiesFileLocation = "src/main/resources/config.properties";

    private ReadPropertiesFile() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(propertiesFileLocation));
    }

    public static ReadPropertiesFile getInstance() throws IOException {
        synchronized (ReadPropertiesFile.class){
            if(readPropertiesFile==null)
                readPropertiesFile = new ReadPropertiesFile();
        }
        return readPropertiesFile;
    }

    public String getProperty(String propName){
        return properties.getProperty(propName);
    }
}
