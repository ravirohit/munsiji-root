package org.munsiji.reqresObject;

public class TestEnumReq implements GenericResponse{
	EnumTest enumObj;
	String status;
	String restUrl;
	String reqId;
	public EnumTest getEnumObj() {
		return enumObj;
	}
	public void setEnumObj(EnumTest enumObj) {
		this.enumObj = enumObj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRestUrl() {
		return restUrl;
	}
	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	
	

}
