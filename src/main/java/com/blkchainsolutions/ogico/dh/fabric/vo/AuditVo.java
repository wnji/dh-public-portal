package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

@Data
public class AuditVo {
	
	private AuditData audit = new AuditData();
	
	@Data
	public class AuditData {

		private String auditUserID;
		private String auditUsername;
		private String auditType;
		private String auditDate;
		private String refT10UUID;
		private String refApprovalUUID;
		private String auditOrg;
	 }	
	
}
