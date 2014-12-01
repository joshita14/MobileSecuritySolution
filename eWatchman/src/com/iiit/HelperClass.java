package com.iiit;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HelperClass {

	/* Delete Data from the folder */
	public static void main(String args[]) throws Exception {
		String currentDir = null;
		String pathToFolderInLocal = "."
				+ "\\WebContent\\Babies\\";
		HelperClass c = new HelperClass();
		File f = new File(pathToFolderInLocal);
		c.parseDate("01-03-2014 10:00");
	}
	
	public static Date parseDate(String dateInString){
	
	//  System.out.println(dateInString);
		Date date =null;
      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	  String s[] = dateInString.split("-");
//	  Date date = new Date(Integer.parseInt(s[2].substring(0, s[2].indexOf(" "))),
//			  Integer.parseInt(s[1])-1,
//			  Integer.parseInt(s[0]),
//			  Integer.parseInt(s[2].substring(s[2].indexOf(" ") + 1, s[2].indexOf(":"))) ,
//			  Integer.parseInt(s[2].substring(s[2].lastIndexOf(":")+1)));
		System.out.println(date);
//		
		return date;
		
	}

	
	public static void delete(File file) throws IOException {

		if (file != null) {
			// list all the directory contents
			String files[] = file.list();

			for (String temp : files) {
				// construct the file structure
				File fileDelete = new File(file, temp);

				// recursive delete
				fileDelete.delete();
			}
		}
	}
	
	
	public static List<String> listFilesForFolder( File folder) {
	    List<String> fileNames = new LinkedList<String>();
		for (final File fileEntry : folder.listFiles()) {
	        
	    	fileNames.add(fileEntry.getAbsolutePath());   
	    }
		return fileNames;
	}
	
}
