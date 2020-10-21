package com.srm.rta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:/properties/rta_picture_path.properties")
public class RTAPicturePath {
	
	@Value("${widgetFilePath}")
	private String widgetFilePath;
	
	
	@Value("${requestAttachmentPath}")
	private String requestAttachmentPath;
	
	
	@Value("${phoneBookExcelAttachment}")
	private String phoneBookExcelAttachment;
	
	@Value("${externalLinkFilePath}")
	private String externalLinkFilePath;
	
	@Value("${userFilePath}")
	private String userFilePath;
	

	
	// DownloadPath
	@Value("${widgetDownloadPath}")
	private String widgetDownloadPath;

	@Value("${externalLinkDownloadPath}")
	private String externalLinkDownloadPath;

	@Value("${phoneBookDownloadPath}")
	private String phoneBookDownloadPath;

	@Value("${userDownloadPath}")
	private String userDownloadPath;
	
	
	@Value("${zipPicturePath}")
	private String zipPicturePath;


	/**
	 * @return the widgetFilePath
	 */
	public String getWidgetFilePath() {
		return widgetFilePath;
	}

	/**
	 * @param widgetFilePath the widgetFilePath to set
	 */
	public void setWidgetFilePath(String widgetFilePath) {
		this.widgetFilePath = widgetFilePath;
	}

	/**
	 * @return the externalLinkFilePath
	 */
	public String getExternalLinkFilePath() {
		return externalLinkFilePath;
	}

	/**
	 * @param externalLinkFilePath the externalLinkFilePath to set
	 */
	public void setExternalLinkFilePath(String externalLinkFilePath) {
		this.externalLinkFilePath = externalLinkFilePath;
	}

	/**
	 * @return the userFilePath
	 */
	public String getUserFilePath() {
		return userFilePath;
	}

	/**
	 * @param userFilePath the userFilePath to set
	 */
	public void setUserFilePath(String userFilePath) {
		this.userFilePath = userFilePath;
	}


	public String getWidgetDownloadPath() {
		return widgetDownloadPath;
	}

	public void setWidgetDownloadPath(String widgetDownloadPath) {
		this.widgetDownloadPath = widgetDownloadPath;
	}

	public String getExternalLinkDownloadPath() {
		return externalLinkDownloadPath;
	}

	public void setExternalLinkDownloadPath(String externalLinkDownloadPath) {
		this.externalLinkDownloadPath = externalLinkDownloadPath;
	}

	public String getPhoneBookDownloadPath() {
		return phoneBookDownloadPath;
	}

	public void setPhoneBookDownloadPath(String phoneBookDownloadPath) {
		this.phoneBookDownloadPath = phoneBookDownloadPath;
	}

	public String getUserDownloadPath() {
		return userDownloadPath;
	}

	public void setUserDownloadPath(String userDownloadPath) {
		this.userDownloadPath = userDownloadPath;
	}

	public String getRequestAttachmentPath() {
		return requestAttachmentPath;
	}

	public void setRequestAttachmentPath(String requestAttachmentPath) {
		this.requestAttachmentPath = requestAttachmentPath;
	}

	public String getPhoneBookExcelAttachment() {
		return phoneBookExcelAttachment;
	}

	public void setPhoneBookExcelAttachment(String phoneBookExcelAttachment) {
		this.phoneBookExcelAttachment = phoneBookExcelAttachment;
	}

	public String getZipPicturePath() {
		return zipPicturePath;
	}

	public void setZipPicturePath(String zipPicturePath) {
		this.zipPicturePath = zipPicturePath;
	}

	
	
	

}
