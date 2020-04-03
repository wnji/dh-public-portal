package com.blkchainsolutions.platform.service.vo;

import lombok.Data;

@Data
public class IPFS {

	private String hash;
	private String filename;
	private String size;
	
	public IPFS(String filename, String hash, String size) {
		this.hash=hash;
		this.filename=filename;
		this.size=size;
	}
}
