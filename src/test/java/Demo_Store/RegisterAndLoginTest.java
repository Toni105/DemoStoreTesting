package Demo_Store;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


public class RegisterAndLoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // Set browser in fullscreen
        driver.manage().window().maximize();
        // Navigate to shop site
        driver.get("http://demostore.supersqa.com/");
    }


    @Test (priority = 1)
    // Complete registration of new user, then log out
    public void registerUser_and_Logout() {

        // Verify that page title is correct and page is visible successfully
        String title = driver.getTitle();
        Assert.assertEquals(title,"Demo Store – Practice Automation The Right Way","\n Error: The title is not correct! \n");

        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        char randomCharacter = (char)((new java.util.Random().nextBoolean() ? 'a' : 'A') + new java.util.Random().nextInt(26));


        // Enter Email address and Password, then click "Register"
        String Email_address = randomCharacter + "rg@falsemail.com";
        String Password = "Idontdothatbut123!";
        driver.findElement(By.id("reg_email")).sendKeys(Email_address);
        driver.findElement(By.id("reg_password")).sendKeys(Password);
        driver.findElement(By.xpath("//button[text()='Register']")).click();

        // Click "My account" and fill details First name and Last name
        driver.findElement(By.linkText("Account details")).click();
        String First_name = "First";
        String Last_name = "Last";
        driver.findElement(By.id("account_first_name")).sendKeys(First_name);
        driver.findElement(By.id("account_last_name")).sendKeys(Last_name);

        // Change Display name
        driver.findElement(By.id("account_display_name")).clear();
        driver.findElement(By.id("account_display_name")).sendKeys(First_name + Last_name);

        // Click at "Save Changes" button
        driver.findElement(By.xpath("//button[text()='Save changes']")).click();

        // Click "Logout"
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();

    }


    @Test (priority = 2)
//    Leave the email field empty in the registration
    public void register_without_Email() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Password
        driver.findElement(By.id("reg_password")).sendKeys("Idoenggtdfothddatbut123!");

        // Click "Register"
        driver.findElement(By.xpath("//button[text()='Register']")).click();

        // Confirm that registration cannot be done without valid e-mail
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Error: Please provide a valid email address.");
    }


    @Test (priority = 3)
    // Enter e-mail witch is not valid (without @)
    public void register_without_validEmail(){
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Email address
        driver.findElement(By.id("reg_email")).sendKeys("firstdcdc");

        // Enter Password
        driver.findElement(By.id("reg_password")).sendKeys("Idihughugzugzgzfftbut123!");

        // Click "Register"
        driver.findElement(By.xpath("//button[text()='Register']")).click();

        // Confirm that registration cannot be done without e-mail
        Assert.assertEquals(driver.findElement(By.xpath("//button[text()='Register']")).getText(), "Register");

    }


    @Test (priority = 4)
    // Enter e-mail witch is used to register before on the site
    public void register_with_UsedEmail() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Email address
        driver.findElement(By.id("reg_email")).sendKeys("first.last@name.com");

        // Enter Password
        driver.findElement(By.id("reg_password")).sendKeys("Idontdothatbut123!");

        // Click "Register"
        driver.findElement(By.xpath("//button[text()='Register']")).click();

        // Confirm that registration cannot be done without valid e-mail
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Error: An account is already registered with your email address. Please log in.");
    }


    @Test (priority = 5)
    public void login() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Email address and Password
        driver.findElement(By.id("username")).sendKeys("first.last@name.com");
        driver.findElement(By.id("password")).sendKeys("Idontdothatbut123!");

        // Check in "Remember me" and click "Log in"
        driver.findElement(By.id("rememberme")).click();
        driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();

        // Click "Logout"
        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
    }


    @Test (priority = 6)
    public void login_without_Email() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Password
        driver.findElement(By.id("password")).sendKeys("Idontdothatbut123!");

        // Click "Log in"
        driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();

        // Confirm that login cannot be done without e-mail
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Error: Username is required.");
    }


    @Test (priority = 7)
    public void login_without_Password() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Email address
        driver.findElement(By.id("username")).sendKeys("first.last@name.com");

        // Click "Log in"
        driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();

        // Confirm that login cannot be done without password
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Error: The password field is empty.");
    }


    @Test (priority = 8)
    public void login_unknown_Email() {
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter unknown Email address
        driver.findElement(By.id("username")).sendKeys("unknown@mail.com");

        // Enter Password
        driver.findElement(By.id("password")).sendKeys("Idontdothatbut123!");

        // Click "Log in"
        driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();

        // Confirm that login cannot be done without password
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Unknown email address. Check again or try your username.");

    }


    @Test (priority = 9)
    public void login_incorrect_Password(){
        // Click on "My account"
        driver.findElement(By.linkText("My account")).click();

        // Enter Email address
        String username = "first.last@name.com";
        driver.findElement(By.id("username")).sendKeys(username);

        // Enter Password
        driver.findElement(By.id("password")).sendKeys("wrongpassword");

        // Click "Log in"
        driver.findElement(By.xpath("//*[@id=\"customer_login\"]/div[1]/form/p[3]/button")).click();

        // Confirm that login cannot be done without password
        Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='woocommerce'])[1]")).getText(), "Error: The password you entered for the email address " + username + " is incorrect. Lost your password?");

    }


    @AfterMethod (enabled = false)
    public void takeScreensForFailures(ITestResult testResult) {

        // Taking screenshot only when test Fails
        if (ITestResult.FAILURE == testResult.getStatus()) {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File source = screenshot.getScreenshotAs(OutputType.FILE);
            File destination = new File(System.getProperty("user.dir") + "/resources/screenshots/" + testResult.getName() + ".png");
            try {
                FileHandler.copy(source, destination);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @AfterMethod
    public void tearDown(){

        driver.quit();
    }
}