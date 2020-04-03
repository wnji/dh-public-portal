package com.blkchainsolutions.ogico.dh.pub.dto;

import lombok.Data;

@Data
public class EmailDTO {

	private String to;
	private String title;
	private String content;
	private String qrCodePath;
	private String ipdCert;
	private String bccList;
}	
