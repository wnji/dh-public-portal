package com.blkchainsolutions.common;

import lombok.Data;

@Data
public final class EmailContent {

	public final static String titleProcessing = "Processing Transfer Trade Mark Request";
	public final static String contentProcessing = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Your Trademark Transfer Request"
			+ "<b> (" + "#1" + ") </b>"
			+ "is recorded into the system and pending for processing. Enclosed please find the QR code for proof of request."
			+ "<br>"
			+ "<br>"
			+ "<a href='" + "#2" + "'>Click here to open</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks for your attention."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";

	public final static String titleConfirmed = "Confirm of Trademark Request";
	public final static String contentConfirmed = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Your Trademark Transfer Request"
			+ "<b> (" + "#1" + ") </b>"
			+ "is confirmed. Enclosed please find the QR Code And Proof of Trademark Certificate."
			+ "<br>"
			+ "<br>"
			+ "<a href='" + "#2" + "'>Click here to open</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks for your attention."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";

	public final static String titleInitiated = "Transfer of your trademark has been recorded";
	public final static String contentInitiated = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "A new transfer of your Trademark "
			+ "<b> (" + "#1" + ") </b>"
			+ "has been recorded."
			+ "<br>"
			+ "<br>"
			+ "<a href='" + "#2" + "'>Click here to open</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks for your attention."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";

	public final static String titleFinished = "Transfer of your trademark had been finished";
	public final static String contentFinished = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "A transfer of your Trademark"
			+ "<b> (" + "#1" + ") </b>"
			+ "is finished."
			+ "<br>"
			+ "<br>"
			+ "<a href='" + "#2" + "'>Click here to open</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks for your attention."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";

	public final static String titleRegister = "Activation of HCP Portal's Account";
	public final static String contentRegister = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "To confirm your account, please click the link below to complete the activation:"
			+ "<br>"
			+ "<a href='" + "#1" + "'>Click Here To Activate</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks and Regards,"
			+ "<br>"
			+ "Intellectual Property Department (HCP)"
			+ "<br>"
			+ "<br>";

	public final static String titleForgetPassword = "Forget password of Your Transfer of Trademark Ownership's Account";
	public final static String contentForgetPassword = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Please click the link below to change your password within <b>10mins</b>"
			+ "<br>"
			+ "<a href='" + "#1" + "'>Click here to change your password</a>"
			+ "<br>"
			+ "<br>"
			+ "Thanks for your attention."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";

	public final static String titleSubsNotification = "Newsletter of Trademark Transfer";
	public final static String contentSubsNotificationd = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Please check the change(s) of the following trademark(s)."
			+ "<br>"
			+ "<br>"
			+ "#1"
			+ "Thanks for your subscription."
			+ "<br>"
			+ "<br>"
			+ "HCP Officer"
			+ "<br>"
			+ "<br>";
	public final static String titleActivate = "Your account registered with HCP has been approved";
	public final static String contentActivate = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Welcome to join this website. Your registered HCP account has been approved. "
			+ "<br>"
			+ "<br>"
			+ "Now you can click <a href='" + "#1" + "'>Health Care Professional </a> to log in to the system for relevant operations"
			+ "<br>"
			+ "<br>";
	public final static String titleNotActivate = "Your account number for HCP registration is not approved";
	public final static String contentNotActivate = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Sorry, the account you registered is not approved. Please contact the administrator or re register.";

	public final static String titleSubscribe = "Hong Kong Department of health";
	public final static String contentSubscribe = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "You have successfully subscribed to HKRegistrationID 《" + "#1" + "》, and you will receive product updates";
	public final static String contentNotSubscribe = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "You have successfully unsubscribed from product 《" + "#1" + "》, you will not receive product updates";

	public final static String contentOverdue = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "Your subscription 《" + "#1" + "》 will expire in " + "#2" + " days";

	public final static String contentExpiryAlert = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "You have successfully set subscription " + "#1" + " alert for " + "#2" + " days";
	public final static String contentSubscribeBatch = "Dear Sir/Madam"
			+ "<br>"
			+ "<br>"
			+ "You have successfully subscribed to Batch 《" + "#1" + "》, and you will receive product updates";
}