import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import base.DataPool;
import base.FileTool;
import base.FindTimeoutException;
import base.Log;
import base.PropertiesUtil;
import base.SyncCacheUtil;
import base.WriteExcel;

/**
 * Script Name : <b>MainRun</b>
 * Description : Functional Test Script
 *
 * @since 2013/01/10
 * @author Qiaorg
 * @version NC63
 */
public class MainRun extends ExecuteLine {

    String[] cases = null;
    HashMap<String, String> dependCase = new HashMap<String, String>();
    LinkedHashMap<String, Integer> results = new LinkedHashMap<String, Integer>();
	SimpleDateFormat commonSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    StringBuffer result = new StringBuffer();
    boolean isException = false;
    StringBuilder errorInfo = null;
    Date now = null;
    String replayTime = "";
    String resultTime = "";
    String envLang = "";
    int sleepTimeInAction = 0;
    String ASRScriptFolder = "";
    String moduleName = "";
    String nodeName = "";

    public void testMain(Object[] args) {

        cases = new String[]{"D:/RFT/case/01_测试脚本/NC6Series.xls"};
        if (args != null && args.length > 0) {
            String argStr = "";
            //回放列表中文件个数过多时的处理
            if (((String) args[0]).charAt(0) == '1'){
                File argFile = new File(((String)args[0]).substring(1));
                FileReader reader;
                try {
                    reader = new FileReader(argFile);
                    BufferedReader in = new BufferedReader(reader);
                    while((argStr = in.readLine()) != null){
                        argStr = argStr.trim();
                        break;
                    }
                    reader.close();
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(argFile.exists()) {
                    argFile.delete();
                }
                if(argStr == null || "".equals(argStr)) {
                    return;
                }
            } else {
                argStr = (String) args[0];
            }

            // auto silence run
            if(argStr.contains("#ASR#")) {
                ASRScriptFolder = argStr.substring(argStr.lastIndexOf("#ASR#") + 5, argStr.length());
                logInfo(ASRScriptFolder);
                argStr = argStr.substring(0, argStr.lastIndexOf("#ASR#"));
            }
            // Language
            if(argStr.contains("#LANG#")) {
                String lang = (argStr.substring(argStr.lastIndexOf("#LANG#") + 6, argStr.length()));
                if("0".equals(lang) || "1".equals(lang)) {
                    envLang = "";
                } else {
                    envLang = lang;
                }
                argStr = argStr.substring(0, argStr.lastIndexOf("#LANG#"));
            }
            // SleepTimeInAction
            if(argStr.contains("#STIA#")) {
                String st = argStr.substring(argStr.lastIndexOf("#STIA#") + 6, argStr.length());
                sleepTimeInAction = Integer.valueOf(st);
                argStr = argStr.substring(0, argStr.lastIndexOf("#STIA#"));
            }
            // SleepTime
            if(argStr.contains("#ST#")) {
                String st = argStr.substring(argStr.lastIndexOf("#ST#") + 4, argStr.length());
                sleepTime = Integer.valueOf(st);
                argStr = argStr.substring(0, argStr.lastIndexOf("#ST#"));
            }

            cases = argStr.split("#");
        }
        now = new Date();
        beforeReplay();

        if (ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
            addDepend(cases);
        }

        if (envLang != null && !"".equals(envLang)) {
            SyncCacheUtil.syncCache(envLang);
        }
        for (int ca = 0; ca < cases.length; ca++) {
        	sleep(1);
        	replayTime = commonSDF.format(new Date());
            errorInfo = new StringBuilder();

            File file = new File(cases[ca]);
            if (dependConditional(file))
                continue;
            String type = FileTool.getXLSData(file, 1, 8);

            moduleName = FileTool.getModuleAndNode(file)[0] == null ? "" : FileTool.getModuleAndNode(file)[0];
            nodeName = FileTool.getModuleAndNode(file)[1] == null ? "" : FileTool.getModuleAndNode(file)[1];

            //envLang = FileTool.getXLSData(file, 1, 9);
            if (envLang != null && !"".equals(envLang)) {
                if("2".equals(envLang)) {
                    compNmCache = PropertiesUtil.readProperties("compNmsCache_EN.properties");
                } else if ("3".equals(envLang)) {
                    compNmCache = PropertiesUtil.readProperties("compNmsCache_FR.properties");
                } else if ("4".equals(envLang)) {
                    compNmCache = PropertiesUtil.readProperties("compNmsCache_TW.properties");
                }
            }

            if ("dp".equalsIgnoreCase(type)) {
                runOneCaseByDp(file);
            } else {
                runOneCase(file);
            }

            if (envLang != null && !"".equals(envLang) && !compNmCacheAdd.isEmpty()) {
                if("2".equals(envLang)) {
                    PropertiesUtil.writeProperties(compNmCacheAdd, "compNmsCache_EN.properties");
                } else if ("3".equals(envLang)) {
                    PropertiesUtil.writeProperties(compNmCacheAdd, "compNmsCache_FR.properties");
                } else if ("4".equals(envLang)) {
                    PropertiesUtil.writeProperties(compNmCacheAdd, "compNmsCache_TW.properties");
                }
                compNmCacheAdd.clear();
            }
        }

        trans.close();

        if (results.size() > 0) {
            try {
                writeResult();
            } catch (Exception e) {
                logError(e.getMessage());
                logError("写入回放结果失败！！");
            }
        }
        unregisterAll();
    }

    public void runOneCase(File file) {
        logInfo("########   " + file.getName() + "  开始回放！");
        boolean isSuccesse = true;
        int loop = 1;
        int begin = 2;
        int end = 2;
        int start = 2;

        try {
            loop = Integer.parseInt(FileTool.getXLSData(file, 1, 8));
            begin = Integer.parseInt(FileTool.getXLSData(file, 1, 7)) - 1;
            end = Integer.parseInt(FileTool.getXLSData(file, 3, 7)) - 1;
        } catch (Exception e) {
            logInfo("请检查开始行，结束行和循环次数！");
            return;
        }
        String[] line_now = null;
        try {
            this.rpNow = file;
            for (int j = 1; j <= loop; j++) {
                begin = Integer.parseInt(FileTool.getXLSData(file, 1, 7)) - 1;
                start = begin;
                while (begin <= end) {
                    Method method = null;
                    try {
                        line_now = FileTool.getLine(file, begin);

                        line_now[2] = line_now[2].trim();

                        try {
                            if (line_now[3].indexOf("数据库") != -1 || line_now[5].indexOf("数据库") != -1) {
                                line_now = FileTool.getLineWithDBInfo(file,
                                        begin);
                            }
                        } catch (Exception e) {
                            logInfo(e.toString());
                        }
                        if (loop != 1)
                            line_now = FileTool.parameter(line_now, j);
                        if (line_now[0] != null && line_now[0].startsWith("#")) {
                            begin++;
                            continue;
                        }
                        if (line_now == null)
                            break;
                        if (line_now[2].equals(""))
                            break;
                        Object arg = line_now;
                        System.out.println("行号:" + (begin + 1) + "    动作:"
                                + line_now[2] + "   对象:" + line_now[3]);
                        method = getMethod(line_now[2]);
                        sleepDeal(line_now, file, begin);
                        if (begin != start && sleepTimeInAction > 0
								&& !"检查点".equals(line_now[2])) {
                            sleep(sleepTimeInAction);
                        }
                        method.invoke(this, arg, envLang);

                        String currentScript = "++++++++  成功  行号:" + (begin + 1) + "  动作:"
                        + line_now[2] + "   对象:" + line_now[3];

                        if(!(line_now[4] == null || "".equals(line_now[4])) && "表体".equals(line_now[3])) {
                            currentScript = currentScript + "   行对象:" + line_now[4];
                        } else if(!(line_now[4] == null || "".equals(line_now[4]))){
                            currentScript = currentScript + "   对象名:" + line_now[4];
                        }

                        if(!(line_now[5] == null || "".equals(line_now[5])) && "表体".equals(line_now[3])) {
                            currentScript = currentScript + "   列对象:" + line_now[5];
                        } else if (!(line_now[5] == null || "".equals(line_now[5]))) {
                            currentScript = currentScript + "   对象名:" + line_now[5];
                        }

                        if(!(line_now[6] == null || "".equals(line_now[6]))) {
                            currentScript = currentScript + "   目标值:" + line_now[6];
                        }

                        logInfo(currentScript);
                    } catch (Exception e) {

                        try {
                            isSuccesse = false;
                            String filename = String.format(
                                    "./log/%s%d_%s_%s.jpg", file.getName(),
                                    begin + 1, line_now[2], line_now[3]);
                            Log.printScreen(filename.replaceAll(">", "-"));

                            if(ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
                                StringBuilder asrLog = new StringBuilder();
                                asrLog.append(ASRScriptFolder);
                                asrLog.append(File.separator);
                                asrLog.append(filename.substring(filename.lastIndexOf("log") + 4));

                                FileTool.copyFile(filename, asrLog.toString());
                            }

                            String error = String.format(
                                            "********   失败    行号:%s 动作:%s     对象:%s 执行失败！截图见%s",
                                            begin + 1, line_now[2], line_now[3], filename);
                            logInfo(error);
                            if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                                errorInfo = errorInfo.append(error);
                            } else {
                                errorInfo = errorInfo.append("  " + error);
                            }
                            // Log.SendEmail(error, filename);
                            if (method.getName().equals("树选择") || method.getName().equals("检查点")) {
                                begin++;
                                continue;
                            }

                            if (!(method.getName().equals("检查点") && line_now[3]
                                    .indexOf("表体") != -1)) {
                                String store = envLang;
                                //对原本是英文的脚本，即使控制界面设置了语言，值列可设置值来覆盖控制界面的语言设置。
                                if("1".equals(line_now[6])
                                        && ("1".equals(envLang) || "".equals(envLang))
                                        && ("关闭节点".equals(line_now[2])
                                                || "关闭页签".equals(line_now[2])
                                                || "打开节点".equals(line_now[2]))) {
                                    envLang = "2";
                                }
                                dealError(envLang);
                                while (true) {
                                    begin++;
                                    String a1 = FileTool.getXLSData(file, begin, 2);
                                    if (a1 == null) {
                                        begin--;
                                        break;
                                    }
                                    if (a1.equals("打开节点") || a1.equals("")) {
                                        begin--;
                                        break;
                                    }
                                }
                                envLang = store;
                            }

                            //异常时跳出当前脚本
                            if (e instanceof InvocationTargetException) {

                                Throwable targetEx = ((InvocationTargetException) e)
                                        .getTargetException();
                                if (targetEx != null) {
                                    isException = true;
                                    logInfo(targetEx.getMessage());
                                    if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                                        errorInfo = errorInfo.append(targetEx.getMessage());
                                    } else {
                                        errorInfo = errorInfo.append("  " + targetEx.getMessage());
                                    }
                                    dealError(envLang);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            if (ex instanceof NullPointerException){
                                logInfo("发生空指针异常！！");
                                if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                                    errorInfo = errorInfo.append("发生空指针异常！！");
                                } else {
                                    errorInfo = errorInfo.append("  " + "发生空指针异常！！");
                                }
                                dealErrorOfNullPoint(envLang);
                                isException = true;
                                break;
                            }
                        }
                    }
                    begin++;
                }
            }
        } catch (Exception e) {
            try {
                isSuccesse = false;
                logInfo(e.toString());
                String filename = String.format("./log/%s.jpg", file.getName());
                Log.printScreen(filename.replaceAll(">", "-"));

                if(ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
                    StringBuilder asrLog = new StringBuilder();
                    asrLog.append(ASRScriptFolder);
                    asrLog.append(File.separator);
                    asrLog.append(filename.substring(filename.lastIndexOf("log") + 4));

                    FileTool.copyFile(filename, asrLog.toString());
                }

                logInfo(String.format(
                        "********   失败    行号:%s 动作:%s     对象:%s 执行失败！截图见./log/%s.jpg",
                        begin + 1, line_now[2], line_now[3], file.getName()));
                if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                    errorInfo = errorInfo.append(String.format(
                            "********   失败  行号:%s   动作:%s   对象:%s   执行失败！截图见./log/%s.jpg",
                            begin + 1, line_now[2], line_now[3], file.getName()));
                } else {
                    errorInfo = errorInfo.append("  " + String.format(
                            "********   失败  行号:%s   动作:%s   对象:%s   执行失败！截图见./log/%s.jpg",
                            begin + 1, line_now[2], line_now[3], file.getName()));
                }
                dealError(envLang);
            } catch (Exception ex) {
                if (ex instanceof NullPointerException){
                    logInfo("发生空指针异常！！");
                    isException = true;
                    isSuccesse = false;
                    if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                        errorInfo = errorInfo.append("发生空指针异常！！");
                    } else {
                        errorInfo = errorInfo.append("  " + "发生空指针异常！！");
                    }
                    try {
                        dealErrorOfNullPoint(envLang);
                    } catch (FindTimeoutException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        resultTime = commonSDF.format(new Date());
        StringBuilder key = null;
        if (!isSuccesse) {
            key = new StringBuilder();
            key.append(moduleName);
            key.append("|");
            key.append(nodeName);
            key.append("|");
            key.append(file.getAbsolutePath().split(".xls")[0]);
            key.append("|");
            key.append(errorInfo.toString());
            key.append("|");
            key.append(replayTime);
            key.append("|");
            key.append(resultTime);
            key.append("|");
            key.append(start);
            key.append("|");
            key.append(end);
            results.put(key.toString(), 1);
            result.append(file.getName() + "失败    ");
            logInfo("########   " + file.getName() + "  回放结束！  失败!");
        } else {
            key = new StringBuilder();
            key.append(moduleName);
            key.append("|");
            key.append(nodeName);
            key.append("|");
            key.append(file.getAbsolutePath().split(".xls")[0]);
            key.append("|");
            key.append(replayTime);
            key.append("|");
            key.append(resultTime);
            key.append("|");
            key.append(start);
            key.append("|");
            key.append(end);
            results.put(key.toString(), 0);
            result.append(file.getName() + "成功    ");
            logInfo("########   " + file.getName() + "  回放结束！  成功!");
        }

    }

    private void sleepDeal(String[] a, File file, int x)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (a[2].equals("树选择")) {
            String[] ne = FileTool.getLine(file, x + 1);
            if (ne != null && ne[2] != null && ne[2].equals("单击") && !ne[3].equals("表体")) {
                // String[] arg = { "", "", "等待", "按钮", ne[3] };
                to = JavaDomain().find(
                        atList(atDescendant("uIClassID", "ButtonUI", "text",
                                ne[3])));
                int st = 0;
                while (to.length == 0) {
                    sleep(1);
                    to = JavaDomain()
                            .find(
                                    atDescendant("uIClassID", "ButtonUI",
                                            "text", a[4]));
                    if (st++ > 15)
                        break;
                }
            }
        }
    }

    public void runOneCaseByDp(File file) {
        logInfo("########   " + file.getName() + "  开始回放！");
        boolean isSuccesse = true;
        int x = Integer.parseInt(FileTool.getXLSData(file, 1, 7)) - 1;
        int x1 = Integer.parseInt(FileTool.getXLSData(file, 3, 7)) - 1;
        int start = 2;
        String[] a = null;
        try {

            int row = 1;
            while (true) {
                HashMap<String, String> dp = DataPool.getDPhead(file, row);
                if (dp == null)
                    break;
                x = Integer.parseInt(FileTool.getXLSData(file, 1, 7)) - 1;
                start = x;
                while (x <= x1) {
                    try {
                        a = DataPool.setValue(FileTool.getLine(file, x), dp);
                        if (a == null)
                            break;
                        if (a[2] == null || a[2].equals(""))
                            break;
                        a[2] = a[2].trim();
                        Object arg = a;
                        System.out.println("行号:" + (x + 1) + "    动作:" + a[2]
                                + " 对象:" + a[3]);
                        Method method = getMethod(a[2]);
                        if(x != start && sleepTimeInAction > 0
                        		&& !"检查点".equals(a[2])) {
                            sleep(sleepTimeInAction);
                        }
                        method.invoke(this, arg, envLang);
                        String currentScript = "++++++++  成功  行号:" + (x + 1) + "  动作:"
                        + a[2] + "   对象:" + a[3];

                        if(!(a[4] == null || "".equals(a[4])) && "表体".equals(a[3])) {
                            currentScript = currentScript + "   行对象:" + a[4];
                        } else if(!(a[4] == null || "".equals(a[4]))){
                            currentScript = currentScript + "   对象名:" + a[4];
                        }

                        if(!(a[5] == null || "".equals(a[5])) && "表体".equals(a[3])) {
                            currentScript = currentScript + "   列对象:" + a[5];
                        } else if (!(a[5] == null || "".equals(a[5]))) {
                            currentScript = currentScript + "   对象名:" + a[5];
                        }

                        if(!(a[6] == null || "".equals(a[6]))) {
                            currentScript = currentScript + "   目标值:" + a[6];
                        }

                        logInfo(currentScript);
                    } catch (Exception e) {
                        isSuccesse = false;
                        String filename = String.format("./log/%s%d.jpg", file
                                .getName(), x + 1);
                        Log.printScreen(filename.replaceAll(">", "-"));

                        if(ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
                            StringBuilder asrLog = new StringBuilder();
                            asrLog.append(ASRScriptFolder);
                            asrLog.append(File.separator);
                            asrLog.append(filename.substring(filename.lastIndexOf("log") + 4));

                            FileTool.copyFile(filename, asrLog.toString());
                        }

                        String error = String.format(
                                "********   失败    行号:%s 动作:%s     对象:%s 执行失败！截图见%s",
                                x + 1, a[2], a[3], filename);
                        System.out.println(error);
                        logInfo(error);
                        if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                            errorInfo = errorInfo.append(error);
                        } else {
                            errorInfo = errorInfo.append("  " + error);
                        }
                        // Log.SendEmail(error, filename);
                        //对原本是英文的脚本，即使控制界面设置了语言，值列可设置值来覆盖控制界面的语言设置。
                        if(a[6] != null && !"".equals(a[6])) {
                            envLang = a[6];
                        }
                        dealError(envLang);
                        while (true) {
                            x++;
                            String a1 = FileTool.getXLSData(file, x, 2);
                            if (a1 == null) {
                                x--;
                                break;
                            }
                            if (a1.equals("打开节点") || a1.equals("")) {
                                x--;
                                break;
                            }
                        }
                    }
                    x++;
                }
                row++;
            }
        } catch (Exception e) {
            isSuccesse = false;
            String filename = String.format("./log/%s.jpg", file.getName());
            Log.printScreen(filename.replaceAll(">", "-"));

            if(ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
                StringBuilder asrLog = new StringBuilder();
                asrLog.append(ASRScriptFolder);
                asrLog.append(File.separator);
                asrLog.append(filename.substring(filename.lastIndexOf("log") + 4));

                FileTool.copyFile(filename, asrLog.toString());
            }

            logInfo(String.format(
                    "********   失败    行号:%s 动作:%s     对象:%s 执行失败！截图见./log/%s.jpg",
                    x + 1, a[2], a[3], file.getName()));
            if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                errorInfo = errorInfo.append(String.format(
                        "********   失败  行号:%s   动作:%s   对象:%s   执行失败！截图见./log/%s.jpg",
                        x + 1, a[2], a[3], file.getName()));
            } else {
                errorInfo = errorInfo.append("  " + String.format(
                        "********   失败  行号:%s   动作:%s   对象:%s   执行失败！截图见./log/%s.jpg",
                        x + 1, a[2], a[3], file.getName()));
            }
            try {
                dealError(envLang);
            } catch (FindTimeoutException e1) {
                logInfo(e1.getMessage());
                logInfo(e1.getStackTrace().toString());
                if (errorInfo.toString() == null || "".equals(errorInfo.toString())){
                    errorInfo = errorInfo.append(e1.getMessage() + e1.getStackTrace().toString());
                } else {
                    errorInfo = errorInfo.append("  " + e1.getMessage() + e1.getStackTrace().toString());
                }
            }
        }
        resultTime = commonSDF.format(new Date());
        StringBuilder key = null;
        if (!isSuccesse) {
            key = new StringBuilder();
            key.append(moduleName);
            key.append("|");
            key.append(nodeName);
            key.append("|");
            key.append(file.getAbsolutePath().split(".xls")[0]);
            key.append("|");
            key.append(errorInfo.toString());
            key.append("|");
            key.append(replayTime);
            key.append("|");
            key.append(resultTime);
            key.append("|");
            key.append(start);
            key.append("|");
            key.append(x1);
            results.put(key.toString(), 1);
            result.append(file.getName() + "失败    ");
            logInfo("########   " + file.getName() + "  回放结束！  失败!");
        } else {
            key = new StringBuilder();
            key.append(moduleName);
            key.append("|");
            key.append(nodeName);
            key.append("|");
            key.append(file.getAbsolutePath().split(".xls")[0]);
            key.append("|");
            key.append(replayTime);
            key.append("|");
            key.append(resultTime);
            key.append("|");
            key.append(start);
            key.append("|");
            key.append(x1);
            results.put(key.toString(), 0);
            result.append(file.getName() + "成功    ");
            logInfo("########   " + file.getName() + "  回放结束！  成功!");
        }
    }

    public Method getMethod(String method) {
        Method[] methods = getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(method)) {

                return methods[i];
            }
        }
        return null;
    }

    // when auto run script, if log on is failed, other cases after log on script will be not replay.
    public void addDepend(String[] cases) {
        for (int i = 2; i < cases.length - 1; i++) {
            dependCase.put(cases[i].split(".xls")[0], cases[1].split(".xls")[0]);
        }
    }

    public void beforeReplay() {
        if (!(new File("./case/depend.xls").exists()))
            return;
        try {
            Workbook book = Workbook.getWorkbook(new File("./case/depend.xls"));
            Sheet sheet = book.getSheet(0); // 获得第一个工作表对象
            String result1;
            String result2;
            int row = 1;

            while (true) {
                Cell cell1 = sheet.getCell(0, row); // 得到第一列第二行的单元格
                result1 = cell1.getContents().trim();
                Cell cell2 = sheet.getCell(1, row);// 得到第二列第二行的单元格
                result2 = cell2.getContents().trim();

                if (result1 == null || result1.equals(""))
                    break;
                dependCase.put(result1, result2);
                row++;
            }
            book.close();
        } catch (Exception e) {
            logInfo("########获取Case依赖信息失败!########");
        }
    }

    public boolean dependConditional(File file) {
    	int start = Integer.parseInt(FileTool.getXLSData(file, 1, 7)) - 1;
        int end = Integer.parseInt(FileTool.getXLSData(file, 3, 7)) - 1;
        String casename = file.getAbsolutePath().split(".xls")[0];
        resultTime = commonSDF.format(new Date());
        if (dependCase.size() > 0 && dependCase.get(casename) != null) {
            String dependCaseName = dependCase.get(casename);
            if (!(dependCaseName == null || "".equals(dependCaseName))) {
                String key = "";
                for (String s: results.keySet()) {
                    if (s.indexOf(dependCaseName) != -1) {
                        key = s;
                        break;
                    }
                }
                if (key != null && !"".equals(key)) {
                    if (results.get(key) != null
                            && results.get(key) == 1) {
                        results.put(moduleName + "|" + nodeName + "|" + file.getAbsolutePath().split(".xls")[0] + "|【"
                                + casename + "】依赖于【" + dependCaseName + "】的测试结果。" + "|" + replayTime + "|"
                                + resultTime + "|" + start + "|" + end, 2);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void writeResult() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        File resultFolder = new File(System.getProperty("user.dir")
                + File.separator + "result");
        if(!(resultFolder.exists() || resultFolder.isDirectory())) {
            resultFolder.mkdir();
        }
        File file = new File(String.format(resultFolder.getAbsolutePath()
                + File.separator + "result_%s.xls", sdf.format(now)));
        WriteExcel we = new WriteExcel(file);

        we.setValue("模块", 0, 0);
        we.setValue("节点", 1, 0);
        we.setValue("脚本名称", 2, 0);
        we.setValue("执行结果", 3, 0);
        we.setValue("原因", 4, 0);
        we.setValue("开始时间", 5, 0);
        we.setValue("结束时间", 6, 0);
        we.setValue("开始行", 7, 0);
        we.setValue("结束行", 8, 0);
        int i = 1;
        String[] key = new String[8];
        for (Entry<String, Integer> s : results.entrySet()) {
        	key = s.getKey().toString().split("[|]");
            we.setValue(key[0], 0, i);
            we.setValue(key[1], 1, i);
            we.setValue(key[2], 2, i);
            if (s.getValue() == 0){
                we.setValue("成功", 3, i);
                we.setValue(key[3], 5, i);
                we.setValue(key[4], 6, i);
                we.setValue(key[5], 7, i);
                we.setValue(key[6], 8, i);
            } else if (s.getValue() == 1) {
                we.setValue("失败", 3, i);
                we.setValue(key[3], 4, i);
                we.setValue(key[4], 5, i);
                we.setValue(key[5], 6, i);
                we.setValue(key[6], 7, i);
                we.setValue(key[7], 8, i);
            } else {
                we.setValue("未回放", 3, i);
                we.setValue(key[3], 4, i);
                we.setValue(key[4], 5, i);
                we.setValue(key[5], 6, i);
                we.setValue(key[6], 7, i);
                we.setValue(key[7], 8, i);
            }
            i++;
        }
        we.write();
        we.close();

        if(ASRScriptFolder != null && !"".equals(ASRScriptFolder)) {
            StringBuilder asrLog = new StringBuilder();
            asrLog.append(ASRScriptFolder);
            asrLog.append(File.separator);
            asrLog.append(ASRScriptFolder.substring(ASRScriptFolder.lastIndexOf("\\") + 1));
            asrLog.append(".xls");

            FileTool.copyFile(file.getAbsolutePath(), asrLog.toString());
        }

        File temp = new File("./temp/temp.txt");
        FileWriter writer;
        try {
            writer = new FileWriter(temp);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            PrintWriter out = new PrintWriter(bufferedWriter);
            out.append(file.getAbsolutePath());
            out.flush();
            out.close();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
