package com.inventoryapi.apis.ProductManagement;

import com.inventoryapi.apis.BaseTest;
import com.inventoryapi.utilities.ProductStockStatusManagement.ApiUtils;
import com.inventoryapi.utilities.SingletonClass;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProductApiTests extends BaseTest
{
    private static final String PRODUCT_NAME = "Test Game";
    private static final String UPDATED_NAME = "Test Updated";

    @Test (priority = 0)
    public void testGetAllProducts()
    {
        ApiUtils.getAllProductsInfo();
    }

    @Test (priority = 1)
    public void testCreateProduct()
    {
        test = extent.createTest("Test Creation of the Product");
        test.info("Starting the Creation of the Product test");

        String payload = ApiUtils.productPayload(PRODUCT_NAME, 29.99, "games", 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/products/", payload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 201);

        SingletonClass.productId = response.jsonPath().getString("productId");
        assertNotNull(SingletonClass.productId);

        SingletonClass.createdAt = response.jsonPath().getString("createdAt");
        assertNotNull(SingletonClass.createdAt);

        ApiUtils.assertProductDetails(response, PRODUCT_NAME, "29.99", "games", "5", SingletonClass.createdAt);

        test.pass("Test Creation of the Product completed successfully");
    }

    @Test (priority = 2)
    public void testGetProduct()
    {
        test = extent.createTest("Test Fetch the Product");
        test.info("Starting the Fetch the Product test");

        Response response = ApiUtils.fetch(token, SingletonClass.baseUrl + "/products/" + SingletonClass.productId);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 200);

        ApiUtils.assertProductDetails(response, PRODUCT_NAME, "29.99", "games", "5", SingletonClass.createdAt);
        assertEquals(response.jsonPath().getString("productId"), SingletonClass.productId);

        test.pass("Test Fetch the Product completed successfully");
    }

    @Test (priority = 3)
    public void testUpdateProduct()
    {
        test = extent.createTest("Test Update the Product");
        test.info("Starting the Update the Product test");

        String updatedPayload = ApiUtils.productPayload(UPDATED_NAME, 100, "test", 10);
        Response response = ApiUtils.update(token, SingletonClass.baseUrl + "/products/" + SingletonClass.productId, updatedPayload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 200);

        ApiUtils.assertProductDetails(response, UPDATED_NAME, "100", "test", "10", SingletonClass.createdAt);
        assertEquals(response.jsonPath().getString("productId"), SingletonClass.productId);

        test.pass("Test Update the Product completed successfully");
    }

    @Test (priority = 4)
    public void testDeleteProduct()
    {
        test = extent.createTest("Test Delete the Product");
        test.info("Starting the Delete the Product test");

        Response response = ApiUtils.delete(token, SingletonClass.baseUrl + "/products/" + SingletonClass.productId);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 200);

        assertEquals(response.jsonPath().getString("message"), "Product removed");

        test.pass("Test Delete the Product completed successfully");
    }

    @Test (priority = 5)
    public void testCreateProductMissingName()
    {
        test = extent.createTest("Test Creation of the Product Without Name");
        test.info("Starting the Creation of the Product Without Name test");

        String payload = ApiUtils.productPayload("", 29.99, "games", 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/products/", payload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Validation failed");

        test.pass("Test Creation of the Product Without Name completed successfully");
    }

    @Test (priority = 6)
    public void testCreateProductNegativePrice()
    {
        test = extent.createTest("Test Creation of the Product Negative Price");
        test.info("Starting the Creation of the Product Negative Price test");

        String payload = ApiUtils.productPayload(PRODUCT_NAME, -29.99, "games", 5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/products/", payload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Price must be greater than 0");

        test.pass("Test Creation of the Product Negative Price completed successfully");
    }

    @Test (priority = 7)
    public void testCreateProductNegativeQuantity()
    {
        test = extent.createTest("Test Creation of the Product Negative Quantity");
        test.info("Starting the Creation of the Product Negative Quantity test");

        String payload = ApiUtils.productPayload(PRODUCT_NAME, 29.99, "games", -5);
        Response response = ApiUtils.create(token, SingletonClass.baseUrl + "/products/", payload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 400);

        assertEquals(response.jsonPath().getString("message"), "Validation failed");

        test.pass("Test Creation of the Product Negative Quantity completed successfully");
    }

    @Test (priority = 8)
    public void testGetProductInvalidId()
    {
        test = extent.createTest("Test Fetch the Product Invalid ID");
        test.info("Starting the Fetch the Product Invalid ID test");

        Response response = ApiUtils.fetch(token, SingletonClass.baseUrl + "/products/" + "12345");

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 404);

        assertEquals(response.jsonPath().getString("message"), "Product not found");

        test.pass("Test Fetch the Product Invalid ID completed successfully");
    }

    @Test (priority = 9)
    public void testUpdateProductInvalidId()
    {
        test = extent.createTest("Test Update the Product Invalid Id");
        test.info("Starting the Update the Product Invalid Id test");

        String updatedPayload = ApiUtils.productPayload(UPDATED_NAME, 100, "test", 10);
        Response response = ApiUtils.update(token, SingletonClass.baseUrl + "/products/" + "0eba100b-83ba-44da-ac26-234b32683ca3-12345", updatedPayload);

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 404);

        assertEquals(response.jsonPath().getString("message"), "Product not found");

        test.pass("Test Update the Product Invalid Id completed successfully");
    }

    @Test (priority = 10)
    public void testDeleteProductInvalidId()
    {
        test = extent.createTest("Test Delete the Product Invalid Id");
        test.info("Starting the Delete the Product Invalid Id test");

        Response response = ApiUtils.delete(token, SingletonClass.baseUrl + "/products/" + "12345");

        ApiUtils.logResponse(response);
        assertEquals(response.getStatusCode(), 404);

        assertEquals(response.jsonPath().getString("message"), "Product not found");

        test.pass("Test Delete the Product Invalid Id completed successfully");
    }
}
