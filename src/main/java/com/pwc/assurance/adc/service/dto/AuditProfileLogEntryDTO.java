package com.pwc.assurance.adc.service.dto;

import java.io.Serializable;


/**
 * A DTO for the AuditQuestionResponse entity.
 */
public class AuditProfileLogEntryDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String description;
    private Long auditProfileId;
    private String userLogin;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getAuditProfileId() {
		return auditProfileId;
	}
	public void setAuditProfileId(Long auditProfileId) {
		this.auditProfileId = auditProfileId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
	@Override
	public String toString() {
		return "AuditProfileLogEntryDTO [id=" + id + ", description=" + description
				+ ", auditProfileId=" + auditProfileId + ", userLogin=" + userLogin + "]";
	}
    
}
