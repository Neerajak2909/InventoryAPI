package com.inventoryapi.apis;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.inventoryapi.utilities.ConfigurationReader;
import com.inventoryapi.utilities.SingletonClass;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BaseTest
{
    public static String token;
    public static String username = ConfigurationReader.getProperty("username");
    public static String password = ConfigurationReader.getProperty("password");
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected static ExtentSparkReporter spark;

    @BeforeSuite
    public void setupExtent() throws IOException
    {
        spark = new ExtentSparkReporter("target/LoggerSpark/index.html");
        spark.loadXMLConfig(getClass().getClassLoader().getResource("extent-config.xml").getPath());

        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeTest
    public void initToken()
    {
        token = SingletonClass.getInstance().getToken();
        System.out.println("Token in setUp: " + token);
    }

    @AfterMethod
    public void getResult(ITestResult result)
    {
        if (result.getStatus() == ITestResult.FAILURE)
        {
            test.fail(result.getThrowable());
        }
        else if (result.getStatus() == ITestResult.SUCCESS)
        {
            test.pass("Test Passed");
        }
        else
        {
            test.skip(result.getThrowable());
        }
    }

    @AfterSuite
    public void tearDown()
    {
        extent.flush();
        openReport();
    }

    public void openReport()
    {
        try
        {
            File htmlFile = new File("target/LoggerSpark/index.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
