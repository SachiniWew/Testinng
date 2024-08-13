import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MenuTest{
    public static void main(String[] args) {
        // 1) Launch browser
        ChromeDriver driver = new ChromeDriver();

        // 2) Open URL
        driver.get("https://sachiniwew.github.io/myportfolio.github.io/index.html");

        // 3) Validate title
        String act_title = driver.getTitle();
        if (act_title.equals("Sachini Wewalwala")) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed");
        }

        // Resize the window to a small screen size
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 812)); // iPhone X size

        // Wait for the hamburger menu icon to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement hamburgerMenuIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navPanelToggle")));

        // Scroll to the hamburger menu icon and click it using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", hamburgerMenuIcon);
        js.executeScript("arguments[0].click();", hamburgerMenuIcon);

        // Add a small delay to ensure the menu items are loaded
        try {
            Thread.sleep(2000); // 2 seconds delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Array of menu item XPath and their descriptions
        String[][] menuItems = {
                {"//*[@id=\"navPanel\"]/nav/ul[1]/li[2]/a", "Projects"},
                {"//*[@id=\"navPanel\"]/nav/ul[1]/li[3]/a", "Resume"},
                {"//*[@id=\"navPanel\"]/nav/ul[1]/li[4]/a", "Contact"},
                {"//*[@id=\"navPanel\"]/nav/ul[2]/li[1]/a/span", "Gmail"},
        };

        // Loop through each menu item and check visibility
        for (String[] menuItem : menuItems) {
            boolean clicked = false;
            int attempts = 0;

            while (!clicked && attempts < 3) {
                try {
                    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(menuItem[0])));
                    js.executeScript("arguments[0].scrollIntoView(true);", element); // Ensure the element is in view
                    js.executeScript("arguments[0].click();", element); // Click using JavaScript
                    System.out.println("Hamburger menu " + menuItem[1] + " test passed.");
                    clicked = true;
                } catch (StaleElementReferenceException | ElementNotInteractableException e) {
                    System.out.println("Retrying " + menuItem[1] + " due to " + e.getClass().getSimpleName());
                } catch (Exception e) {
                    System.out.println("Error with menu item " + menuItem[1] + ": " + e.getMessage());
                    break;
                }
                attempts++;
            }

            if (!clicked) {
                System.out.println("Hamburger menu " + menuItem[1] + " test failed.");
            } else {
                // Navigate back to the home page to reset the menu state
                driver.get("https://sachiniwew.github.io/myportfolio.github.io/index.html");

                // Resize the window to a small screen size again
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 812)); // iPhone X size

                // Re-open the hamburger menu
                hamburgerMenuIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navPanelToggle")));
                js.executeScript("arguments[0].scrollIntoView(true);", hamburgerMenuIcon);
                js.executeScript("arguments[0].click();", hamburgerMenuIcon);

                // Add a small delay to ensure the menu items are loaded
                try {
                    Thread.sleep(2000); // 2 seconds delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
