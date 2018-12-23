package com.plefs.backend;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentDateTime {
	
	public static  String currentdtime(){
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
    LocalDate localDate = LocalDate.now();
    LocalTime localtime = LocalTime.now();
    String Str_Date= dtf.format(localDate).replace("/", "_");
    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss");
    String Str_Time= dtf1.format(localtime).replace(":", "");
    String DateTime = Str_Date +"_"+ Str_Time;
    return DateTime;
    //System.out.println(Str_Date);
    //System.out.println(Str_Time);
	}
	
	public static void main(String[] args){
		String s = currentdtime();
		System.out.println(s);
	}

}
