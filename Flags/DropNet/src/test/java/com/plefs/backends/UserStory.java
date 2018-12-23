package com.plefs.backends;

public class UserStory extends BaseClass{
	
	static String US_name=null;
	
	public static void setUS_name(String UserStoryname){
		US_name= UserStoryname;
		
	}

	public static String getUS_name(){
		return US_name;
	}
}
