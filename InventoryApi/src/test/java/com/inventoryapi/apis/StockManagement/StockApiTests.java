package com.inventoryapi.apis.StockManagement;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.ProductStockStatusManagement.ApiUtils;
import com.inventoryapi.utilities.SingletonClass;
import io.restassured.response.Response;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class StockApiTests extends BaseTest
{
    @Test (priority = 0)
    public void testGetAllProducts()
    {
        ApiUtils.getAllProductsInfo();
    }

    @Test (priority = 1)
    public void testCreateOrderBuy()
    {
        test = extent.createTest("Test Create the Order (Buy)");
        test.info("Starting the Create the Order (Buy) test");

        ApiUtils.createOrderAndValidate("buy");

        test.pass("Test Create the Order (Buy) completed successfully");
    }

    @Test (priority = 2)
    public void testCreateOrderSell()
    {
        test = extent.createTest("Test Create the Order (Sell)");
        test.info("Starting the Create the Order (Sell) test");

        ApiUtils.createOrderAndValidate("sell");

        test.pass("Test Create the Order (Sell) completed successfully");
    }

    @Test (priority = 3)
    public void testGetCurrentStockForProduct()
    {
        test = extent.createTest("Test Fetch the Current Stock for the Product");
        test.info("Starting the Fetch the Current Stock for the Product test");

        Response response = ApiUtils.fetch(token, SingletonClass.baseUrl + "/orders/product/" + SingletonClass.productId);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("productId"), SingletonClass.productId);
        assertEquals(response.jsonPath().getString("currentStock"), SingletonClass.currentStock);

        test.pass("Test Fetch the Current Stock for the Product completed successfully");
    }

    @Test (priority = 4)
    public void testCreateOrderInvalidOrderType()
    {
        test = extent.createTest("Test Create the Order Invalid Order Type");
        test.info("Starting the Create the Order Invalid Order Type test");

        String body = ApiUtils.stockPayload("sells", SingletonClass.productId, 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Invalid order type. Must be \"buy\" or \"sell\"");

        test.pass("Test Create the Order Invalid Order Type completed successfully");
    }

    @Test (priority = 5)
    public void testCreateOrderInvalidId()
    {
        test = extent.createTest("Test Create the Order Invalid ID");
        test.info("Starting the Create the Order Invalid ID test");

        String body = ApiUtils.stockPayload("buy", "0eba100b-83ba-44da-ac26-234b32683ca3-12345", 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 404);

        assertEquals(response.jsonPath().getString("message"), "Product not found");

        test.pass("Test Create the Order Invalid ID completed successfully");
    }

    @Test (priority = 6)
    public void testCreateOrderNegativeQuantity()
    {
        test = extent.createTest("Test Create the Order Negative Quantity");
        test.info("Starting the Create the Order Negative Quantity test");

        String body = ApiUtils.stockPayload("buy", SingletonClass.productId, -5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Quantity must be a positive number");

        test.pass("Test Create the Order Negative Quantity completed successfully");
    }

    @Test (priority = 7)
    public void testCreateOrderMissingProductId()
    {
        test = extent.createTest("Test Create the Order Without Product ID");
        test.info("Starting the Create the Order Without Product ID test");

        String body = ApiUtils.stockPayload("buy", "", 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Invalid product ID");

        test.pass("Test Create the Order Without Product ID completed successfully");
    }

    @Test (priority = 8)
    public void testCreateOrderMissingQuantity()
    {
        test = extent.createTest("Test Create the Order Without Quantity");
        test.info("Starting the Create the Order Without Quantity test");

        String body = ApiUtils.stockPayload("buy", SingletonClass.productId, 0);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Quantity must be a positive number");

        test.pass("Test Create the Order Without Quantity completed successfully");
    }
}
