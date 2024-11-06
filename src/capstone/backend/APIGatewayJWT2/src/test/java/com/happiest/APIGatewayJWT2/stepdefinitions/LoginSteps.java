package com.happiest.APIGatewayJWT2.stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.Assert.*;

public class LoginSteps {
    WebDriver driver;

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/login"); // Adjust the URL as needed
    }

    @When("the user enters valid credentials")
    public void the_user_enters_valid_credentials() {
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']")); // Adjust locator if needed
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']")); // Adjust locator if needed
        emailField.sendKeys("abhignya001@gmail.com"); // Replace with actual valid email
        passwordField.sendKeys("Abhi1234"); // Replace with actual valid password
    }

    @When("the user enters invalid credentials")
    public void the_user_enters_invalid_credentials() {
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email']")); // Adjust locator if needed
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password']")); // Adjust locator if needed
        emailField.sendKeys("invalid@example.com"); // Replace with actual invalid email
        passwordField.sendKeys("invalidpassword"); // Replace with actual invalid password
    }

    @When("clicks the login button")
    public void clicks_the_login_button() {
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // Adjust locator if needed
        loginButton.click();
    }

    @Then("the user should be redirected to the appropriate dashboard")
    public void the_user_should_be_redirected_to_the_appropriate_dashboard() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("doctorhome")); // Adjust the URL as needed
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl); // Print the current URL for debugging
        assertTrue("Expected URL to contain '/doctorhome', but was: " + currentUrl, currentUrl.contains("doctorhome")); // Adjust the URL as needed
        driver.quit();
    }

    @Then("an error message should be displayed")
    public void an_error_message_should_be_displayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("login-text-danger"))); // Adjust locator if needed
        assertTrue(errorMessage.isDisplayed());
        driver.quit();
    }
}
