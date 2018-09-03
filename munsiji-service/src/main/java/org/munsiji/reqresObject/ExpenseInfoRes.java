package org.munsiji.reqresObject;

import java.util.ArrayList;
import java.util.List;

public class ExpenseInfoRes implements GenericResponse{
  ColTitle colTitle;
  List<String> colKey;
  String accType;
  List<AccountContent> content = new ArrayList();;
public ColTitle getColTitle() {
	return colTitle;
}
public void setColTitle(ColTitle colTitle) {
	this.colTitle = colTitle;
}
public List<String> getColKey() {
	return colKey;
}
public void setColKey(List<String> colKey) {
	this.colKey = colKey;
}

public String getAccType() {
	return accType;
}
public void setAccType(String accType) {
	this.accType = accType;
}
public List<AccountContent> getContent() {
	return content;
}
public void setContent(List<AccountContent> content) {
	this.content = content;
}

  
 
}
