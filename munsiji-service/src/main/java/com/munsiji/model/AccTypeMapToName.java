package com.munsiji.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.munsiji.reqresObject.GenericResponse;

public class AccTypeMapToName implements GenericResponse{
	Map<String,List<String>> accTypeToNameMap = null;

	public Map<String, List<String>> getAccountDetail() {
		if(accTypeToNameMap == null){
			accTypeToNameMap = new HashMap<String,List<String>>();
		}
		return accTypeToNameMap;
	}

	public void setAccountDetail(Map<String, List<String>> accountDetail) {
		this.accTypeToNameMap = accountDetail;
	}
	
	

}
