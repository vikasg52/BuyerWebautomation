package com.makaan.Middleware;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.makaan.Dictionary.Home;
import com.makaan.Utilities.ConnectDB;
import com.makaan.Utilities.Xls_Reader;
import com.makaan.Webhelper.Webhelper;

public class HomeMiddleware {

	public static Webhelper wb = null;
	public static Home dict = null;
	static WebDriver driver = null;

	public static ConnectDB db = null;
	public static Xls_Reader xls = new Xls_Reader("Files/Marketplace.xls");

	public HomeMiddleware() {
		wb = new Webhelper();

		db = new ConnectDB();

		System.out.println("inside Middleware Constructor");

	}

	public void OpenURL() throws Exception {

		String URL = ReadSheet("Login", "URL", 2);
		wb.InitiateDriver();
		System.out.println("Opening URL through Middleware");
		wb.GetURL(URL);
		wb.WaitUntillVisiblility(dict.MakaanLogo);
		//Thread.sleep(2000);
		// closechat();

	}

	public static String ReadSheet(String Sheet, String Col_Name, int row_id)
			throws IOException, NoSuchElementException, TimeoutException {

		String data = xls.getCellData(Sheet, Col_Name, row_id);
		System.out.println("Data from sheet " + data);

		return (data);
	}

	public boolean VerifyAppButton() throws Exception {
		System.out.println("Inside test Validate Download button");
		wb.WaitUntill(dict.DownloadApp);
		if (wb.IsElementPresent(dict.DownloadApp)) {
			System.out.println("download app button is present");
			 if(SwitchWindow(dict.DownloadApp,dict.GooglePlayLogo,"app")){
				 System.out.println("Dowload App is verified successfully");
			 }else{
				 System.err.println("Download app was not verified successfully");
				 return false;
			 }
		 } else {
			System.out.println("Download app button was not present");
			return false;
		}
		return true;

	}

	public boolean VerifySeller() throws Exception {
		System.out.println("Inside test Verify Seller Link");
		wb.WaitUntill(dict.SellerLink);
		if (wb.IsElementPresent(dict.SellerLink)) {
			System.out.println("seller link is present");
			wb.ClickbyXpath(dict.SellerLink);
			//Thread.sleep(1000);
			wb.WaitUntillVisiblility(dict.LoginPop);
			if (wb.IsElementPresent(dict.LoginPop)) {
				System.out.println("Login popup is open to connect with seller link");
			} else {
				System.err.println("Login popup was not present on seller link button");
				return false;
			}
		} else {
			System.err.println("Seller link was not present");
			return false;
		}
		wb.WaitUntill(dict.Closepopup);
		wb.ClickbyXpath(dict.Closepopup);
		return true;

	}

	public boolean VerifyBuyerJourney() throws Exception {
		System.out.println("Inside test Verify Buyer Journey Link");
		Thread.sleep(1000);
		wb.WaitUntill(dict.Buyerjourney);
		if (wb.IsElementPresent(dict.Buyerjourney)) {
			System.out.println("Buyer Journey link is present");
			if (NewTab(dict.Buyerjourney, dict.ValidateBuyerJourney, "journey")) {
				System.out.println("Buyer Journey button was verified");
			} else {
				System.err.println("Buyer journey button was not successfullt verified");
				return false;
			}
		} else {
			System.out.println("Buyer Journey link was not present");
			return false;
		}
		return true;

	}

	public boolean VerifyMenuDrawerBuyerJourney() throws Exception {
		System.out.println("Inside test Verify Menu Drawer BuyerJourney Link");
		if (wb.IsElementPresent(dict.MenuDrawer)) {
			System.out.println("Menu Drawer is present");
			wb.ClickbyXpath(dict.MenuDrawer);
			//Thread.sleep(2000);
			if (wb.IsElementPresent(dict.MenuDrawerJourney)) {
				System.out.println("Buyer Journey in drawer is present");
				if (NewTab(dict.MenuDrawerJourney, dict.ValidateBuyerJourney, "journey")) {
					System.out.println("Buyer Journey button was verified in menu drawer");
				} else {
					System.err.println("Buyer journey button was not successfully verified in menu drawer");
					return false;
				}
			} else {
				System.err.println("Buyer Journey link was not present");
				return false;
			}
		}
		return true;
	}

	public boolean VerifyMenuDrawerCities() throws Exception {
		System.out.println("Inside Validate top cities in drawer");
		wb.IsElementPresent(dict.MenuDrawer);
		wb.ClickbyXpath(dict.MenuDrawer);
		int count = 0;
		String Element = null;
		//Thread.sleep(2000);

		if (wb.IsElementPresent(dict.TopCities)) {
			System.out.println("Link in drawer is present");
			wb.ClickbyXpath(dict.TopCities);
			//Thread.sleep(2000);
			for (int i = 1; i < 12; i++) {
				String Path = dict.TopCityGeneric.concat(Integer.toString(i)).concat("]/a");
				String City = wb.getText(dict.TopCityGeneric.concat(Integer.toString(i)).concat("]/a/span"));
				if(City.contains("all")){
					City = "all";
					Element = dict.allCity;
				}
				else{
				City = City.substring(12).toLowerCase();
				Element = dict.CityCover;
				}
				if (NewTab(Path, Element, City)) {
					System.out.println(City + " is verified in menu drawer");
				} else {
					System.err.println(City + " is not verified in menu drawer");
					count++;
				}
				wb.ClickbyXpath(dict.MenuDrawer);
				//Thread.sleep(1000);
			}
		
		} else {
			System.err.println("Top City link was not present in drawer");
			return false;
		}

		if (count > 0) {
			System.err.println("One or more city link was not verified due to error ");
			return false;
		}
		return true;
	}
	
	public boolean VerifyMenuDrawerBuilder() throws Exception {
		System.out.println("Inside Validate top Builder in drawer");
		int count = 0;
		String Element = null;
		//Thread.sleep(2000);

		if (wb.IsElementPresent(dict.TopBuilder)) {
			System.out.println("Link in drawer is present");
			wb.ClickbyXpath(dict.TopBuilder);
			//Thread.sleep(2000);
			for (int i = 1; i < 12; i++) {
				String Path = dict.TopBuilderGeneric.concat(Integer.toString(i)).concat("]/a");
				String Builder = wb.getText(dict.TopBuilderGeneric.concat(Integer.toString(i)).concat("]/a/span"));
				if(Builder.contains("all")){
					Builder = "all";
					Element = dict.allBuilders;
				}
				else{
					for (String retval : Builder.split(" ")) {
						System.out.println(retval);
						Builder = retval;
						break;
					} 
				Element = dict.BuilderCover;
				}
				if (NewTab(Path, Element, Builder)) {
					System.out.println(Builder + " is verified in menu drawer");
				} else {
					System.err.println(Builder + " is not verified in menu drawer");
					count++;
				}
				wb.ClickbyXpath(dict.MenuDrawer);
				//Thread.sleep(1000);
			}
		
		} else {
			System.err.println("Top Builder link was not present in drawer");
			return false;
		}

		if (count > 0) {
			System.err.println("One or more builder link was not verified due to error ");
			return false;
		}
		return true;
	}
	
	public boolean VerifyMenuDrawerBrokers() throws Exception {
		System.out.println("Inside Validate top Brokers in drawer");
		int count = 0;
		String Element = null;
		//Thread.sleep(2000);

		if (wb.IsElementPresent(dict.TopBrokers)) {
			System.out.println("Link of brokers in drawer is present");
			wb.ClickbyXpath(dict.TopBrokers);
			Thread.sleep(1000);
			for (int i = 1; i < 12; i++) {
				String Path = dict.TopBrokerGeneric.concat(Integer.toString(i)).concat("]/a");
				String Broker = wb.getText(dict.TopBrokerGeneric.concat(Integer.toString(i)).concat("]/a/span"));
				if(Broker.contains("all")){
					Broker = "all";
					Element = dict.allBrokers;
				}
				else{
					for (String retval : Broker.split(" ")) {
						System.out.println(retval);
						Broker = retval;
						break;
					} 
				Element = dict.BrokerCover;
				}
				if (NewTab(Path, Element, Broker)) {
					System.out.println(Broker + " is verified in menu drawer");
				} else {
					System.err.println(Broker + " is not verified in menu drawer");
					count++;
				}
				wb.ClickbyXpath(dict.MenuDrawer);
				//Thread.sleep(1000);
				wb.ClickbyXpath(dict.TopBrokers);
				closechat();
			}
		
		} else {
			System.err.println("Top Broker link was not present in drawer");
			return false;
		}

		if (count > 0) {
			System.err.println("One or more broker link was not verified due to error ");
			return false;
		}
		return true;
	}

	

	 public boolean VerifyMenuDrawerMakaanIQ() throws Exception {
		 System.out.println("Valiadting Makaan IQ in Drawer");
		 
		 if(wb.IsElementPresent(dict.MakaanIQ)) { 
			 System.out.println("MakaanIQ in drawer is present"); 
			 if(SwitchWindow(dict.MakaanIQ,dict.VerifyMakaanIQ,"makaaniq")){
				 System.out.println("Makaan IQ is verified successfully");
			 }else{
				 System.err.println("Makaan IQ was not verified successfully");
				 return false;
			 }
			return true;
		 }
		 else {
				System.err.println("Makaan IQ link was not present in drawer");
				return false;
			}
	 } 
		
		 public boolean VerifyMenuDrawerApp() throws Exception {
			 System.out.println("Valiadting APP in Drawer");
			 if(wb.IsElementPresent(dict.MenuDrawerApp)) { 
				 System.out.println("App in drawer is present"); 
				 if(SwitchWindow(dict.MenuDrawerApp,dict.GooglePlayLogo,"app")){
					 System.out.println("Dowload App is verified successfully");
				 }else{
					 System.err.println("Download app was not verified successfully");
					 return false;
				 }
				return true;
			 }
			 else{
				 System.err.println("Download app link was not present in drawer");
					return false;
			 }
		 } 
	

	public static void CloseAll() {
		db.Close();
		wb.CloseBrowser();
	}

	public static void closechat() throws Exception {
		if (wb.IsElementPresentById("inner-wrapper")) {
			wb.ClickbyXpath(".//textarea[@id='input']");
			wb.ClickbyXpath(".//div[@class='cross']");
			System.out.println("Closed mchat popup");
		} else {
			System.out.println("mchat popup was not present");
		}
	}

	public boolean NewTab(String Path, String Element, String Value) throws Exception {
		//Thread.sleep(2000);
		wb.ClickbyXpath(Path);
		wb.WaitUntill(Element);
		//Thread.sleep(4000);
		String URL = wb.CurrentURL();
		//Thread.sleep(1000);
		if (URL.contains(Value)) {
			wb.WaitUntillVisiblility(Element);
			if (wb.IsElementPresent(Element)) {
				System.out.println("Element is present on page");
			} else {
				System.err.println("Element was not present in URL " + URL);
				wb.Back();
				return false;
			}
		} else {
			System.err.println("URL did not contain desired value " + URL + "And Value is "+ Value);
			wb.Back();
			return false;
		}
		System.out.println("Current URL found is: " + URL);
		wb.Back();
		return true;
	}

	public boolean SwitchWindow(String Path,String Element, String Value) throws Exception {
		driver = wb.getDriver();
		String parentHandle = driver.getWindowHandle();
		wb.WaitUntillVisiblility(Path);
		driver.findElement(By.xpath(Path)).click();
		Thread.sleep(2000);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		String URL = driver.getCurrentUrl();
		System.out.println("URL of opened window is " + URL);
		if (URL.contains(Value)) {
			wb.WaitUntillVisiblility(Element);
			if (wb.IsElementPresent(Element)) {
				System.out.println("Element is present on page");
			} else {
				System.err.println("Element was not present in URL " + URL);
				driver.close();
				driver.switchTo().window(parentHandle);
				return false;
			}
		} else {
			System.err.println("URL did not contain desired value " + URL + "Value is " + Value);
			driver.close();
			driver.switchTo().window(parentHandle);
			return false;
		}
		driver.close();
		driver.switchTo().window(parentHandle);
		return true;
	}

	public boolean DownloadAppSection() throws Exception {
		wb.PageRefresh();
		//Thread.sleep(2000);
		wb.scrolldown(dict.DownloadAppSection);
		//Thread.sleep(2000);
		wb.enterTextByxpath(dict.Inputnumber, "9212746345");
		//Thread.sleep(2000);
		wb.ClickbyXpath(dict.GetApp);
		//Thread.sleep(2000);
		if (wb.getText(dict.ErrorApp).contains("successfull")) {
			System.out.println("App link send successfully sent");

		} else {
			System.err.println("app link was not successfully sent");
			return false;
		}
		return true;
	}

}
