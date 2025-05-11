package com.inventoryapi.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader
{
    private static final Properties properties = new Properties();

    static
    {
        try
        {
            // Load the properties file
            FileInputStream inputStream = new FileInputStream("src/test/resources/Properties/configuration.properties");
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    // Method to get property value by key
    public static String getProperty(String key)
    {
        return properties.getProperty(key);
    }
}
