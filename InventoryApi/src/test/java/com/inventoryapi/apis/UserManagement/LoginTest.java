package com.inventoryapi.apis.UserManagement;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.ConfigurationReader;
import com.inventoryapi.utilities.UserManagement.LoginClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest
{
    @Test (priority = 0)
    public void invalidLoginTest()
    {
        LoginClass.runLoginTest("Invalid Login", "username", "password", 400, false, "Invalid credentials");
    }

    @Test(priority = 1)
    public void testWithInvalidToken()
    {
        LoginClass.runAuthTokenTest("Invalid Token Access", "incorrectToken", 401, "Invalid token");
    }

    @Test (priority = 2)
    public void validLoginTest()
    {
        LoginClass.runLoginTest("Valid Login", username, password, 200, true, username + " logged in successful");
    }
}
