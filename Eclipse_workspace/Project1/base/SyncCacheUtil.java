package base;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SyncCacheUtil {

    public static void syncCache(String envLang) {
        PropertiesRW pp = new PropertiesRW(new File(System.getProperty("user.dir")
                + File.separator + "conf" + File.separator + "db.properties"));
        DBMng dbm = DBMng.getInstance(pp.readValue("user"), pp.readValue("pwd"),
                pp.readValue("ip"), pp.readValue("port"), pp.readValue("dbnm"));
        String cacheVersion = getCacheFileVersion("baseline.properties", envLang);
        String lastestVersion = getLastestVersion(dbm);
        boolean isSame = compareVersion(cacheVersion, lastestVersion);

        if(!isSame) {
            Map<String,String> compNmCache = new HashMap<String,String>();
            Map<String,String> baseLine = new HashMap<String, String>();
            String cacheFileNm = "";
            if("2".equals(envLang)) {
                cacheFileNm = "compNmsCache_EN.properties";
            } else if ("3".equals(envLang)) {
                cacheFileNm = "compNmsCache_FR.properties";
            } else if ("4".equals(envLang)) {
                cacheFileNm = "compNmsCache_TW.properties";
            }
            //update cache contents
            compNmCache = PropertiesUtil.readProperties(cacheFileNm);
            PropertiesUtil.writeProperties(getLastestTrans(compNmCache, dbm, envLang, lastestVersion), cacheFileNm);

            //update baseline contents
            baseLine.put(cacheFileNm, lastestVersion);
            PropertiesUtil.writeProperties(baseLine, "baseline.properties");
        }
    }

    public static  String getCacheFilePath(String envLang) {
        StringBuilder cacheFilePath = new StringBuilder();

        cacheFilePath.append(System.getProperty("user.dir"));
        cacheFilePath.append(File.separator);
        cacheFilePath.append("temp");
        cacheFilePath.append(File.separator);
        cacheFilePath.append("baseline.properties");

        return cacheFilePath.toString();
    }

    public static String getCacheFileVersion(String fileNm, String envLang) {
        String version = "";
        if ("2".equals(envLang)) {            
            version = PropertiesUtil.readProperties(fileNm).get("compNmsCache_EN.properties");
        } else if ("3".equals(envLang)) {
            version = PropertiesUtil.readProperties(fileNm).get("compNmsCache_FR.properties");
        } else if ("4".equals(envLang)) {
            version = PropertiesUtil.readProperties(fileNm).get("compNmsCache_TW.properties");
        }
        
        return version;
    }

    public static  boolean compareVersion(String cacheVer, String dbVersion) {
        return dbVersion.equals(cacheVer) ? true : false;
    }

    public static  String getLastestVersion(DBMng dbm) {
        String dbVersion = "";
        String sql = "SELECT LASTESTVER FROM LASTEST_VERSION";
        ResultSet rs = dbm.executeQuery(sql);
        try {
            while (rs.next()) {
                dbVersion = rs.getString(1);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbVersion;
    }

    public static  Map<String, String> getLastestTrans(
            Map<String, String> src, DBMng dbm, String envLang, String version) {
        Map<String, String> dest = new HashMap<String, String>();
        StringBuilder sql = null;
        ResultSet rs = null;
        for (String key : src.keySet()) {
            String value = "";
            sql = new StringBuilder();
            sql.append("SELECT DISTINCT(");
            if ("2".equals(envLang)) {
                sql.append("ENGLISH_GBK");
            } else if ("3".equals(envLang)) {
                sql.append("FRENCH_UTF16");
            } else if ("4".equals(envLang)) {
                sql.append("TRADCHN_UTF16");
            }
            sql.append(") FROM ALLINONE ");
            sql.append("WHERE VERSION = '");
            sql.append(version);
            sql.append("' AND SIMPCHN_GBK = '");
            sql.append(key);
            sql.append("'");
            rs = dbm.executeQuery(sql.toString());

            try {
                while (rs.next()) {
                    value = rs.getString(1);
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dest.put(key, value);
        }
        return dest;
    }
}
