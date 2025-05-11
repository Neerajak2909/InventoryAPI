package com.inventoryapi.utilities;

import com.inventoryapi.utilities.UserManagement.LoginClass;
import io.restassured.response.Response;

public class SingletonClass
{
    private String token;
    public static final String baseUrl = ConfigurationReader.getProperty("baseUrl");
    private static SingletonClass instance;
    public static String productId;
    public static String orderId;
    public static String createdAt;
    public static String currentStock;

    private SingletonClass()
    {
        generateToken();
    }

    public static SingletonClass getInstance()
    {
        if (instance == null)
        {
            synchronized (SingletonClass.class)
            {
                if (instance == null)
                {
                    instance = new SingletonClass();
                }
            }
        }
        return instance;
    }

    private void generateToken()
    {
        String username = ConfigurationReader.getProperty("username");
        String password = ConfigurationReader.getProperty("password");
        Response response = LoginClass.loginAndFetchResponse(username, password);
        this.token = LoginClass.getToken(response);
        System.out.println("Token initialized: " + token);
    }

    public String getToken()
    {
        return token;
    }
}
