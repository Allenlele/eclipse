package base;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Translator {
    private static Translator trans = null;
    private static final String SIMPCHN2ENGLISH = "2";
    private static final String SIMPCHN2FRENCH = "3";
    private static final String SIMPCHN2TRADCHN = "4";

    public Translator() {
    }

    public static Translator getInstance() {
        if (trans == null) {
            trans = new Translator();
        }
        return trans;
    }

    public List<String> transSwich(String transType, String compNm, String module) {
        PropertiesRW pp = new PropertiesRW(new File(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "db.properties"));
        DBMng dbm = DBMng.getInstance(
                pp.readValue("user"),
                pp.readValue("pwd"),
                pp.readValue("ip"),
                pp.readValue("port"),
                pp.readValue("dbnm"));
        List<String> translatedNms = new ArrayList<String>();
        String sql = "";
        if(module == null || "".equals(module)) {
            if(SIMPCHN2ENGLISH.equals(transType)) {
                sql = "SELECT DISTINCT(ENGLISH_GBK) FROM ALLINONE" 
                    + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else if (SIMPCHN2FRENCH.equals(transType)) {
                sql = "SELECT DISTINCT(FRENCH_UTF16) FROM ALLINONE" 
                    + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else if (SIMPCHN2TRADCHN.equals(transType)){
                sql = "SELECT DISTINCT(TRADCHN_UTF16) FROM ALLINONE" 
                    + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else {
                System.out.println("指定语种类别不正确！");
                return null;
            }
        } else {
            if(SIMPCHN2ENGLISH.equals(transType)) {
                sql = "SELECT DISTINCT(ENGLISH_GBK) FROM " + module.toUpperCase() 
                + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else if (SIMPCHN2FRENCH.equals(transType)) {
                sql = "SELECT DISTINCT(FRENCH_UTF16) FROM " + module.toUpperCase() 
                + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else if (SIMPCHN2TRADCHN.equals(transType)){
                sql = "SELECT DISTINCT(TRADCHN_UTF16) FROM " + module.toUpperCase() 
                + " WHERE SIMPCHN_GBK = '" + compNm +"'";
            } else {
                System.out.println("指定语种类别不正确！");
                return null;
            }
        }

        ResultSet rs = dbm.executeQuery(sql);
        try {
            String name;
            while(rs.next()) {
                name = rs.getString(1);
                translatedNms.add(name);
            }

            if(translatedNms.size() == 0){
                if(SIMPCHN2ENGLISH.equals(transType)) {
                    sql = "SELECT DISTINCT(ENGLISH_GBK) FROM ALLINONE" 
                        + " WHERE SIMPCHN_GBK = '" + compNm +"'";
                } else if (SIMPCHN2FRENCH.equals(transType)) {
                    sql = "SELECT DISTINCT(FRENCH_UTF16) FROM ALLINONE" 
                        + " WHERE SIMPCHN_GBK = '" + compNm +"'";
                } else if (SIMPCHN2TRADCHN.equals(transType)){
                    sql = "SELECT DISTINCT(TRADCHN_UTF16) FROM ALLINONE" 
                        + " WHERE SIMPCHN_GBK = '" + compNm +"'";
                }

                rs = dbm.executeQuery(sql);
                while(rs.next()) {
                    name = rs.getString(1);
                    translatedNms.add(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return translatedNms;
    }

    public void close() {
        PropertiesRW pp = new PropertiesRW(new File("./conf/db.properties"));
        DBMng dbm = DBMng.getInstance(
                pp.readValue("user"),
                pp.readValue("pwd"),
                pp.readValue("ip"),
                pp.readValue("port"),
                pp.readValue("dbnm"));
        dbm.close();
    }
}
