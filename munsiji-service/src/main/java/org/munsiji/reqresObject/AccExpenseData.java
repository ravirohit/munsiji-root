package org.munsiji.reqresObject;

public class AccExpenseData {
	String accName;
	Float amnt;
	String date;
	String desc;
	public AccExpenseData(String accName,Float amnt, String date, String desc){
		this.accName = accName;
		this.amnt = amnt;
		this.date = date;
		this.desc = desc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Float getAmnt() {
		return amnt;
	}
	public void setAmnt(Float amnt) {
		this.amnt = amnt;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}
