package Demo_Store_Cross_Browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class CheckoutTest_Cross_Browser {

    WebDriver driver;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String cross) {
        String browserName = cross;

        if(browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }   else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }   else if (browserName.equalsIgnoreCase("edge")){
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        // Set browser in fullscreen
        driver.manage().window().maximize();
        // Navigate to shop site
        driver.get("http://demostore.supersqa.com/");
    }

    // Make order as new customer
    @Test (priority = 1)
    public void checkoutNewCustomer() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" address
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")));
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter first name
        jse6.executeScript("window.scrollBy(0,1000)", "");
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

    }

    @Test(priority = 2)
    // Make order as existing customer
    public void checkoutExistingCustomer () throws InterruptedException {
        // Choose one product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();

        // Scroll down
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" adress
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Click on "Returning customer?"
        driver.findElement(By.linkText("Click here to login")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        // Enter "e-mail" and "password" of existing customer
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("first.last@name.com");
        driver.findElement(By.id("password")).sendKeys("Idontdothatbut123!");

        // Click log-in button
        driver.findElement(By.xpath("//*[@id=\"post-8\"]/div/div/form[1]/p[4]/button")).click();
    }


    @Test (priority = 3)
    // Make order and check functionality to ship to a different address (than billing address)
    public void checkoutShipToDifferentAddress() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" address
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")));
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter Billing first name
        jse6.executeScript("window.scrollBy(0,1000)", "");
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter Billing last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter Billing address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter Billing phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter Billing e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it again. hahaha");

        // Click checkbox "Ship to a different address?"
        driver.findElement(By.id("ship-to-different-address-checkbox")).click();

        //Fill required Shipping field
        // Enter Shipping first name
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(3));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.id("shipping_first_name")));
        driver.findElement(By.id("shipping_first_name")).sendKeys("Prvi");
        // Enter Shipping last name
        driver.findElement(By.id("shipping_last_name")).sendKeys("Zadnji");
        // Enter Shipping address
        driver.findElement(By.id("shipping_address_1")).sendKeys("Solinska 365");

        // Click "Place order"
        driver.findElement(By.id("place_order")).click();

    }


    @Test (priority = 4)
    // Fill out the entire checkout list except "First name", then order
    public void CheckoutMissingFirstName() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" address
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")));
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details (don't write first name)
        jse6.executeScript("window.scrollBy(0,1000)", "");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing First name is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing First name is a required field.","Error: There should not be possible to execute an order without \"First name\"");
    }


    @Test (priority = 5)
    // Fill out the entire checkout list except "Last name", then order
    public void CheckoutMissingLastName() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" address
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")));
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details (don't write last name)
        jse6.executeScript("window.scrollBy(0,1000)", "");
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing First name is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing Last name is a required field.", "Error: There should not be possible to execute an order without \"Last name\"");
    }


    @Test (priority = 6)
    // Fill out the entire checkout list except "Street Address", then order
    public void CheckoutMissingStreetAddress() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Change address button
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/a")).click();
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,500)", "");

        // Change country drop down
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#select2-calc_shipping_country-container")));
        driver.findElement(By.cssSelector("#select2-calc_shipping_country-container")).click();
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys("Croatia");
        driver.findElement(By.xpath("/html/body/span/span/span[1]/input")).sendKeys(Keys.ENTER);

        // Enter state
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calc_shipping_state\"]")));
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_state\"]")).sendKeys("Splitsko-dalmatinska županija");

        // Enter city
        driver.findElement(By.xpath("//*[@id=\"calc_shipping_city\"]")).sendKeys("Split");

        // Enter zip code
        driver.findElement(By.id("calc_shipping_postcode")).sendKeys("21000");

        // Click "Update" address
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")));
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/form/section/p[5]/button")).click();
        Thread.sleep(1500);

        // Check is address is changed
        WebElement check = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div[2]/div/table/tbody/tr[2]/td/p"));
        Assert.assertEquals(check.getText(),"Shipping to Split, Splitsko-dalmatinska županija, 21000, Croatia.", "\n The change of address is not recognized \n");

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details (don't write Street Address)
        jse6.executeScript("window.scrollBy(0,1000)", "");
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing Street Address is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing Street address is a required field.", "Error: There should not be possible to execute an order without \"Street address\"");
    }


    @Test (priority = 7)
    // Fill out the entire checkout list except "Town / City", then order
    public void CheckoutMissingCity() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter ZIP Code
        driver.findElement(By.id("billing_postcode")).sendKeys("21000");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing Town / City is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing Town / City is a required field.", "Error: There should not be possible to execute an order without \"Town / City\"");
    }


    @Test (priority = 8)
    // Fill out the entire checkout list except "ZIP Code", then order
    public void CheckoutMissingZIPCode() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter Town/City
        driver.findElement(By.id("billing_city")).sendKeys("Split");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing ZIP Code is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing ZIP Code is a required field.", "Error: There should not be possible to execute an order without \"ZIP Code\"");
    }


    @Test (priority = 9)
    // Fill out the entire checkout list except "Phone", then order
    public void CheckoutMissingPhone() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter Town/City
        driver.findElement(By.id("billing_city")).sendKeys("Split");
        // Enter ZIP Code
        driver.findElement(By.id("billing_postcode")).sendKeys("21000");
        // Enter e-mail
        driver.findElement(By.id("billing_email")).sendKeys("first.last@name.com");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing Phone is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing Phone is a required field.", "Error: There should not be possible to execute an order without \"Phone\"");
    }


    @Test (priority = 10)
    // Fill out the entire checkout list except "Email address", then order
    public void CheckoutMissingEmail() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        // Fill Billing details
        // Enter first name
        driver.findElement(By.id("billing_first_name")).sendKeys("Firstelo");
        // Enter last name
        driver.findElement(By.id("billing_last_name")).sendKeys("Lastelo");
        // Enter address
        driver.findElement(By.id("billing_address_1")).sendKeys("Splitska 365");
        // Enter Town/City
        driver.findElement(By.id("billing_city")).sendKeys("Split");
        // Enter ZIP Code
        driver.findElement(By.id("billing_postcode")).sendKeys("21000");
        // Enter phone number
        driver.findElement(By.id("billing_phone")).sendKeys("0123456789");
        // Enter "order notes"
        driver.findElement(By.cssSelector("textarea[name='order_comments']")).sendKeys("Please don't deliver it. hahaha");

        // Click "Place order"
        Thread.sleep(2000);
        driver.findElement(By.id("place_order")).click();

        // Verify existence of error "Billing Email address is a required field."
        WebDriverWait w = new WebDriverWait(driver,Duration.ofSeconds(5));
        w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='woocommerce-error']//li[1]")));

        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Billing Email address is a required field.", "Error: There should not be possible to execute an order without \"Email address\"");
    }


    @Test(priority = 11)
    // There are "Free shipping on orders over $50"
    public void freeShippingOption () throws InterruptedException {
        // Add 4 products in cart (value of all products greater than $50)
        driver.findElement(By.xpath("(//a[contains(@class,'button product_type_simple')])[3]")).click();
        for (String addProducts : List.of("4", "5", "6", "8")) {
            driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[" + addProducts + "]/a[2]")).click();
        }

        // View cart
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "5 items"));
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Check is there "Free shipping" option
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"shipping_method\"]/li[2]/label")).getText(), "Free shipping", "\n There are no Free shipping \n");
    }

    @Test (priority = 12)
    // Check that the coupon does not work on every text
    //TODO Ask developer for working Coupon !!
    public void functionalityCouponCode() throws InterruptedException {
        //  Choose product
        driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[4]/a[2]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "1 item"));

        // Go to cart
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Click the "Proceed to checkout" button
        driver.findElement(By.cssSelector("#post-7 > div > div > div.cart-collaterals > div > div > a")).click();

        //Click on 'Click here to enter your code'
        driver.findElement(By.xpath("//a[contains(text(),'Click here to enter your code')]")).click();

        // Enter "123456789" on Coupon Code entry
        String Coupon_code = "123456789";
        Thread.sleep(2000);
        driver.findElement(By.id("coupon_code")).sendKeys(Coupon_code);

        //Click on "Apply coupon"
        driver.findElement(By.xpath("//button[text()='Apply coupon']")).click();

        // Verify existence of error "Coupon "123456789" does not exist!"
        Thread.sleep(2000);
        String text_of_error = driver.findElement(By.xpath("//ul[@class='woocommerce-error']//li[1]")).getText();
        Assert.assertEquals(text_of_error,"Coupon \"" + Coupon_code+ "\" does not exist!", "Error: Coupon should not work!");
    }


    @AfterMethod
    public void tearDown() {

        driver.quit();
    }

}
