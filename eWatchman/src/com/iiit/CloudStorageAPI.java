package com.iiit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CloudStorageAPI {

	@SuppressWarnings("deprecation")
	private static List<String> getFilesToDownload(String bucketName, Date date1) {
		
		List<String> fileNames = new LinkedList<String>();
		List<String> files;
		
		try {
			files = CloudStorage.listBucket(bucketName);
		} catch (Exception e) {
			return null;
		}
		
		for (String file : files) {
			
			if(file.endsWith(".jpeg")) {
				
				String [] f = file.substring(0, file.lastIndexOf('.')).split(":");
				if(f.length == 6) {
					
//					Date date2 = new Date(Integer.parseInt(f[0]),
//						Integer.parseInt(f[1]),
//						Integer.parseInt(f[2]),
//						Integer.parseInt(f[3]),
//						Integer.parseInt(f[4]),
//						Integer.parseInt(f[5]));
					String s = f[2] + "." + f[1] + "." + f[0] + " " + f[3] + ":" + f[4];
					Date date2 = HelperClass.parseDate(s);
					
					float diffHours = (float) ((date2.getTime() - date1.getTime()) / (60.0 * 60 * 1000));
					System.out.println(diffHours);
					if(diffHours >= 0.0 && diffHours <= 1.0) {
				    	System.out.println(file + " : " + diffHours);
				    	fileNames.add(file);
				    }
				}
			}
		}
		return fileNames;	
	}
	
	private static int downloadFiles(String bucketName, List<String> fileNames, String path) {
		
		int count =0;
		for(String file : fileNames) {
			try {
				CloudStorage.downloadFile(bucketName, file, file.replace(':', '-'), path);
				count++;
			} 
			catch (Exception e) {
			}			
		}
		return count;
	}
	
	public static List<String> getBuckets() throws Exception {
		
		try {
			return CloudStorage.listBuckets();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CloudStorage.listBuckets();
	}
	
	public static boolean getImages(String bucketName, Date date1, String path)
	{
		List<String> fileNames = getFilesToDownload(bucketName, date1);
		if(fileNames != null)
		{
			System.out.println(fileNames.size());
			downloadFiles(bucketName, fileNames, path);
			return true;
		}
		return false;
	}
	
	public static void main(String args[]) throws Exception
	{	
		//Sat Nov 29 20:00:00 IST 2014
		//getImages("ewatchman", HelperClass.parseDate("29.11.2014 20:00"), "C://");
        List<String> bucketNames = CloudStorageAPI.getBuckets();
//        System.out.println("hey " + bucketNames.size());
//        for(String bucknm : bucketNames){
//          System.out.println(" We are  herrerre !!!!!!!!!!!!!" + bucknm);
//        }
	}
}
