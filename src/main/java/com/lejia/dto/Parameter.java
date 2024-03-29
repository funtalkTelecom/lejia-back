package com.lejia.dto;

/**
 * 参数键值对
 * @author zjc
 *
 */
public class Parameter {

	private String key;
	private String value;
	
	public Parameter(){
		
	}

	public Parameter(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
