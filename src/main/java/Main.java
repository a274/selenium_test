
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class Main {
    public static WebDriver driver;

    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "D:\\учебка\\tests\\pr4\\msedgedriver.exe");
        driver = new EdgeDriver();
        final String printedMessage = "JAVA";
        final String editedMessage = "hi";

        login();
        printMsg(printedMessage);
        editMsg(editedMessage);
        searchMessage(printedMessage);
    }

    public static void login() {
        driver.get("https://vk.com/login");
        WebElement username = driver.findElement(By.xpath("//input[@id=\"email\"]"));
        WebElement password = driver.findElement(By.xpath("//input[@id=\"pass\"]"));
        WebElement login = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        username.sendKeys("example@mail.com");
        password.sendKeys("***");
        login.click();
        String expectedUrl = "https://vk.com/login";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(expectedUrl, actualUrl);
        System.out.println("TEST PASSED: login");
    }

    public static void printMsg(String expectedMessage) {
        driver.get("https://vk.com/im");
        driver.get("https://vk.com/im?peers=c97&sel=235696113");
        WebElement input = driver.findElement(By.xpath("//div[@role=\"textbox\"]"));
        input.sendKeys(expectedMessage);
        WebElement send = driver.findElement(By.xpath("//button[contains(@class, \"_im_send\")]"));
        send.click();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement message = driver.findElement(By.xpath(String.format("//div[contains(text(), \"%s\")]", expectedMessage)));
        String actualMessage = message.getText();
        Assert.assertEquals(expectedMessage, actualMessage);
        System.out.println("TEST PASSED: print message");
    }

    public static void editMsg(String editedMessage) {
        try {
            //путь до последнего edit
            WebElement edit = driver.findElement(By.xpath("//div[contains(@class, \"_im_peer_history im-page-chat-contain\")]/div[last()]/div[@class=\"im-mess-stack--content\"]/ul[last()]/li[last()]/div[@class=\"im-mess--actions\"]/span[@aria-label=\"Редактировать\"]"));
            edit.click();
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            WebElement edit = driver.findElement(By.xpath("//div[contains(@class, \"_im_peer_history im-page-chat-contain\")]/div[last()]/div[@class=\"im-mess-stack--content\"]/ul[last()]/li[last()]/div[@class=\"im-mess--actions\"]/span[@aria-label=\"Редактировать\"]"));
            edit.click();
        }
        WebElement input = driver.findElement(By.xpath("//div[@role=\"textbox\"]"));
        input.clear();
        input.sendKeys(editedMessage);
        WebElement send_edit = driver.findElement(By.xpath("//button[@aria-label=\"Редактировать\"]"));
        send_edit.click();
        WebElement edit_msg = driver.findElement(By.xpath(String.format("//div[contains(text(), \"%s\")]", editedMessage)));
        String actualMessage = (edit_msg.getText().split(" "))[0];
        Assert.assertEquals(editedMessage, actualMessage);
        System.out.println("TEST PASSED: edit message");
    }

    public static void searchMessage(String printedMessage) {
        WebElement search_btn = driver.findElement(By.xpath("//*[@aria-label=\"Поиск по беседе\"]"));
        search_btn.click();
        WebElement search_line = driver.findElement(By.xpath("//input[@id=\"im_history_search\"]"));
        //сообщение, которое мы ищем
        search_line.sendKeys(printedMessage);
        WebElement button = driver.findElement(By.xpath("//button[contains(@class, \"FlatButton--primary\")]"));
        button.click();
        WebElement last_msg = driver.findElement(By.xpath("//div[contains(@class, \"_im_peer_history \")]/div[contains(@class, \"im-mess-stack\")][last()]/div[@class=\"im-mess-stack--content\"]/ul[last()]/li[last()]/div[@class=\"im-mess--text wall_module _im_log_body\"]"));
        //сообщение, которые мы нашли
        String actualMessage = (last_msg.getText().split(" "))[0];
        try {
            Assert.assertEquals(actualMessage, printedMessage);
            System.out.println("TEST PASSED: find message");
        } catch (java.lang.AssertionError e) {
            System.err.println("TEST FAILED: wrong message found");
            System.err.println(e.getMessage());
        }
    }
}
