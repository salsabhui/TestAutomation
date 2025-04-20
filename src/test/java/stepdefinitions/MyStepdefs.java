package stepdefinitions;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {

    private WebDriver driver;
    private WebDriverWait wait;

    // test details
    private String UserEmail = "alwaysPrada" + System.currentTimeMillis() + "@gmail.com";
    private String UserPassword = "alwaysDior";

    @Given("I am on the registration page for basketballengland.co.uk")
    public void iAmOnTheRegistrationPageForBasketballenglandCoUk() {
        driver = new ChromeDriver(); // start the browser
        driver.manage().window().maximize(); // maximize the window
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 sec wait set up
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount"); // open url
    }

    private void enterUserDetails(String UserFirstName, String UserLastName, String UserPassword, String ConfirmUserPassword) {
        // user details
        driver.findElement(By.cssSelector("input#dp")).sendKeys("10/05/2000");
        driver.findElement(By.cssSelector("input#member_firstname")).sendKeys(UserFirstName);

        if (UserLastName != null) { // checking if last name is provided
            driver.findElement(By.cssSelector("input#member_lastname")).sendKeys(UserLastName);

        }
        driver.findElement(By.cssSelector("input#member_emailaddress")).sendKeys(UserEmail);
        driver.findElement(By.cssSelector("input#member_confirmemailaddress")).sendKeys(UserEmail);
        driver.findElement(By.cssSelector("#signupunlicenced_password")).sendKeys(UserPassword);
        driver.findElement(By.cssSelector("#signupunlicenced_confirmpassword")).sendKeys(ConfirmUserPassword);
    }

    // tick the agreement boxes
    private void checkAllBoxes() {
        agreeToAll("sign_up_25");
        agreeToAll("sign_up_26");
        agreeToAll("fanmembersignup_agreetocodeofethicsandconduct");
    }

    // ticks all the checkboxes
    private void agreeToAll(String boxId) {
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='" + boxId + "']"))); // wait for box to be clickable
        label.click(); // click the label for the checkbox
        WebElement checkbox = driver.findElement(By.id(boxId)); // find the checkbox

        if (!checkbox.isSelected()) { // if checkbox isn't ticked
            label.click(); // tick it
        }
    }

    //submission of form
    private void submitForm() {
        // wait for submission button to be clickable and clicking
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn"))).click();
    }

    // checking if error message appears on website
    private void verifyValidationError(String expectedError) {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".field-validation-error"))); // waiting for the error message to appear
        String actual = error.getText(); // we get the actual error message from the website
        System.out.println("Validation error message: " + actual); // error message is printed
        assertEquals(expectedError, actual); // check if expected and actual message is a match
    }

    // form for correct details
    @When("I enter the correct registration details")
    public void iEnterTheCorrectRegistrationDetails() {
        enterUserDetails("Prada", "Dior", UserPassword, UserPassword);
        checkAllBoxes();
        submitForm();
    }

    // successfully a member message
    @Then("I should become a member successfully")
    public void iShouldBecomeAMemberSuccessfully() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.gray"))); // waiting for the message to appear
        String actual = driver.findElement(By.cssSelector("h2.gray")).getText(); // we get the actual message from the website
        String expected = "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND"; // expected message
        assertEquals(expected, actual); // check if expected and actual message is a match
    }

    // form for correct details with last name missing
    @When("I enter the correct registration details without filling in the last name")
    public void iEnterTheCorrectRegistrationDetailsWithoutFillingInTheLastName() {
        enterUserDetails("Chanel", null, UserPassword, UserPassword);
        checkAllBoxes();
        submitForm();
    }

    // checking for error message for missing last name
    @Then("I should get an error message indicating that the last name is missing")
    public void iShouldGetAnErrorMessageIndicatingThatTheLastNameIsMissing() {
        verifyValidationError("Last Name is required");
    }

    // form for correct details but the password does not match
    @When("I enter the correct registration details but the password does not match")
    public void iEnterTheCorrectRegistrationDetailsButThePasswordDoesNotMatch() {
        enterUserDetails("Louis", "Vitton", UserPassword, "Hermes");
        checkAllBoxes();
        submitForm();
    }

    // checking for error message
    @Then("I should get an error message indicating that the password does not match")
    public void iShouldGetAnErrorMessageIndicatingThatThePasswordDoesNotMatch() {
        verifyValidationError("Password did not match");
    }

    // form for not accepting the terms and conditions
    @When("I enter the correct registration details but do not accept the terms and conditions")
    public void iEnterTheCorrectRegistrationDetailsButDoNotAcceptTheTermsAndConditions() {
        enterUserDetails("Prada", "Dior", UserPassword, UserPassword); // correct details are entered
        agreeToAll("sign_up_26"); // only tick to sign_up_26
        agreeToAll("fanmembersignup_agreetocodeofethicsandconduct"); // also tick code of ethics
        submitForm();
    }

    // checking for error message
    @Then("I should get an error message indicating that the terms and conditions are not accepted")
    public void iShouldGetAnErrorMessageIndicatingThatTheTermsAndConditionsAreNotAccepted() {
        verifyValidationError("You must confirm that you have read and accepted our Terms and Conditions");
    }

    // after the test if browser is open then it closes
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
