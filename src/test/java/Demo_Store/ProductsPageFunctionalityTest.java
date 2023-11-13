package Demo_Store;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;


public class ProductsPageFunctionalityTest {

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
    public void productPageTest() throws InterruptedException {
        // Go to product page (T-Shirt)
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Choose a size "Medium" of T-Shirt
        WebElement medium_size = driver.findElement(By.xpath("(//td[@class='value']//select)[2]"));
        Select medium = new Select(medium_size);
        Thread.sleep(1000);
        medium.selectByVisibleText("Medium");

        // Take a look of every available color
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td[@class='value']//select)[1]")));
        WebElement all_colors = driver.findElement(By.xpath("(//td[@class='value']//select)[1]"));
        Thread.sleep(1000);

        // Blue color
        Select color = new Select(all_colors);
        color.selectByVisibleText("Blue");

        // Green color
        color.selectByVisibleText("Green");

        // Red color
        color.selectByVisibleText("Red");

        // Check Additional information of product
        driver.findElement(By.linkText("Additional information")).click();

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();

        // Go on the first next product to the left
        driver.findElement(By.xpath("(//nav[@class='storefront-product-pagination']//img)[1]")).click();

        // Confirmed that site change to the first product to the left
        Assert.assertTrue(driver.findElement(By.xpath("//h1[text()='T-Shirt with Logo']")).isDisplayed());
    }


    @Test (priority = 2)
    public void writeReview() {
        // Go to product page (T-Shirt)
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();

        // Check that all 5 ratings work (1_star -> 2_star -> 3_star -> 4_star -> 5_star)
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        jse6.executeScript("window.scrollBy(0,750)", "");

        for(String starNumber : List.of("1", "2", "3", "4", "5")) {
            driver.findElement(By.className("star-" + starNumber)).click();
        }

        // Write a review
        char randomCharacter = (char)((new java.util.Random().nextBoolean() ? 'a' : 'A') + new java.util.Random().nextInt(26));

        String text_of_review = "T-shirt is great. I love it T" + randomCharacter;
        driver.findElement(By.xpath("//p[@class='comment-form-comment']//textarea[1]")).sendKeys(text_of_review);

        // Enter "Name" and "Email", then click "Submit"
        driver.findElement(By.id("author")).sendKeys("Fiddgfrst_name");
        driver.findElement(By.id("email")).sendKeys("firdst.last@name.com");
        driver.findElement(By.id("submit")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//div[@class='description']//p[1]")).getText(), text_of_review);
    }


    // Sending review without rating (1-5 stars)
    @Test (priority = 3)
    public void submitReviewWithoutRating(){
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();
        driver.findElement(By.id("submit")).click();

        //Handle alert pop-up "Please select a rating"
        Alert alarm = driver.switchTo().alert();
        alarm.accept();
    }


    @Test (priority = 4)
    public void submitReviewWithoutComment(){
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();

        // Give a rating of 5 stars
        driver.findElement(By.className("star-5")).click();

        // Enter "Name" and "Email", then click "Submit"
        driver.findElement(By.id("author")).sendKeys("Fiddgfrst_name");
        driver.findElement(By.id("email")).sendKeys("firdst.last@name.com");
        driver.findElement(By.id("submit")).click();

        // Verify "Error: Please type your comment text."
        Assert.assertEquals(driver.getCurrentUrl(),"http://demostore.supersqa.com/wp-comments-post.php","There are no expected error!");
    }


    @Test (priority = 5)
    public void submitReviewWithoutName() {
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();

        // Give a rating of 5 stars
        driver.findElement(By.className("star-5")).click();

        // Write a review
        String text_of_review = "T-shirt is great. I love it :)";
        driver.findElement(By.xpath("//p[@class='comment-form-comment']//textarea[1]")).sendKeys(text_of_review);

        // Enter "Email", then click "Submit"
        driver.findElement(By.id("email")).sendKeys("firdst.last@name.com");
        driver.findElement(By.id("submit")).click();

        // Verify "Error: Please fill the required fields."
        Assert.assertEquals(driver.getCurrentUrl(),"http://demostore.supersqa.com/wp-comments-post.php","There are no expected error!");
    }


    @Test (priority = 6)
    public void submitReviewWithoutEmail() {
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys("V-Neck T-Shirt");
        driver.findElement(By.id("woocommerce-product-search-field-0")).sendKeys(Keys.ENTER);

        // Check review of product
        driver.findElement(By.linkText("Reviews (0)")).click();

        // Give a rating of 5 stars
        driver.findElement(By.className("star-5")).click();

        // Write a review
        String text_of_review = "T-shirt is great. I love it :)";
        driver.findElement(By.xpath("//p[@class='comment-form-comment']//textarea[1]")).sendKeys(text_of_review);

        // Enter "Name", then click "Submit"
        driver.findElement(By.id("author")).sendKeys("Fiddgfrst_name");
        driver.findElement(By.id("submit")).click();

        // Verify "Error: Please fill the required fields."
        Assert.assertEquals(driver.getCurrentUrl(),"http://demostore.supersqa.com/wp-comments-post.php","There are no expected error!");
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}


