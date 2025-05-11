package com.inventoryapi.apis.Status;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.ProductStockStatusManagement.ApiUtils;
import com.inventoryapi.utilities.SingletonClass;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class StatusApiTest extends BaseTest
{
    @Test
    public void testStatus()
    {
        test = extent.createTest("Test Fetch the Status");
        test.info("Starting the Fetch the Status test");

        Response response = ApiUtils.fetch(token, SingletonClass.baseUrl + "/status/");

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 200);

        assertEquals(response.jsonPath().getString("status"), "OK");
        assertEquals(response.jsonPath().getString("dbStatus"), "Connected");

        test.pass("Test Fetch the Status completed successfully");
    }
}
