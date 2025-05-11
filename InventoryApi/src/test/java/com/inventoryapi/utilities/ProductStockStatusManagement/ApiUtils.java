package com.inventoryapi.utilities.ProductStockStatusManagement;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.SingletonClass;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class ApiUtils extends BaseTest
{
    public static Response create(String token, String endpoint, String body)
    {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body)
                .post(endpoint);
    }

    public static Response fetch(String token, String endpoint)
    {
        return given()
                .header("Authorization", "Bearer " + token)
                .get(endpoint);
    }

    public static Response update(String token, String endpoint, String body)
    {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body)
                .put(endpoint);
    }

    public static Response delete(String token, String endpoint)
    {
        return given()
                .header("Authorization", "Bearer " + token)
                .delete(endpoint);
    }

    public static void assertProductDetails(Response response, String name, String price, String productType, String quantity, String createdAt)
    {
        assertEquals(response.jsonPath().getString("name"), name);
        assertEquals(response.jsonPath().getString("price"), price);
        assertEquals(response.jsonPath().getString("productType"), productType);
        assertEquals(response.jsonPath().getString("quantity"), quantity);
        assertEquals(response.jsonPath().getString("createdAt"), createdAt);
    }

    public static void logResponse(Response response)
    {
        System.out.println("Status code : " + response.getStatusCode());
        System.out.println("Response body : " + response.getBody().asString());
    }

    public static void createOrderAndValidate(String orderType)
    {
        String body = ApiUtils.stockPayload(orderType, SingletonClass.productId, 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/orders/", body);

        System.out.println("Status code : " + response.getStatusCode());
        System.out.println("Response body : " + response.getBody().asString());

        assertEquals(201, response.getStatusCode(), "Status code should be 201");
        assertEquals(response.jsonPath().getString("success"), "true");
        assertEquals(response.jsonPath().getString("productId"), SingletonClass.productId);
        assertEquals(response.jsonPath().getString("quantity"), "5");

        String orderId = response.jsonPath().getString("orderId");
        assertNotNull(orderId, "orderId should not be null");
        SingletonClass.orderId = orderId;

        String newStock = response.jsonPath().getString("newStock");
        assertNotNull(newStock, "newStock should not be null");
        SingletonClass.currentStock = newStock;

        if (orderType.contentEquals("buy"))
        {
            assertEquals(response.jsonPath().getString("orderType"), "buy");
        }
        else if (orderType.contentEquals("sell"))
        {
            assertEquals(response.jsonPath().getString("orderType"), "sell");
        }
    }

    public static void getAllProductsInfo()
    {
        test = extent.createTest("Test Fetch All the Products");
        test.info("Starting the Fetch All the Products test");

        Response response = ApiUtils.fetch(token, SingletonClass.baseUrl + "/products/");
        System.out.println("Status code : " + response.getStatusCode());
        String responseBody = response.getBody().asString();
        System.out.println("Response body : " + responseBody);
        assertEquals(200, response.getStatusCode(), "Status code should be 200");
        assertFalse(response.jsonPath().getList("$").isEmpty(), "Response should contain at least one product");
        assertNotNull(response.jsonPath().getString("productId"), "Response should contain 'productId'");
        SingletonClass.productId = response.jsonPath().getString("[0].productId");

        test.pass("Test Fetch All the Products completed successfully");
    }

    public static String productPayload(String name, double price, String type, int quantity)
    {
        return "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"productType\": \"" + type + "\",\n" +
                "  \"quantity\": " + quantity + "\n" +
                "}";
    }

    public static String stockPayload(String orderType, String productId, int quantity)
    {
        return "{\n" +
                "  \"orderType\": \"" + orderType + "\",\n" +
                "  \"productId\": \"" + productId + "\",\n" +
                "  \"quantity\": " + quantity + "\n" +
                "}";
    }
}
