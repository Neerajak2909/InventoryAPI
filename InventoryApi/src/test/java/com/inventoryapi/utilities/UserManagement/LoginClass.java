package com.inventoryapi.utilities.UserManagement;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.ConfigurationReader;
import com.inventoryapi.utilities.SingletonClass;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class LoginClass extends BaseTest
{
    public static Response loginAndFetchResponse(String username, String password)
    {
        String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
        String endpointUrl = SingletonClass.baseUrl + ConfigurationReader.getProperty("loginEndpoint");

        System.out.println("POST to: " + endpointUrl);
        System.out.println("Body: " + requestBody);

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(endpointUrl);
    }

    public static String getToken(Response response)
    {
        if (response.getStatusCode() == 200 && response.jsonPath().get("token") != null)
        {
            String token = response.jsonPath().getString("token");
            System.out.println("Token: " + token);
            return token;
        }
        else
        {
            System.out.println("Login failed. No token returned.");
            return null;
        }
    }

    public static void runLoginTest(String testName, String username, String password, int expectedStatus, boolean shouldContainToken, String expectedMessage)
    {
        test = extent.createTest("Test the " + testName);
        test.info("Starting the " + testName + " test");

        Response response = LoginClass.loginAndFetchResponse(username, password);
        int actualStatus = response.getStatusCode();
        String responseBody = response.getBody().asString();
        String actualMessage = response.jsonPath().getString("message");

        assertEquals(actualStatus, expectedStatus, "Status code mismatch");

        if (shouldContainToken) {
            assertTrue(responseBody.contains("token"), "Token should be present");
        }
        else
        {
            assertFalse(responseBody.contains("token"), "Token should not be present");
        }

        System.out.println(actualMessage);
        assertEquals(actualMessage, expectedMessage, "Message mismatch");

        test.pass("Test " + testName + " completed successfully");
    }

    public static void runAuthTokenTest(String testName, String token, int expectedStatus, String expectedMessage)
    {
        test = extent.createTest("Test the " + testName);
        test.info("Starting the " + testName + " test");

        String endpointUrl = SingletonClass.baseUrl + "/products";
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .post(endpointUrl);

        int actualStatus = response.getStatusCode();
        String responseBody = response.getBody().asString();

        assertEquals(actualStatus, expectedStatus, "Status code mismatch");
        assertTrue(responseBody.contains(expectedMessage), "Expected message not found");

        System.out.println("Response message: " + response.jsonPath().getString("message"));

        test.pass("Test " + testName + " completed successfully");
    }
}
