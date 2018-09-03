package org.munsiji.resourceController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {
	public static void main(String args[]) {
      System.out.println("Sum of x+y = ");
      Map<String, List<String>> map = new HashMap();
      System.out.println(map.get("name"));
      if(map.get("name") == null ){
    	  List<String> list = new ArrayList();
    	  list.add("new value");
       map.put("name", list);
      }
      System.out.println("map:"+map);
    }
}

class Test{
    public String name;
    private String rol;
    String address;
    String phone;
    public void abc(){
    	
    }
    public void dis(){
    	
    }
    
    
}

	