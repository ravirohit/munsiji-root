package com.munsiji.reqresObject;

public class AccExpenseData {
	String accName;
	Float amnt;
	String date;
	String desc;
	boolean status;
	public AccExpenseData(String accName,Float amnt, String date, String desc,boolean status){
		this.accName = accName;
		this.amnt = amnt;
		this.date = date;
		this.desc = desc;
		this.status = status;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	@Override
	public boolean equals(Object obj){
		System.out.println("comparing the accoExpense data:"+accName+"   (AccExpenseData) obj:"+(AccExpenseData) obj);
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if ((obj instanceof AccExpenseData) && (((AccExpenseData) obj).getAccName().equals(this.accName))) {
			System.out.println("true executed");
			return true;
		} else {
			return false;
		}
	}
	

}
