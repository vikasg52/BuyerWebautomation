package com.makaan.Middleware;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jboss.netty.util.internal.SystemPropertyUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.makaan.Dictionary.ConnectNow;
import com.makaan.Utilities.ConnectDB;
import com.makaan.Utilities.ExcelOperation;
import com.makaan.Utilities.Xls_Reader;
import com.makaan.Webhelper.Webhelper;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ConnectNowMiddleware {

	public static Webhelper wb = null;
	public static ConnectNow dict = null;
	public static WebDriver driver = null;;
	public static ExcelOperation ex = null;
	public static ConnectDB db = null;
	public static Xls_Reader xls = new Xls_Reader("Files/Marketplace.xls");

	public ConnectNowMiddleware() {
		wb = new Webhelper();
		ex = new ExcelOperation();
		db = new ConnectDB();

		System.out.println("inside Middleware Constructor");

	}

	public void OpenURL() throws Exception {

		String URL = ReadSheet("CallNow", "URL", 2);
		wb.InitiateDriver();
		System.out.println("Opening URL through Middleware");
		wb.GetURL(URL);
		wb.WaitUntill(dict.MakaanLogo);

	}

	public static String ReadSheet(String Sheet, String Col_Name, int row_id)
			throws IOException, NoSuchElementException, TimeoutException {

		String data = xls.getCellData(Sheet, Col_Name, row_id);
		System.out.println("Data from sheet " + data);

		return (data);
	}

	public boolean ConnectNow() throws NoSuchElementException, Exception {
		String OTP = null;
		System.out.println("Inside Call Now Test");
		wb.WaitUntillVisiblility(dict.ConnectNowButton);
		wb.Jsclickbyxpath(dict.ConnectNowButton);
		System.out.println(" element has been clicked ");
		if (wb.IsElementPresentById("leadPopup")) {
			System.out.println("Lead popup is present");
			System.out.println("Selecting Country code for india");
			if (!(wb.IsElementPresent(dict.LeadDropDown))) {
				System.err.println("Country code dropdown of Connect now is not present");
			}
			Thread.sleep(1000);
			wb.WaitUntillVisiblility(dict.MobileNumberInput);
			wb.ClickbyXpath(dict.MobileNumberInput);
			String MobileNumber = ReadSheet("CallNow", "Phone Number", 2);

			wb.enterTextByxpath(dict.MobileNumberInput, MobileNumber);
			Thread.sleep(2000);
			wb.ClickbyXpath(dict.CallNowSubmit);
			Thread.sleep(1000);

			if (wb.getText(dict.Error).contains("error")) {
				System.err.println("some error occurred while submiting Call Now Lead");
				return false;
			}

			else if (wb.IsElementPresent(dict.OTP_Screen)) {
				System.out.println("OTP screen appeared on lead form");
				OTP = "4192";
				// OTP = GetOTP();
				wb.enterTextByxpath(dict.OTP_input, OTP);
				wb.WaitUntillVisiblility(dict.Emailinput);
				wb.enterTextByxpath(dict.Emailinput, ReadSheet("CallNow", "Email", 2));
				wb.WaitUntillVisiblility(dict.Nameinput);
				wb.enterTextByxpath(dict.Nameinput, ReadSheet("CallNow", "Name", 2));
				wb.WaitUntillVisiblility(dict.SubmitOTP);
				wb.ClickbyXpath(dict.SubmitOTP);
				Thread.sleep(2000);
				wb.WaitUntillVisiblility(dict.InvalidOTP);
				if (wb.getText(dict.InvalidOTP).equals("otp is invalid")) {
					System.out.println("Click on resend OTP");
					wb.ClickbyXpath(dict.resendOTP);
					try {
						if (wb.getText(dict.resendOTPMessage).equals("we have sent an otp")) {
							System.out.println("OTP is resent");
							OTP = GetOTP();
							System.out.println("OTP found "+OTP);
							Thread.sleep(1000);
							wb.clearbox(dict.OTP_input);
							Thread.sleep(1000);
							wb.enterTextByxpath(dict.OTP_input, OTP);
							wb.WaitUntillVisiblility(dict.SubmitOTP);
							wb.ClickbyXpath(dict.SubmitOTP);
							
						}
					} catch (Exception e) {
						System.err.println("OTP resend is failed");
					}
				} else if (wb.getText(dict.Error).contains("error")) {
					System.err.println("some error occurred while submiting OTP from Connect Now");
					return false;
				}
			}
				Thread.sleep(2000);
			if (wb.IsElementPresent(dict.TringCallNow)) {
				System.out.println("Lead form has successfully submitted");
				System.out.println("Now submitting Lead for Share my number");
				wb.WaitUntillVisiblility(dict.ShareMyNumber);
				wb.ClickbyXpath(dict.ShareMyNumber);
				wb.WaitUntillVisiblility(dict.Thank);
				if (wb.IsElementPresent(dict.Thank)) {
					Thread.sleep(2000);
					wb.Jsclickbyxpath(dict.ThanksSubmit);
					// wb.ClickbyXpath(dict.ThanksSubmit);
					
				}

				else if (wb.getText(dict.Error).contains("error")) {
					wb.ClickbyXpath(dict.Skip);
					wb.WaitUntillVisiblility(dict.ThanksSubmit);
					wb.ClickbyXpath(dict.ThanksSubmit);
					System.err.println("some error occurred while submiting Share My datails Lead");
					return false;
				}
			}

			return true;
		} else {
			System.err.println("Lead popup did not open");
			return false;
		}

	}

	public String GetOTP() throws Exception {
		String id = null;
		System.out.println("Inside verify OTP from DB");
		String Database = "use user ;";
		String Query = "select id from users where email = 'email_".concat(ReadSheet("CallNow", "Phone Number", 2))
				.concat("@email.com';");
		Thread.sleep(2000);// wait added to connect db after this time so that
							// table can be updated
		db.Connect();
		// String Phone = ReadSheet("CallNow", "Phone Number", 2);
		ResultSet rs = db.Execute(Query, Database);
		while (rs.next()) {
			id = rs.getString("ID");
			System.out.println("id " + id);
		}
		Query = "select otp from user.user_otps where user_id =".concat("'" + id + "'");
		rs = db.Execute(Query, Database);

		while (rs.next()) {
			id = rs.getString("OTP");
			System.out.println("OTP found is " + id);
		}
		return id;
	}
public boolean ViewPhoneNumber(String Type) throws Exception{
		if(ViewNumber(Type)){
			return true;
		}else{
			return false;
		}
	
}
	
	public boolean ViewNumber(String Type) throws Exception {
		System.out.println("Inside ViewPhone Test");
		if(Type.equals("Inner Lead")){
			wb.WaitUntillVisiblility(dict.ConnectNowButton);
			wb.Jsclickbyxpath(dict.ConnectNowButton);
		}
		else{
			wb.WaitUntillVisiblility(dict.ViewPhoneButton);
			wb.Jsclickbyxpath(dict.ViewPhoneButton);
		}
		if (wb.IsElementPresentById("leadPopup")) {
			System.out.println("Lead popup is present");
			if(Type.equals("Inner Lead")){
			wb.ClickbyXpath(dict.ViewPhone);
			}
				wb.WaitUntillVisiblility(dict.SellerDetails);
			if (wb.IsElementPresent(dict.SellerDetails)) {
				System.out.println("seller detail is displayed");
				if(wb.getText(dict.PhoneText).equals("na")){
					System.err.println("There is no number present on view phone");
				}
				else{
				System.out.println("Phone number of selller is: " + wb.getText(dict.PhoneText));
				System.out.println("Phone number was present on View phone");
				}
				wb.Jsclickbyxpath(dict.PhoneSubmit);
				// wb.ClickbyXpath(dict.PhoneSubmit);
			} else {
				System.err.println("Seller details card was not present");
				return false;
			}
		} else {
			System.err.println("Lead popup did not open");
			return false;
		}
		return true;

	}

	public boolean VerifyEnquiry() throws Exception {
		String Database = "use ptigercrm ;";
		String Query = "select * from LEAD_TMP_UPLOAD order by id desc limit 5";
		Thread.sleep(5000);
		db.Connect();
		String Phone = ReadSheet("CallNow", "Phone Number", 2);
		ResultSet rs = db.Execute(Query, Database);
		while (rs.next()) {
			String id = rs.getString("ID");
			if (rs.getString("MOBILE").equals(Phone)) {
				System.out.println("id " + id);
				System.out.println("Mobile number " + rs.getString("MOBILE"));
				return true;
			}
		}
		System.out.println("Can not find phone number in leads");
		return false;
	}

	public void CloseBrowser() {
		db.Close();
		wb.CloseBrowser();

	}

	public boolean ContactSellers() throws Exception {
		System.out.println("Inside Contact Seller Test");
		Thread.sleep(2000);
		wb.WaitUntillVisiblility(dict.ContactSellerCard);
		if (wb.IsElementPresent(dict.ContactSellerCard)) {
			wb.ClickbyXpath(dict.ContactSellerCard);
			wb.scrollup(dict.ThankContactSeller);
			Thread.sleep(2000);
			System.out.println("Text on page is " + wb.getText(dict.TitleContactSeller));
			Thread.sleep(2000);
			wb.ClickbyXpath(dict.InputPhone);
			Thread.sleep(2000);
			String PhoneNumber = ReadSheet("CallNow", "Phone Number", 2);
			wb.enterTextByxpath(dict.InputPhone, PhoneNumber);
			Thread.sleep(2000);
			wb.ClickbyXpath(dict.SubmitContactSeller);
			Thread.sleep(2000);

			if (wb.IsElementPresent(dict.ErrorSubmit)) {
				System.out.println("Some error occurred while contact seller");

			} else if (wb.IsElementPresent(dict.ThankContactSeller)) {
				System.out.println("Thanks Window displayed, enquiry submitted on UI");
				if (VerifyEnquiry()) {
					System.out.println("Enquiry was present in table");
					return true;
				} else {
					System.out.println("Not able to find lead in Enquiry table");
					return false;
				}
			}
			return false;

		} else {
			System.out.println("Contact Seller card was not present on SERP Page");
			return false;
		}

	}
}
