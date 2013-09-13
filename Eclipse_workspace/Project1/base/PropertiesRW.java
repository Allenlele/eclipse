package base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;


public class PropertiesRW {
	File file = null;

	public PropertiesRW(File file) {
		this.file = file;
	}
	
	/**
	 * 读取属性
	 */
	public String readValue(String key) {
		Properties props = new Properties();
		try {
			InputStream ips = new BufferedInputStream(new FileInputStream(this.file));
			props.load(ips);
			String value = props.getProperty(key);
			ips.close();
			return value;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 按指定的编码方式读取读取属性
	 * @param itemName
	 * @param charSet
	 * @return
	 */
	public String readModeValue(String itemName)
	{
	  try{
	       Properties pCharSet = new Properties(); 
	       InputStreamReader isr = new InputStreamReader(new FileInputStream(this.file));
	       pCharSet.load(isr);
	       isr.close();
	       return pCharSet.getProperty(itemName); 
	   }catch(Exception e){
		   e.printStackTrace();
	   }
       return null;
	}
	
	/**
	 * 写入属性
	 */
	public void writeProperties(String key, String paraValue) {
		Properties props = new Properties();
		try {
			InputStream ips = new BufferedInputStream(new FileInputStream(this.file));
			props.load(ips);
			OutputStream ops = new FileOutputStream(this.file);
			props.setProperty(key, paraValue);
			props.store(ops, "nctesttool");
			ips.close();
			ops.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
