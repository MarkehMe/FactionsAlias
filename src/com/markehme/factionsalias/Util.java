package com.markehme.factionsalias;

import java.util.HashMap;

public class Util {
	public static HashMap<String, String> settings(String... data) {
		HashMap<String, String> ret = new HashMap<String, String>();
		
		String holding = null;
		
		for(String v : data) {
			if(holding != null) {
				ret.put(holding, v);
			} else {
				holding = v;
			}
		}
		
		return ret;
	}
}
