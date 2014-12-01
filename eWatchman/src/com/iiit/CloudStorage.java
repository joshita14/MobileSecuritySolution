package com.iiit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

/**
 * Simple wrapper around the Google Cloud Storage API
 */
public class CloudStorage {

	private static Properties properties;
	private static Storage storage;
	//notasecret
	private static final String PROJECT_ID_PROPERTY = "PROJECT_ID_PROPERTY";
	private static final String APPLICATION_NAME_PROPERTY = "APPLICATION_NAME_PROPERTY";
	private static final String ACCOUNT_ID_PROPERTY = "ACCOUNT_ID_PROPERTY";
	private static final String PRIVATE_KEY_PATH_PROPERTY = "PRIVATE_KEY_PATH_PROPERTY";
	private static final String HTTP_PROXY = "HTTP_PROXY";
	private static final String HTTP_PORT = "HTTP_PORT";
	
	public static void downloadFile(String bucketName, String oldfileName, String newfileName,String destinationDirectory) throws Exception {
		
		File directory = new File(destinationDirectory);
		if(!directory.isDirectory()) {
			throw new Exception("Provided destinationDirectory path is not a directory");
		}
		File file = new File(directory.getAbsolutePath() + "/" + newfileName);
		
		Storage storage = getStorage();
		
		Storage.Objects.Get get = storage.objects().get(bucketName, oldfileName);
		FileOutputStream stream = new FileOutputStream(file);
		try {
			get.executeMediaAndDownloadTo(stream);
		} finally {
			stream.close();
		}
	}
	
	/**
	 * Lists the objects in a bucket
	 * 
	 * @param bucketName bucket name to list
	 * @return Array of object names
	 * @throws Exception
	 */
	public static List<String> listBucket(String bucketName) throws Exception {
		
		Storage storage = getStorage();
		
		List<String> list = new ArrayList<String>();
		
		List<StorageObject> objects = storage.objects().list(bucketName).execute().getItems();
		if(objects != null) {
			for(StorageObject o : objects) {
				list.add(o.getName());
			}
		}
		
		return list;
	}
	
	/**
	 * List the buckets with the project
	 * (Project is configured in properties)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> listBuckets() throws Exception {
		
		Storage storage = getStorage();
		
		List<String> list = new ArrayList<String>();
		
		List<Bucket> buckets = storage.buckets().list(properties.getProperty(PROJECT_ID_PROPERTY)).execute().getItems();
		if(buckets != null) {
			for(Bucket b : buckets) {
				list.add(b.getName());
			}
		}
		
		return list;
	}
	
	private static Storage getStorage() throws Exception {

		if (storage == null) {
			getProperties();
			HttpTransport httpTransport;
			if(!properties.getProperty(HTTP_PROXY).isEmpty() && !properties.getProperty(HTTP_PORT).isEmpty())
			{
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(properties.getProperty(HTTP_PROXY), 
						Integer.parseInt(properties.getProperty(HTTP_PORT))));
				httpTransport = new NetHttpTransport.Builder().setProxy(proxy).build();
			}
			else
			{
				httpTransport = new NetHttpTransport();
			}
			JsonFactory jsonFactory = new JacksonFactory();

			List<String> scopes = new ArrayList<String>();
			scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);

			Credential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setServiceAccountId(properties.getProperty(ACCOUNT_ID_PROPERTY))
					.setServiceAccountPrivateKeyFromP12File(
							new File(properties.getProperty(PRIVATE_KEY_PATH_PROPERTY)))
					.setServiceAccountScopes(scopes).build();

			storage = new Storage.Builder(httpTransport, jsonFactory,
					credential).setApplicationName(properties.getProperty(APPLICATION_NAME_PROPERTY))
					.build();
		}

		return storage;
	}
	
	private static Properties getProperties() throws Exception {

		if (properties == null) {
			properties = new Properties();
			InputStream stream = null;
			try {
				stream =new FileInputStream(".\\cloud.properties");
				properties.load(stream);
				System.out.println(properties.getProperty(PRIVATE_KEY_PATH_PROPERTY));
			} catch (IOException e) {
				System.out.println("cloudstorage.properties must be present in classpath" + e.getMessage());
				throw new RuntimeException(
						"cloudstorage.properties must be present in classpath",
						e);
			} finally {
				stream.close();
			}
		}
		return properties;
	}
}