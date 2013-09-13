package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

    public static void writeProperties(Map<String, String> properties, String cacheFileName) {
        StringBuilder cacheFilePath = new StringBuilder();
        
        cacheFilePath.append(System.getProperty("user.dir"));
        cacheFilePath.append(File.separator);
        cacheFilePath.append("temp");
        cacheFilePath.append(File.separator);
        cacheFilePath.append(cacheFileName);
        
        Properties props = new Properties();
        File file = new File(cacheFilePath.toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream input = new FileInputStream(file);
            props.load(input);
            props.putAll(properties);
            FileOutputStream output = new FileOutputStream(file);
            props.store(output, null);
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println("未找到多语资源缓存文件！");
        } catch (IOException e) {
            System.out.println("写入多语资源缓存文件失败！");
        }
    }

    public static Map<String, String> readProperties(String cacheFileName) {
        Map<String, String> properties = new HashMap<String, String>();
        StringBuilder cacheFilePath = new StringBuilder();
        
        cacheFilePath.append(System.getProperty("user.dir"));
        cacheFilePath.append(File.separator);
        cacheFilePath.append("temp");
        cacheFilePath.append(File.separator);
        cacheFilePath.append(cacheFileName);
        
        Properties props = new Properties();
        File file = new File(cacheFilePath.toString());
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream input = new FileInputStream(file);
            props.load(input);
            Enumeration<Object> keys = props.keys();
            String key;
            while(keys.hasMoreElements()){
                key = keys.nextElement().toString();
                properties.put(key, props.getProperty(key));
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("未找到多语资源缓存文件！");
        } catch (IOException e) {
            System.out.println("写入多语资源缓存文件失败！");
        }
        return properties;
    }
}
