package org.munsiji.reqresObject;

import java.util.List;

public class AccountContent {
	Header header;
	List<AccExpnseData>  data;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public List<AccExpnseData> getData() {
		return data;
	}
	public void setData(List<AccExpnseData> data) {
		this.data = data;
	}
	

}
