package Demo_Store;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartFunctionalityTest {

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


    // Add 5 products to the cart, remove them all and then undo the last removed product
    @Test (priority = 1)
    public void addAndRemoveProductsInCart() throws InterruptedException {
        // Add 5 products from page
        driver.findElement(By.xpath("(//a[contains(@class,'button product_type_simple')])[3]")).click();
        for(String liNumber : List.of("4", "5", "6", "8")) {
            driver.findElement(By.xpath("//*[@id=\"main\"]/ul/li[" + liNumber + "]/a[2]")).click();
        }

        // View cart
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a/span[2]"), "5 items"));
        driver.findElement(By.xpath("//*[@id=\"site-header-cart\"]/li[1]/a")).click();

        // Remove all products
        for (int i = 0; i < 5; i++) {
            driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/form/table/tbody/tr[1]/td[1]/a")).click();
            Thread.sleep(2000);
        }

        // Verify that "Cart" is empty
        WebElement empty_cart = driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div/p"));
        Assert.assertTrue(empty_cart.isDisplayed(), "\n The cart is not empty ! \n");

        // Undo last remove product
        driver.findElement(By.xpath("//*[@id=\"post-7\"]/div/div/div/div/a")).click();
    }


    // Taking screenshot only when test Fails
    @AfterMethod
    public void takeScreenshotForFailures(ITestResult testResult) {
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


    @Test (priority = 2)
    public void checkProductSortingInAlphabeticallyOrder() {
        // Check is there correct number of products (there are 37 products in shop)
        String number_of_product = driver.findElement(By.xpath("(//p[@class='woocommerce-result-count'])")).getText();
        Assert.assertTrue(number_of_product.contains("37"), "\n There are wrong number of products in shop! \n");

        // Check default(alphabetically) sorting
        // Capture the names of products
        List<WebElement> beforeFilterPrice = driver.findElements(By.className("woocommerce-loop-product__title"));
        // Remove and convert the string into double unsorted
        List<String> unsortedFilterPriceList = new ArrayList<>();

        for(WebElement p : beforeFilterPrice) {
            unsortedFilterPriceList.add(p.getText().toLowerCase());
        }

        // Capture the prices second time and sort them
        // Remove and convert the string into double unsorted
        List<String> sortedFilterPriceList = new ArrayList<>();

        for(WebElement p : beforeFilterPrice) {
            sortedFilterPriceList.add(p.getText().toLowerCase());
        }
        Collections.sort(sortedFilterPriceList);

        //Verify products order
        Assert.assertEquals(unsortedFilterPriceList, sortedFilterPriceList,"Sorting is not done correctly");
    }


    @Test(enabled = false)
    public void checkProductSortingByPriceAscendingOrder() {
        //TODO = correct this error !!
        //TODO = test is disable, until it is fixed

        // Change list to the: "Sort by price: low to high"
        WebElement product_sort = driver.findElement(By.xpath("(//select[@name='orderby'])"));
        Select sort = new Select(product_sort);
        sort.selectByVisibleText("Sort by price: low to high");

        // Capture the prices
        List<WebElement> beforeFilterPrice = driver.findElements(By.className("price"));
        // Remove and convert the string into double unsorted
        List<Double> unsortedFilterPriceList = new ArrayList<>();

        for(WebElement p : beforeFilterPrice) {
            unsortedFilterPriceList.add(Double.valueOf(p.getText().replace("$","").replace("– 45.00","").replace("3.00 ","").replace("10.99 ","")));
        }

        // Capture the prices second time and sort them
        // Remove and convert the string into double unsorted
        List<Double> sortedFilterPriceList = new ArrayList<>();

        for(WebElement p : beforeFilterPrice) {
            sortedFilterPriceList.add(Double.valueOf(p.getText().replace("$","").replace("– 45.00","").replace("3.00 ","").replace("10.99 ","")));
        }
        Collections.sort(sortedFilterPriceList);

        //Verify products order
        Assert.assertEquals(unsortedFilterPriceList, sortedFilterPriceList,"The sorting is no correct");
    }


    @Test (enabled = false)
    public void checkProductSortingByPriceDescendingOrder() {
        //TODO = correct this error !!
        //TODO = test is disable, until it is fixed

        // Change list to the: "Sort by price: high to low"
        WebElement product_sort = driver.findElement(By.xpath("(//select[@name='orderby'])"));
        Select sort = new Select(product_sort);
        sort.selectByVisibleText("Sort by price: high to low");

        // Capture the prices
        List<WebElement> beforeFilterPrice = driver.findElements(By.className("price"));
        // Remove and convert the string into double unsorted
        List<Double> unsortedFilterPriceList = new ArrayList<>();

        for (WebElement p : beforeFilterPrice) {
            unsortedFilterPriceList.add(Double.valueOf(p.getText().replace("$", "").replace("15.00 – ", "").replace(" 18.00", "").replace(" 16.00", "").replace(" 13.50", "")));
        }

        // Capture the prices second time and sort them
        // Remove and convert the string into double unsorted
        List<Double> sortedFilterPriceList = new ArrayList<>();

        for (WebElement p : beforeFilterPrice) {
            sortedFilterPriceList.add(Double.valueOf(p.getText().replace("$", "").replace("15.00 – ", "").replace(" 18.00", "").replace(" 16.00", "").replace(" 13.50", "")));
        }
        Collections.sort(sortedFilterPriceList);
        Collections.reverse(sortedFilterPriceList);

        // Verify products order
        Assert.assertEquals(unsortedFilterPriceList, sortedFilterPriceList, "The sorting is no correct");
    }


    @Test (priority = 3)
    public void findProductBySearchBar() {
        // Searching for a specific product (V-Neck T-Shirt)
        driver.findElement(By.id("woocommerce-product-search-field-0")).clear();
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Confirmed that search bar has found expected product
        Assert.assertTrue(driver.findElement(By.xpath("//h1[text()='V-Neck T-Shirt']")).isDisplayed());
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}


