package org.munsiji.reqresObject;

import java.util.List;

public class AccountContent {
	Header header;
	List<AccExpenseData>  data;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public List<AccExpenseData> getData() {
		return data;
	}
	public void setData(List<AccExpenseData> data) {
		this.data = data;
	}
	

}
