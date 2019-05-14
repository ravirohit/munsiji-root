package com.munsiji.commonUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class ServerSentEventUtil {
  static Map<String,SseEmitter> seeEmitterMap = new HashMap<>();
  public static SseEmitter getEmitter(String key){
	  return seeEmitterMap.get(key);
  }
  public static void setEmitter(String key, SseEmitter sseEmitter){
	   seeEmitterMap.put(key, sseEmitter);
  }
  public static void removeEmitter(String key){
	  seeEmitterMap.remove(key);
  }
  
}
