package base;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetObjectInputStream;
import nc.monitor.servlet.RequestInfo;
import nc.monitor.servlet.URLCommuniatcor;
import nc.servlet.call.LogWriter;

public class GetFlux {

	public static String[] transServerLog(String startTime, String nmcurl) {
		String endTime = getTime(nmcurl);

		ObjectOutputStream outPut = null;
		NetObjectInputStream inputStream = null;
		Hashtable<String, Object> hm = new Hashtable<String, Object>();
		hm.put("servicename", "directlogquery");
		hm.put("methodname", "query");
		hm.put("parametertypes", new Class[] { String.class });
		String query = String
				.format(
						"select server/mwsummary from nclogs where ts>\"%s\" and ts<\"%s\"",
						startTime, endTime);
		hm.put("parameter", ((Object) (new Object[] { query })));
		String serverUrl = nmcurl;
		URL url;
		try {
			url = new URL(serverUrl);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			outPut = new ObjectOutputStream(con.getOutputStream());
			outPut.writeObject(hm);
			inputStream = new NetObjectInputStream(con.getInputStream());
			String str = (String) inputStream.readObject();
			inputStream.close();
			outPut.close();
			return tranLog(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] tranLog(String log) {
		String s[] = log.split("\n");
		int conCount = 0;
		int getFlux = 0;
		int postFlux = 0;
		for (int i = 0; i < s.length; i++) {
			if (s[i].indexOf(getIP()) != -1) {
				conCount++;
				String ss[] = s[i].split(";");
				for (int j = 0; j < ss.length; j++) {
					if (ss[j].trim().startsWith("writetoclientbytes")) {
						getFlux = getFlux
								+ Integer.valueOf(ss[j].split("=")[1]);
					}
					if (ss[j].trim().startsWith("readfromclientbytes")) {
						postFlux = postFlux
								+ Integer.valueOf(ss[j].split("=")[1]);
					}
				}
			}
		}
		return new String[]{String.valueOf(conCount),String.valueOf(getFlux),String.valueOf(postFlux)};
	}

	// »ñÈ¡IPµØÖ·
	public static String getIP() {
		InetAddress inet = null;
		try {
			inet = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inet.getHostAddress();
	}

	public static String getTime(String url) {
		RequestInfo info = new RequestInfo();
		info.setMethodName("getCurTs");
		info.setServiceName("loganalyze");
		Object curtime = URLCommuniatcor.remoteCall(info, url);
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(((Long) curtime).longValue()));
		return time;
	}
}
