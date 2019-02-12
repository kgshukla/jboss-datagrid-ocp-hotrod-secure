package com.example.hotrod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotrod.service.HotrodService;

import org.infinispan.client.hotrod.RemoteCache;

@RestController
public class HotrodController {

	@Value("${HOSTNAME:local}")
	private String podName; 

  private String cachename = "mycache";

	@Autowired
	HotrodService hotrodService;
	
	RemoteCache<String, Object> cache;
	
	@RequestMapping(value = "/put/{id}", method = RequestMethod.GET)
	public String put (@PathVariable("id") long id) {
		initializeCache();
		cache.put(String.valueOf(id), "Value is "+String.valueOf(id));
		return "Successfully inserted!";
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public String get (@PathVariable("id") long id) {
		initializeCache();
		String value = (String)cache.get(String.valueOf(id));
		String mesg = "Value: "+value+"; Hello from pod - " + podName + "!";
		return value;
	}
	
	private void initializeCache() {
		if (cache == null) {
			cache = hotrodService.getCacheManager().getCache(cachename);
		} 	
  }
}
