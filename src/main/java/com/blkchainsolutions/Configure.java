package com.blkchainsolutions;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Data
@Configuration
@EnableConfigurationProperties
@Service
@ConfigurationProperties("ogcio")
public class Configure {
	
	private String domainUrl;
	private String jmshost;
	private String chainUrl;
	private String ipfsDownload;
	private String publicIpfsDownload;
	private String portalName;
	private String emailPW;
	private String emailHost;
	private String emailUsername;
	private String ogcioEmailURL;
	private String ogcioEmailUsername;
	private String emailSignature;
	private Integer emailPort;
}
