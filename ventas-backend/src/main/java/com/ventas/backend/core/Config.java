package com.ventas.backend.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;


public class Config {
	
	public static String MYSQL_PATH = "mysql_path";
	public static String USER = "user";
	public static String PASSWORD = "password";
	public static String APP_PASSWORD = "app-password";
	public static String CONTADOR = "contador";
	public static String PRINT_SERVER_PORT = "print-server-port";


	private static Config config = null;
	
	public static String baseDirectory = "";//new File("").getAbsolutePath();//+File.separator+"src"+File.separator+"main"+File.separator+"java";
	
	//VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()
	
	private Properties properties = new Properties();
	
	private String propertiesFileName = baseDirectory+File.separator+"config"+File.separator+"config.properties";
	
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	private Config() {
		try {
			
			this.inputStream = new FileInputStream(new File(this.propertiesFileName));
			this.properties.load(inputStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getProperti(String properti) {
		return this.properties.getProperty(properti);
	}
	
	public void setProperti(String properti, String value) {
		try {
			this.outputStream = new FileOutputStream(new File(this.propertiesFileName));
			this.properties.setProperty(properti, value);
			this.properties.store(this.outputStream, "");
			this.outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Config getInstance() {
		if(config == null) {
			config = new Config();
		}
		return config;
	}
}
