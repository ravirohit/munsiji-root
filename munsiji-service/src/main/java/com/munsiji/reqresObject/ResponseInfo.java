package com.munsiji.reqresObject;

public class ResponseInfo {
	String status;
	Integer statusCode;
	String msg;
	String reason;
	GenericResponse data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public GenericResponse getData() {
		return data;
	}
	public void setData(GenericResponse data) {
		this.data = data;
	}
    
}
