package com.athanazio.jira;

import java.util.HashMap;

public class JiraProject {
	
	public String id;
	public String self;
	public String key;
	public String name;
	public HashMap<String , String> avatarUrls;
	
	@Override
	public String toString() {
		return "JiraProject [id=" + id + ", self=" + self + ", key=" + key + ", name=" + name + ", avatarUrls="
				+ avatarUrls + "]";
	}

	
}
