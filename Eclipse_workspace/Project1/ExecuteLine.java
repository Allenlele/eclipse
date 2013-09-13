import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.DBMng;
import base.FileTool;
import base.FindTimeoutException;
import base.GetFlux;
import base.MultiSCKUtil;
import base.SqlFileExec;

import com.rational.test.ft.object.interfaces.BrowserTestObject;
import com.rational.test.ft.object.interfaces.FrameTestObject;
import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.IScreen;
import com.rational.test.ft.object.interfaces.IWindow;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiTestObject;
import com.rational.test.ft.object.interfaces.TopLevelTestObject;
import com.rational.test.ft.script.Action;
import com.rational.test.ft.script.MouseModifiers;
import com.rational.test.ft.script.Property;
import com.rational.test.ft.sys.graphical.Mouse;
import com.rational.test.ft.vp.ITestDataTable;
import com.rational.test.util.regex.Regex;

/**
 * Script Name : <b>ExecuteLine</b>
 * Description : Functional Test Script `
 *
 * @since 2012/11/01
 * @author qiaorg
 */
public class ExecuteLine extends GetNcUI {
    List<String> compNm = new ArrayList<String>(0);
    Map<String,String> compNmCache = new HashMap<String,String>();
    Map<String,String> compNmCacheAdd = new HashMap<String,String>();
    File moduleProp = new File(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "module_zh_ascii.properties");
    int breakTime = 3;

    public File rpNow;
    String startTime = null;
    String url = "";

    public void testMain(Object[] args){
    }

    public void 打开网页(String[] a, String envLang) throws FindTimeoutException {
        startBrowser(a[3]);
        sleep(4);
        if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
            to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText", "登录NC系统")));
        } else if ("2".equals(envLang)){
            to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText",
                    "Log into NC System")));
        }
        int st = 0;
        while(to.length == 0) {
        	sleep(1);
        	if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText", "登录NC系统")));
            } else if ("2".equals(envLang)){
                to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText",
                        "Log into NC System")));
            }
        	if(st++ == 180) {
        		throw new FindTimeoutException(
                	"提示信息：打开网页失败，打开网页动作已超过三分钟！！请检查测试环境连接是否正常或服务器是否正常运行！！");
        	}
        }
        unregisterAll();
    }

    // ********************************************节点打开****************************
    public void 打开节点(String[] a, String envLang) throws FindTimeoutException {
        // changTab("功能导航");
        try {
            if (a[3].indexOf("->") != -1) {
                String[] tip = a[3].split("->");
                if(!(envLang == null || "".equals(envLang))) {
                    String module;

                    String compNmInCache = compNmCache.get(tip[0]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        tip[0] = compNmInCache;
                    } else {

                        module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, tip[0], module);
                        if(compNm == null || compNm.size() == 0){
                            logError(String.format("【%s】控件名字的翻译结果为空！！当前行的脚本未执行......", tip[0]));
                            return;
                        }
                        compNmCache.put(tip[0], compNm.get(0));
                        compNmCacheAdd.put(tip[0], compNm.get(0));
                        tip[0] = compNm.get(0);
                    }

                    compNmInCache = compNmCache.get(tip[1]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        tip[1] = compNmInCache;
                    } else {
                        module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, tip[1], module);
                        if(compNm == null || compNm.size() == 0){
                            logError(String.format("【%s】控件名字的翻译结果为空！！当前行的脚本未执行......", tip[1]));
                            return;
                        }
                        compNmCache.put(tip[1], compNm.get(0));
                        compNmCacheAdd.put(tip[1], compNm.get(0));
                        tip[1] = compNm.get(0);
                    }
                }
                changTab(tip[0]);
                clickList(tip[1]);
            }
        } catch (Exception e) {
        }

        GuiTestObject to = getSercherField();
        to.click();
        if (to.getProperty("text").toString() != null || !"".equals(to.getProperty("text").toString())) {
            to.doubleClick();
        }
        try {
            String[] tip = a[3].split("->");
            if(!(envLang == null || "".equals(envLang))) {

                String compNmInCache = compNmCache.get(tip[tip.length - 1]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    tip[tip.length - 1] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, tip[tip.length - 1], module);
                    if(compNm == null || compNm.size() == 0){
                        logError(String.format("【%s】控件名字的翻译结果为空！！当前行的脚本未执行......",
                                tip[tip.length - 1]));
                        return;
                    }
                    compNmCache.put(tip[tip.length - 1], compNm.get(0));
                    compNmCacheAdd.put(tip[tip.length - 1], compNm.get(0));
                    tip[tip.length - 1] = compNm.get(0);
                }
            }
            inputString(tip[tip.length - 1]);
        } catch (Exception e) {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError(String.format("【%s】控件名字的翻译结果为空！！当前行的脚本未执行......", a[3]));
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }
            inputString(a[3]);
        }
        sleep(0.5);
        IScreen is = getRootTestObject().getScreen();
        if (a[4] != null && !a[4].equals("")) {
            for (int i = 1; i < Integer.parseInt(a[4]); i++) {
                is.inputKeys("{ExtDown}");
            }
        }
        inputKeyENTER();
        sleep(0.3);
        inputKeyENTER();
        int st = 0;
        while (JavaDomain().find(
                atDescendant("uIClassID", "LabelUI", "text", "Loading")).length != 0) {
            sleep(1);
            if (st++ > 15)
                break;
        }

        if(!a[3].equals("报表数据中心") && !a[3].equals("Report Data Center")) {
        	GuiSubitemTestObject gst = null;
        	
        	String store = envLang;
        	//对原本是英文的脚本，即使控制界面设置了语言，值列可设置值来覆盖控制界面的语言设置。
        	if("1".equals(a[6]) && ("".equals(envLang) || "1".equals(envLang))) {
        		envLang = "2";
        	}
        	
        	if(envLang == null || "".equals(envLang) || "4".equals(envLang)){
        		gst = getTabbedPane("消息中心");
        	} else if (envLang.equals("2")) {
        		gst = getTabbedPane("Message Center");
        	}
        	
        	if (gst == null)
        		return;
        	st = 0;
        	String node = a[3].split("->").length > 1 ? a[3].split("->")[a[3].split("->").length - 1] : a[3];
        	while (gst.getProperty(".tabs").toString().indexOf(node) == -1) {
        		sleep(1);
        		if(envLang == null || "".equals(envLang) || "4".equals(envLang)){
        			gst = getTabbedPane("消息中心");
        		} else if (envLang.equals("2")) {
        			gst = getTabbedPane("Message Center");
        		}
        		if (st++ > sleepTime){
        			throw new FindTimeoutException(
        					"提示信息：打开指定节点超时、请查证指定名称的节点名字是否正确！！请检查测试环境是否正常！！");
        		}
        	}
        	envLang = store;
        }
        unregisterAll();
    }

    // ************************************************按钮单击*******************
    public void 单击(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("") || a[3] == null) {
            GuiTestObject to = getFirst(a);
            to.click();
            return;
        }
        if (a[3].toString().indexOf("表体") != -1) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {

                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }

            if (a[3].toString().equals("表体内容")) {
                GuiSubitemTestObject to = getTableTeshu(a);
                clickTableContent(to, a);
            } else {
                GuiSubitemTestObject to = getTable(a);
                clickTable(to, a, envLang);
            }
        } else if (a[3].equals("表头")) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }

            GuiSubitemTestObject to = getTableHeader(a);
            if("多选".equals(a[5])) {
                to.click(atColumn(atIndex(1)));
            } else {
                to.click(atColumn(a[5]));
            }
        } else if (a[3].equals("报表")) {
            GuiTestObject to = getReportTable();
            java.awt.Point p = (Point) to.getProperty("locationOnScreen");
            clickScreenPoint(new Point(p.x + 5, p.y + 5));
            inputKey("{ExtDown}", Integer.parseInt(a[4]) - 1);
            inputKey("{ExtRight}", Integer.parseInt(a[5]) - 1);
        } else if (a[3].equals("按钮")) {
            if(isNumber(a[4])) {
                GuiTestObject to = getButtonByIndex(a);
                to.click();
            } else if(a[4] != null && !"".equals(a[4])){
                if(a[4].equals("选中")) {
                    a[5] = "move_down.png";
                } else if (a[4].equals("全部选中")) {
                    a[5] = "bottom.png";
                } else if (a[4].equals("取消")) {
                    a[5] = "move_up.png";
                } else if (a[4].equals("全部取消")) {
                    a[5] = "top.png";
                }
                GuiTestObject to = getButtonByIcon(a);
                to.click();
                a[5] = "";
            }
        } else if (a[3].equals("文本框")) {
            if (a[4] != null && !"".equals(a[4])) {

                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[4]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[4] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[4], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[4], compNm.get(0));
                        compNmCacheAdd.put(a[4], compNm.get(0));
                        a[4] = compNm.get(0);
                    }
                }

                TextGuiTestObject to = getFieldTextByName(a);
                to.click();
            } else if (a[6] != null && !"".equals(a[6])){
                TextGuiTestObject to = getFieldTextByIndex(a);
                to.click();
            }
        } else if (a[3].equals("透视图")) {
        	GuiTestObject to = getPerspectiveChart(a);
        	Point atPoint = new Point();
        	if("".equals(a[4]) && "".equals(a[5])) {        		
        		to.click();
        	} else if(!"".equals(a[4]) && !"".equals(a[5])) {
        		String[] chartDetails = to.getProperty("imageMap").toString().split("\\n");
                String subchartRec = "";
                for(String detail : chartDetails) {
                    if(detail.indexOf(a[4]) != -1 && detail.indexOf(a[5]) != -1) {
                    	subchartRec = detail.substring(detail.indexOf("coords=\"") + 8, detail.indexOf("\" title"));
                        break;
                    }
                }
                int leftTopPointX = Integer.valueOf(subchartRec.split(",")[0]);
                int leftTopPointY = Integer.valueOf(subchartRec.split(",")[1]);
                int rightBottomPointX = Integer.valueOf(subchartRec.split(",")[2]);
                int rightBottomPointY = Integer.valueOf(subchartRec.split(",")[3]);
                atPoint.x = leftTopPointX + (rightBottomPointX-leftTopPointX)/2;
                atPoint.y = leftTopPointY + (rightBottomPointY-leftTopPointY)/2;
                to.click(atPoint);
        	} else {
        		throw new FindTimeoutException("请输入合法的脚本格式：字段行和字段列可均为空或者均不为空！！！");
        	}
        } else if (a[3].equals("标签")) {

            if (a[6].equals("返回")) {
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[6]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[6] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[6], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[6], compNm.get(0));
                        compNmCacheAdd.put(a[6], compNm.get(0));
                        a[6] = compNm.get(0);
                    }
                }
                GuiTestObject to = getActionButton(a[6]);
                to.click();
            } else {
                 if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[6]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[6] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[6], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[6], compNm.get(0));
                        compNmCacheAdd.put(a[6], compNm.get(0));
                        a[6] = compNm.get(0);
                    }
                }
                GuiTestObject to = getLabel(a);
                to.click();
            }
        } else if (a[3].equals("列表")) {
            GuiSubitemTestObject to = getList(a);
            if(!(envLang == null || "".equals(envLang)) && atText(a[6]) == null) {
                String compNmInCache = compNmCache.get(a[6]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[6] = compNmInCache;
                } else {
                    if (a[0] == null || "".equals(a[0])) {
                        compNm = getTransNames(envLang, a[6], null);
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[6], module);
                    }
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[6], compNm.get(0));
                    compNmCacheAdd.put(a[6], compNm.get(0));
                    a[6] = compNm.get(0);
                }
            }
            to.click(atText(a[6]));
        } else if (a[3].equals("注销")) {
            inputKeyF10();
        } else if (a[3].equals("关闭窗口")) {
            closeWindow();
        } else if (a[3].equals("登录")) {
            if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText", "登录NC系统")));
            } else if ("2".equals(envLang)){
                to = JavaDomain().find(atList(atDescendant("uIClassID", "ButtonUI", "toolTipText",
                        "Log into NC System")));
            }
            ((GuiTestObject) to[0]).click();

            String pwdLabel = "新密码确认";
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(pwdLabel);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    pwdLabel = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, pwdLabel, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(pwdLabel, compNm.get(0));
                    compNmCacheAdd.put(pwdLabel, compNm.get(0));
                    pwdLabel = compNm.get(0);
                }
            }
            GuiTestObject lable = getLabelByName(pwdLabel);
            int st = 0;
            while(lable == null) {
            	sleep(1);
            	lable = getLabelByName(pwdLabel);
            	if(st++ == 2) {
            		break;
            	}
            }
            if (lable != null) {
                logInfo("登录过程中有更新密码提示！");
                String cancelBtn = "取消";
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(cancelBtn);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        cancelBtn = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, cancelBtn, module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(cancelBtn, compNm.get(0));
                        compNmCacheAdd.put(cancelBtn, compNm.get(0));
                        cancelBtn = compNm.get(0);
                    }
                }
                GuiTestObject cancel = getButtonByName(cancelBtn);
                if (cancel != null) {
                    cancel.click();
                }
            }
            sleep(1);
            getSercherField();
        } else if (a[3].equals("增行")) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }

            if (a[4].equals("0")) {
                GuiTestObject to = getButton(a);
                to.click();
            } else if (a[4].equals("n")) {
                GuiTestObject to = getActionButton();
                to.click();
            } else {
                IScreen is = getRootTestObject().getScreen();
                if (a[4] == null || a[4].equals("") || a[4].equals("0")) {
                    is.click(getTabbedPaneButton(0));
                } else {
                    is.click(getTabbedPaneButton(Integer.parseInt(a[4])));
                }
            }
        } else if (!(a[3] == null || "".equals(a[3]))
                && a[4].equals("增行")) {
            if(!(envLang == null || "".equals(envLang))) {
                String module;
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }

                compNmInCache = compNmCache.get(a[4]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[4] = compNmInCache;
                } else {
                    module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[4], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[4], compNm.get(0));
                    compNmCacheAdd.put(a[4], compNm.get(0));
                    a[4] = compNm.get(0);
                }
            }
            if (a[6].equals("0")) {
                a[3] = a[4];
                GuiTestObject to = getButton(a);
                to.click();
            } else if (a[6].equals("n")) {
                GuiTestObject to = getActionButton();
                to.click();
            } else {
                IScreen is = getRootTestObject().getScreen();
                if (a[6] == null || a[6].equals("") || a[6].equals("0")) {
                    is.click(getTabbedPaneButtonByTabbedPaneName(a[3],0));
                } else {
                    is.click(getTabbedPaneButtonByTabbedPaneName(a[3],Integer.parseInt(a[6])));
                }
            }
        } else if (a[3].equals("切换业务日期")){

            GuiTestObject to = getBussinessButton();
            to.click();
            IScreen is = getRootTestObject().getScreen();
            is.inputKeys("{ExtUp}");
            is.inputKeys("{ExtUp}");
            is.inputKeys("{ENTER}");
            sleep(1);

        } else if (a[3].equals("参照")){
            GuiTestObject to = getRefButton(a);
            to.click();
            sleep(1);
        } else if (a[3].equals("单元格")) {
            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            Object[] cs =  {rowIndex, colIndex};

            GuiTestObject to = getCellsPane();
            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");
            selectModel.invoke("setAnchorCell", "(II)V", cs);
            Object[] cellRectArgs = {selectModel.invoke("getAnchorCell"),Boolean.valueOf(false)};
            Rectangle rect = (Rectangle)to.invoke("getCellRect","(Lcom/ufsoft/table/IArea;Z)Ljava/awt/Rectangle",
                    cellRectArgs);

            Point cellsPanePoint = (Point) to.getProperty("locationOnScreen");

            Point clickPoint = new Point();
            if(a[6] != null && !"".equals(a[6]) && isNumber(a[6])) {
                int level = Integer.valueOf(a[6]);
                clickPoint.x = cellsPanePoint.x + rect.x + 15 * level + 15;
            } else {
                clickPoint.x = cellsPanePoint.x + rect.x + rect.width/2;
            }
            clickPoint.y = cellsPanePoint.y + rect.y + rect.height/2;

            clickScreenPoint(clickPoint);
        } else if (a[3].equals("流程图")) {
            if(a[4] == null || "".equals(a[4]) || a[5] == null || "".equals(a[5])) {
                logError("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
            }

            GuiTestObject to = getMxGraphComponent(a);
            GuiSubitemTestObject container = (GuiSubitemTestObject)to.invoke("getGraphContainer");
            TestObject graph = (TestObject)container.invoke("getGraph");
            Object[] obj = {graph.invoke("getDefaultParent")};
            Object[] vertexObjects = (Object[])graph.invoke(
                    "getChildVertices","(Ljava/lang/Object;)[Ljava/lang/Object",obj);
            boolean click = false;
            for (Object o : vertexObjects) {
                Object[] objCell = {o};
                graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
                TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
                        "getBillFlowModel")).invoke("getSelectedData"));
                String code = selectedData.invoke("getBillCode").toString();
                String type = selectedData.invoke("getBillTypeName").toString();
                if(a[4].equals(type) && a[5].equals(code)) {
                    TestObject mxRec = (TestObject) graph.invoke(
                            "getCellBounds","(Ljava/lang/Object;)Lcom/mxgraph/util/mxRectangle",objCell);
                    Rectangle rec = (Rectangle) mxRec.invoke("getRectangle");
                    to.click(atPoint(rec.x + rec.width/2, rec.y + rec.height + 10));
                    click = true;
                    break;
                }
            }
            if(!click) {
                logError("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
            }
        } else if (a[3].equals("流程图下游")) {
            if(a[4] == null || "".equals(a[4]) || a[5] == null || "".equals(a[5])) {
                logError("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
            }

            GuiTestObject to = getMxGraphComponent(a);
            GuiSubitemTestObject container = (GuiSubitemTestObject)to.invoke("getGraphContainer");
            TestObject graph = (TestObject)container.invoke("getGraph");
            Object[] obj = {graph.invoke("getDefaultParent")};
            Object[] vertexObjects = (Object[])graph.invoke(
                    "getChildVertices","(Ljava/lang/Object;)[Ljava/lang/Object",obj);
            boolean click = false;
            Object forwardObj = null;
            for (Object o : vertexObjects) {
                Object[] objCell = {o};
                graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
                TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
                        "getBillFlowModel")).invoke("getSelectedData"));
                String code = selectedData.invoke("getBillCode").toString();
                String type = selectedData.invoke("getBillTypeName").toString();
                if(a[4].equals(type) && a[5].equals(code)) {
                	TestObject lightBillVO = (TestObject)selectedData.invoke("getLightBillVO");
                    Object[] forwardNodes = (Object[]) lightBillVO.invoke("getForwardBillVOs");
                    if(forwardNodes == null || forwardNodes.length == 0) {
                    	throw new FindTimeoutException("当前单据未找到下游单据！");
                    } else {
                    	if(a[6] != null && !"".equals(a[6])) {
                    		//type and index.
                    		if(a[6].split("/").length == 2) {
                    			List<Object> filter = new ArrayList<Object>(0);
                    			for(Object forwardNode : forwardNodes) {
                    				String typeForwardNode = ((TestObject)forwardNode).invoke("getTypeName").toString();
                    				if(a[6].split("/")[0].equals(typeForwardNode)) {
                    					filter.add(forwardNode);
                    				}
                    			}
                    			// No bill of the current type.
                    			if(filter.size() == 0) {
                    				throw new FindTimeoutException(String.format(
                    						"当前单据未找到指定类型为【%s】的下游单据！", a[6].split("/")[0]));
                    			}

                    			// the index of user given is bigger.
                    			if(isNumber(a[6].split("/")[1])
                    					&& Integer.valueOf(a[6].split("/")[1]) <= filter.size()) {
                    				forwardObj = filter.get(Integer.valueOf(a[6].split("/")[1]) - 1);
                    			} else {
                    				forwardObj = filter.get(0);
                    			}
                    			break;
                    		//Only index.
                    		} else if(isNumber(a[6])) {
                    			if(Integer.valueOf(a[6]) > forwardNodes.length) {
                    				throw new FindTimeoutException("指定索引超出下游单据的个数，请修改！");
                    			} else {
                    				forwardObj = forwardNodes[Integer.valueOf(a[6]) - 1];
                    				break;
                    			}
                    		// Only type.
                    		} else {
                    			List<Object> filter = new ArrayList<Object>(0);
                    			for(Object forwardNode : forwardNodes) {
                    				String typeForwardNode = ((TestObject)forwardNode).invoke("getTypeName").toString();
                    				if(a[6].split("/")[0].equals(typeForwardNode)) {
                    					filter.add(forwardNode);
                    				}
                    			}

                    			if(filter.size() == 0) {
                    				throw new FindTimeoutException(String.format(
                    						"当前单据未找到指定类型为【%s】的下游单据！", a[6].split("/")[0]));
                    			}
                    			forwardObj = filter.get(0);
                    			break;
                    		}
                    	} else {
                    		forwardObj = forwardNodes[0];
                			break;
                    	}
                    }
                }
            }

            try{            	
            	String forwardCode = ((TestObject)forwardObj).invoke("getCode").toString();
            	String forwardType = ((TestObject)forwardObj).invoke("getTypeName").toString();
            	for (Object o : vertexObjects) {
            		Object[] objCell = {o};
            		graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
            		TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
            		"getBillFlowModel")).invoke("getSelectedData"));
            		String code = selectedData.invoke("getBillCode").toString();
            		String type = selectedData.invoke("getBillTypeName").toString();
            		if(forwardType.equals(type) && forwardCode.equals(code)) {
            			TestObject mxRec = (TestObject) graph.invoke(
            					"getCellBounds","(Ljava/lang/Object;)Lcom/mxgraph/util/mxRectangle",objCell);
            			Rectangle rec = (Rectangle) mxRec.invoke("getRectangle");
            			to.click(atPoint(rec.x + rec.width/2, rec.y + rec.height + 10));
            			click = true;
            			break;
            		}
            	}
            } catch (NoSuchMethodError ex) {
            	throw new FindTimeoutException("指定的单据未找到，正确格式：字段行-单据类型、字段列-单据号、值列：索引");
            }

            if(!click) {
                logError("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
            }
        } else if (a[3].equals("流程图上游")) {
            if(a[4] == null || "".equals(a[4]) || a[5] == null || "".equals(a[5])) {
                logError("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
            }

            GuiTestObject to = getMxGraphComponent(a);
            GuiSubitemTestObject container = (GuiSubitemTestObject)to.invoke("getGraphContainer");
            TestObject graph = (TestObject)container.invoke("getGraph");
            Object[] obj = {graph.invoke("getDefaultParent")};
            Object[] vertexObjects = (Object[])graph.invoke(
                    "getChildVertices","(Ljava/lang/Object;)[Ljava/lang/Object",obj);
            boolean click = false;
            Object sourceObj = null;
            for (Object o : vertexObjects) {
                Object[] objCell = {o};
                graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
                TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
                        "getBillFlowModel")).invoke("getSelectedData"));
                String code = selectedData.invoke("getBillCode").toString();
                String type = selectedData.invoke("getBillTypeName").toString();
                if(a[4].equals(type) && a[5].equals(code)) {
                	TestObject lightBillVO = (TestObject)selectedData.invoke("getLightBillVO");
                    Object[] sourceNodes = (Object[]) lightBillVO.invoke("getSourceBillVOs");
                    if(sourceNodes == null || sourceNodes.length == 0) {
                    	throw new FindTimeoutException("当前单据未找到上游单据！");
                    } else {
                    	if(a[6] != null && !"".equals(a[6])) {
                    		//type and index.
                    		if(a[6].split("/").length == 2) {
                    			List<Object> filter = new ArrayList<Object>(0);
                    			for(Object sourceNode : sourceNodes) {
                    				String typeSourceNode = ((TestObject)sourceNode).invoke("getTypeName").toString();
                    				if(a[6].split("/")[0].equals(typeSourceNode)) {
                    					filter.add(sourceNode);
                    				}
                    			}
                    			// No bill of the current type.
                    			if(filter.size() == 0) {
                    				throw new FindTimeoutException(String.format(
                    						"当前单据未找到指定类型为【%s】的上游单据！", a[6].split("/")[0]));
                    			}

                    			// the index of user given is bigger.
                    			if(isNumber(a[6].split("/")[1])
                    					&& Integer.valueOf(a[6].split("/")[1]) <= filter.size()) {
                    				sourceObj = filter.get(Integer.valueOf(a[6].split("/")[1]) - 1);
                    			} else {
                    				sourceObj = filter.get(0);
                    			}
                    			break;
                    		//Only index.
                    		} else if(isNumber(a[6])) {
                    			if(Integer.valueOf(a[6]) > sourceNodes.length) {
                    				throw new FindTimeoutException("指定索引超出上游单据的个数，请修改！");
                    			} else {
                    				sourceObj = sourceNodes[Integer.valueOf(a[6]) - 1];
                    				break;
                    			}
                    		// Only type.
                    		} else {
                    			List<Object> filter = new ArrayList<Object>(0);
                    			for(Object sourceNode : sourceNodes) {
                    				String typeSourceNode = ((TestObject)sourceNode).invoke("getTypeName").toString();
                    				if(a[6].split("/")[0].equals(typeSourceNode)) {
                    					filter.add(sourceNode);
                    				}
                    			}

                    			if(filter.size() == 0) {
                    				throw new FindTimeoutException(String.format(
                    						"当前单据未找到指定类型为【%s】的上游单据！", a[6].split("/")[0]));
                    			}
                    			sourceObj = filter.get(0);
                    			break;
                    		}
                    	} else {
                    		sourceObj = sourceNodes[0];
                			break;
                    	}
                    }
                }
            }
            try{            	
            	String sourceCode = ((TestObject)sourceObj).invoke("getCode").toString();
            	String sourceType = ((TestObject)sourceObj).invoke("getTypeName").toString();
            	for (Object o : vertexObjects) {
            		Object[] objCell = {o};
            		graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
            		TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
            		"getBillFlowModel")).invoke("getSelectedData"));
            		String code = selectedData.invoke("getBillCode").toString();
            		String type = selectedData.invoke("getBillTypeName").toString();
            		if(sourceType.equals(type) && sourceCode.equals(code)) {
            			TestObject mxRec = (TestObject) graph.invoke(
            					"getCellBounds","(Ljava/lang/Object;)Lcom/mxgraph/util/mxRectangle",objCell);
            			Rectangle rec = (Rectangle) mxRec.invoke("getRectangle");
            			to.click(atPoint(rec.x + rec.width/2, rec.y + rec.height + 10));
            			click = true;
            			break;
            		}
            	}
            } catch (NoSuchMethodError ex) {
            	throw new FindTimeoutException("指定的单据未找到，正确格式：字段行-单据类型、字段列-单据号、值列：索引");
            }

            if(!click) {
                logError("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
            }
        } else if (a[3].indexOf("图标") != -1) {
            clickIcon(a);
        } else if (a[3].indexOf("下拉按钮") != -1) {
            if(a[4].indexOf("粘贴") != -1
                    || a[4].indexOf("背景色") != -1
                    ||a[4].indexOf("边框") != -1
                    ||a[4].indexOf("前景色") != -1) {
                GuiTestObject to = getComboBoxIcon(a);
                Point toP = (Point) to.getProperty("locationOnScreen");
                int width = Integer.parseInt(to.getProperty("width").toString());
                int height = Integer.parseInt(to.getProperty("height").toString());
                if(!to.hasFocus()) {
                    to.hover();
                }
                clickPoint(atPoint(toP.x + width - 5, toP.y + height/2));
            }
        } else if("页签按钮".equals(a[3])) {
            GuiTestObject tabbedPane = null;
            if(!(envLang == null || "".equals(envLang))) {
                if (!"".equals(a[4]) && a[4] != null) {
                    String compNmInCache = compNmCache.get(a[4]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[4] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[4], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[4], compNm.get(0));
                        compNmCacheAdd.put(a[4], compNm.get(0));
                        a[4] = compNm.get(0);
                    }
                }
                if (!"".equals(a[5]) && a[5] != null) {
                    String compNmInCache = compNmCache.get(a[5]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[5] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[5], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[5], compNm.get(0));
                        compNmCacheAdd.put(a[5], compNm.get(0));
                        a[5] = compNm.get(0);
                    }
                }
            }
            tabbedPane = getTabbedPaneForToolBar(a);
            if(tabbedPane == null) {
                throw new FindTimeoutException("未找到包含按钮的页签！");
            }

            boolean hasBtn = false;
            try{
                GuiTestObject actionToolBar = (GuiTestObject) tabbedPane.invoke("getToolBar");
                TestObject[] to = actionToolBar.getChildren();
                for (int i = 0; i < to.length; i++) {
                    GuiTestObject tool = (GuiTestObject)to[i];
                    Object toolTip = tool.invoke("getToolTipText");
                    if(toolTip != null && toolTip.toString().indexOf(a[5]) != -1
                            && (Boolean)tool.getProperty("enabled")) {
                        hasBtn = true;
                        tool.click();
                        break;
                    }
                }
            } catch (Exception e) {
                throw new FindTimeoutException(String.format(
                        "指定【%s】页签未找到名字为【%s】的按钮！或者按钮是不可编辑状态，请检查按钮的名字！",a[4], a[5]));
            }
            if(!hasBtn) {
                throw new FindTimeoutException(String.format(
                        "指定【%s】页签未找到名字为【%s】的按钮！或者按钮是不可编辑状态，请检查按钮的名字！",a[4], a[5]));
            }

            if (a[5].indexOf("保存") != -1) {
                sleep(1.5);
            }
        } else if (a[3].equals("工具箱")||a[3].equals("属性编辑器")) {
            if(a[3].equals("工具箱")){
                a[3] = "<html><right>工<br>具<br>箱";
            } else {
                a[3] = "<html><right>属<br>性<br>编<br>辑<br>器";
            }
            GuiTestObject to = getToggleButton(a);
            if(a[3].equals("<html><right>工<br>具<br>箱")){
                a[3] = "工具箱";
            } else {
                a[3] = "属性编辑器";
            }
            to.click();
        } else if (a[3].equals("审批流活动")) {
            GuiTestObject to = getProcessGragh(a);
            TestObject flowGraphManager = (TestObject)to.invoke("getFlowGraphManager");
            TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
            Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
            Point atPoint = new Point();
            for(Object actChild : actChildren) {
                Object tooltip = ((TestObject)actChild).invoke("getTooltip");
                if(tooltip != null) {
                    String strTooltip = tooltip.toString();
                    if(strTooltip.indexOf(a[5]) == -1) {
                        continue;
                    }
                    strTooltip = strTooltip.replaceAll("<html>", "").replace("</html>", "").replace(
                            "<strong>", "").replace("</strong>", "").replace("<br>", "");
                    String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                            strTooltip.indexOf("描述:")).trim();
                    String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                            strTooltip.indexOf("合并方式: ")).trim();
                    if(name.equals(a[5]) && type.equals(a[4])) {
                        Object[] recArgs = {actChild};
                        TestObject rec2D = (TestObject) to.invoke(
                                "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
                        Object x = rec2D.invoke("getX");
                        Object y = rec2D.invoke("getY");
                        Object height = rec2D.invoke("getHeight");
                        Object width = rec2D.invoke("getWidth");
                        atPoint.x = (int) (Double.valueOf(x.toString()) + Double.valueOf(width.toString())/2);
                        atPoint.y = (int) (Double.valueOf(y.toString()) + Double.valueOf(height.toString())/2);
                        break;
                    }
                }
            }
            to.click(atPoint);
        } else if (a[3].equals("审批流转移")) {
            GuiTestObject to = getProcessGragh(a);

            Point atPoint = new Point();
            TestObject flowGraphManager = (TestObject)to.invoke("getFlowGraphManager");
            if(a[4].equals("开始")) {
                if(a[5].split("/").length != 2) {
                    throw new FindTimeoutException("请指定精确的转移终点，格式【类型/名称】。");
                }
                Object[] allTranArray = new Object[0];
                String endType = a[5].split("/")[0];
                String endName = a[5].split("/")[1];

                //start activity location info.
                TestObject activityStart = (TestObject) flowGraphManager.invoke("getStart");
                TestObject outTrans = (TestObject) activityStart.invoke("getOutgoingTransitions");
                allTranArray = (Object[]) outTrans.invoke("toArray");

                if(allTranArray != null) {
                    for(Object tran : allTranArray) {
                        TestObject tranTo = (TestObject) tran;
                        TestObject targetActivity = (TestObject) tranTo.invoke("getTargetActivity");
                        Object tooltip = targetActivity.invoke("getTooltip");
                        if(tooltip != null) {
                            String strTooltip = tooltip.toString();
                            if(strTooltip.indexOf(endName) == -1) {
                                continue;
                            }
                            strTooltip = strTooltip.replaceAll("<html>", "").replaceAll("</html>", "").replaceAll(
                                    "<strong>", "").replaceAll("</strong>", "").replaceAll("<br>", "");
                            String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                    strTooltip.indexOf("描述:")).trim();
                            String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                    strTooltip.indexOf("合并方式: ")).trim();
                            if(name.equals(endName) && type.equals(endType)) {
                                Object[] recArgs = {tran};
                                Rectangle rec = (Rectangle) to.invoke(
                                        "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
                                atPoint.x = rec.x + rec.width/2;
                                atPoint.y = rec.y + rec.height/2;
                                break;
                            }
                        }
                    }
                }
            } else if (a[5].equals("结束")) {
                if(a[4].split("/").length != 2) {
                    throw new FindTimeoutException("请指定精确的转移终点，格式【类型/名称】。");
                }
                Object[] allTranArray = new Object[0];
                String startType = a[4].split("/")[0];
                String startName = a[4].split("/")[1];

                //end activity location info.
                TestObject activityEnds = (TestObject) flowGraphManager.invoke("getEnds");
                if(activityEnds != null) {
                    Object[] endChildren = (Object[]) activityEnds.invoke("toArray");
                    if(endChildren != null) {
                        TestObject activityEnd = (TestObject) endChildren[0];
                        TestObject incomeTrans = (TestObject) activityEnd.invoke("getIncomingTransitions");
                        allTranArray = (Object[]) incomeTrans.invoke("toArray");
                        if(allTranArray != null) {
                            for(Object tran : allTranArray) {
                                TestObject tranTo = (TestObject) tran;
                                TestObject sourceActivity = (TestObject) tranTo.invoke("getSourceActivity");
                                Object tooltip = sourceActivity.invoke("getTooltip");
                                if(tooltip != null) {
                                    String strTooltip = tooltip.toString();
                                    if(strTooltip.indexOf(startName) == -1) {
                                        continue;
                                    }
                                    strTooltip = strTooltip.replaceAll("<html>", "").replaceAll(
                                            "</html>", "").replaceAll("<strong>", "").replaceAll(
                                                    "</strong>", "").replaceAll("<br>", "");
                                    String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                            strTooltip.indexOf("描述:")).trim();
                                    String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                            strTooltip.indexOf("合并方式: ")).trim();
                                    if(name.equals(startName) && type.equals(startType)) {
                                        Object[] recArgs = {tran};
                                        Rectangle rec = (Rectangle) to.invoke("getCellBounds",
                                                "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                        atPoint.x = rec.x + rec.width/2;
                                        atPoint.y = rec.y + rec.height/2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if(a[4].split("/").length != 2 || a[5].split("/").length != 2) {
                    throw new FindTimeoutException("请指定精确的转移起点、终点，格式【类型/名称】。");
                }
                Object[] allTranArray = new Object[0];
                String startType = a[4].split("/")[0];
                String startName = a[4].split("/")[1];
                String endType = a[5].split("/")[0];
                String endName = a[5].split("/")[1];

                //end activity location info.
                TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
                Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
                TestObject activityEnd = null;
                for(Object actChild : actChildren) {
                    Object tooltip = ((TestObject)actChild).invoke("getTooltip");
                    if(tooltip != null) {
                        String strTooltip = tooltip.toString();
                        if(strTooltip.indexOf(endName) == -1) {
                            continue;
                        }
                        strTooltip = strTooltip.replaceAll("<html>", "").replace("</html>", "").replace(
                                "<strong>", "").replace("</strong>", "").replace("<br>", "");
                        String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                strTooltip.indexOf("描述:")).trim();
                        String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                strTooltip.indexOf("合并方式: ")).trim();
                        if(name.equals(endName) && type.equals(endType)) {
                            activityEnd = (TestObject) actChild;
                            break;
                        }
                    }
                }

                if(activityEnd != null) {
                    TestObject incomeTrans = (TestObject) activityEnd.invoke("getIncomingTransitions");
                    allTranArray = (Object[]) incomeTrans.invoke("toArray");
                    if(allTranArray != null) {
                        for(Object tran : allTranArray) {
                            TestObject tranTo = (TestObject) tran;
                            TestObject sourceActivity = (TestObject) tranTo.invoke("getSourceActivity");
                            Object tooltip = sourceActivity.invoke("getTooltip");
                            if(tooltip != null) {
                                String strTooltip = tooltip.toString();
                                if(strTooltip.indexOf(startName) == -1) {
                                    continue;
                                }
                                strTooltip = strTooltip.replaceAll("<html>", "").replaceAll(
                                        "</html>", "").replaceAll("<strong>", "").replaceAll(
                                                "</strong>", "").replaceAll("<br>", "");
                                String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                        strTooltip.indexOf("描述:")).trim();
                                String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                        strTooltip.indexOf("合并方式: ")).trim();
                                if(name.equals(startName) && type.equals(startType)) {
                                    Object[] recArgs = {tran};
                                    Rectangle rec = (Rectangle) to.invoke("getCellBounds",
                                            "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                    atPoint.x = rec.x + rec.width/2;
                                    atPoint.y = rec.y + rec.height/2;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            to.click(atPoint);
        } else {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }

            GuiTestObject to = getButton(a);

            if (to.getProperty("class").equals("nc.funcnode.ui.GroupButton"))
                to.click(atPoint(Integer.parseInt(to.getProperty("width")
                        .toString()) - 5, 10));
            else
                to.click();
        }

        if (envLang == null || "".equals(envLang) || "4".equals(envLang)) {
            if (a[3].indexOf("保存") != -1 || a[3].indexOf("确定") != -1
                    || a[3].indexOf("查询") != -1) {
                sleep(1.5);
            }
        } else if ("2".equals(envLang)) {
            if (a[3].indexOf("Save") != -1 || a[3].indexOf("OK") != -1
                    || a[3].indexOf("Query") != -1) {
                sleep(1.5);
            }
        }
        unregisterAll();
    }

    public void 单选(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }
        GuiTestObject to = getRadioButton(a);
        to.click();
        unregisterAll();
    }

    public void 单击右键(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("单元格")) {
            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            Object[] cs =  {rowIndex, colIndex};

            GuiTestObject to = getCellsPane();
            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");
            selectModel.invoke("setAnchorCell", "(II)V", cs);
            Object[] cellRectArgs = {selectModel.invoke("getAnchorCell"),Boolean.valueOf(false)};
            Rectangle rect = (Rectangle)to.invoke("getCellRect","(Lcom/ufsoft/table/IArea;Z)Ljava/awt/Rectangle",
                    cellRectArgs);

            Point cellsPanePoint = (Point) to.getProperty("locationOnScreen");

            Point clickPoint = new Point();
            clickPoint.x = cellsPanePoint.x + rect.x + rect.width/2;
            clickPoint.y = cellsPanePoint.y + rect.y + rect.height/2;

            rightClickScreenPoint(clickPoint);
        } else if (a[3].equals("区域")) {
            if(a[4].length() != 2) {
                logInfo("选中区域的开始位置格式不正确，正确格式类似于:A1");
                throw new FindTimeoutException("选中区域的开始位置格式不正确，正确格式类似于:A1");
            }

            if(a[5].length() != 2) {
                logInfo("选中区域的结束位置格式不正确，正确格式类似于:A1");
                throw new FindTimeoutException("选中区域的开始位置格式不正确，正确格式类似于:A1");
            }

            Integer colIndexStart = calcExcelColumnToIndex(String.valueOf(a[4].toCharArray()[0]));
            Integer rowIndexStart = Integer.valueOf(String.valueOf(a[4].substring(1))) - 1;
            Integer colIndexEnd = calcExcelColumnToIndex(String.valueOf(a[5].toCharArray()[0]));
            Integer rowIndexEnd = Integer.valueOf(String.valueOf(a[5].substring(1))) - 1;
            GuiTestObject to = getCellsPane();

            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");

            Object[] start =  {rowIndexStart, colIndexStart};
            Object[] indecies =  {rowIndexStart, colIndexStart, colIndexEnd - colIndexStart + 1,
                    rowIndexEnd - rowIndexStart + 1};
            selectModel.invoke("setAnchorCell", "(II)V", start);
            TestObject areaPosition = (TestObject)selectModel.invoke("getSelectedArea");
            Object[] areaP = {areaPosition.invoke("getInstance","(IIII)Lcom/ufsoft/table/AreaPosition;",indecies)};
            selectModel.invoke("setSelectedArea", "(Lcom/ufsoft/table/AreaPosition;)V", areaP);

            Object[] cellRectArgs = {selectModel.invoke("getSelectedArea"),Boolean.valueOf(false)};
            Rectangle rect = (Rectangle)to.invoke("getCellRect","(Lcom/ufsoft/table/IArea;Z)Ljava/awt/Rectangle",
                    cellRectArgs);

            Point cellsPanePoint = (Point) to.getProperty("locationOnScreen");

            Point clickPoint = new Point();
            clickPoint.x = cellsPanePoint.x + rect.x + rect.width/2;
            clickPoint.y = cellsPanePoint.y + rect.y + rect.height/2;

            rightClickScreenPoint(clickPoint);
        } else if (a[3].equals("透视图")) {
        	GuiTestObject to = getPerspectiveChart(a);
        	Point atPoint = new Point();
        	if("".equals(a[4]) && "".equals(a[5])) {        		
        		to.click(new MouseModifiers(MouseModifiers.MOUSE_RIGHT));
        	} else if(!"".equals(a[4]) && !"".equals(a[5])) {
        		String[] chartDetails = to.getProperty("imageMap").toString().split("\\n");
                String subchartRec = "";
                for(String detail : chartDetails) {
                    if(detail.indexOf(a[4]) != -1 && detail.indexOf(a[5]) != -1) {
                    	subchartRec = detail.substring(detail.indexOf("coords=\"") + 8, detail.indexOf("\" title"));
                        break;
                    }
                }
                int leftTopPointX = Integer.valueOf(subchartRec.split(",")[0]);
                int leftTopPointY = Integer.valueOf(subchartRec.split(",")[1]);
                int rightBottomPointX = Integer.valueOf(subchartRec.split(",")[2]);
                int rightBottomPointY = Integer.valueOf(subchartRec.split(",")[3]);
                atPoint.x = leftTopPointX + (rightBottomPointX-leftTopPointX)/2;
                atPoint.y = leftTopPointY + (rightBottomPointY-leftTopPointY)/2;
                to.click(new MouseModifiers(MouseModifiers.MOUSE_RIGHT),atPoint);
        	} else {
        		throw new FindTimeoutException("请输入合法的脚本格式：字段行和字段列可均为空或者均不为空！！！");
        	}
        }
        unregisterAll();
    }

    public void 双击(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("") || a[3] == null) {
            Point p = getScreen().getMousePosition();
            IScreen is = getRootTestObject().getScreen();
            is.doubleClick(p);
            return;
        }
        if (a[3].indexOf("表体") != -1) {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }

            GuiSubitemTestObject to = getTable(a);
            doubleClickTable(to, a, envLang);
        } else if (a[3].equals("列表")) {
            GuiSubitemTestObject to = getList(a);
            to.doubleClick(atText(a[6]));
        } else if (a[3].equals("单元格")) {
            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            Object[] cs =  {rowIndex, colIndex};

            GuiTestObject to = getCellsPane();
            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");
            selectModel.invoke("setAnchorCell", "(II)V", cs);
            Object[] cellRectArgs = {selectModel.invoke("getAnchorCell"),Boolean.valueOf(false)};
            Rectangle rect = (Rectangle)to.invoke("getCellRect","(Lcom/ufsoft/table/IArea;Z)Ljava/awt/Rectangle",
                    cellRectArgs);

            Point cellsPanePoint = (Point) to.getProperty("locationOnScreen");

            Point clickPoint = new Point();
            clickPoint.x = cellsPanePoint.x + rect.x + rect.width/2;
            clickPoint.y = cellsPanePoint.y + rect.y + rect.height/2;

            doubleClickScreenPoint(clickPoint);
        } else if (a[3].equals("流程图")) {
            if(a[4] == null || "".equals(a[4]) || a[5] == null || "".equals(a[5])) {
                logError("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("请指定的单据节点信息，正确格式：字段行-单据类型、字段列-单据号");
            }
            if ("数据库".equals(a[5])) {
                // userName, password, IP, port, database
                String username = a[9].trim();
                String password = a[10].trim();
                String ip = a[11].trim();
                String port = a[12].trim();
                String database = a[13].trim();
                String sql = a[14].trim();
                if ("".equals(username) || username == null) {
                    logError("********检查点验证失败！缺少数据库用户名！");
                    throw new FindTimeoutException("********检查点验证失败！缺少数据库用户名！");
                }
                if ("".equals(password) || password == null) {
                    logError("********检查点验证失败！缺少数据库密码！");
                    throw new FindTimeoutException("********检查点验证失败！缺少数据库密码！");
                }
                if ("".equals(ip) || ip == null) {
                    logError("********检查点验证失败！缺少数据库IP！");
                    throw new FindTimeoutException("********检查点验证失败！缺少数据库IP！");
                }
                if ("".equals(port) || port == null) {
                    logError("********检查点验证失败！缺少数据库地址端口！");
                    throw new FindTimeoutException("********检查点验证失败！缺少数据库地址端口！");
                }
                if ("".equals(database) || database == null) {
                    logError("********检查点验证失败！缺少数据库名！");
                    throw new FindTimeoutException("********检查点验证失败！缺少数据库名！");
                }
                if ("".equals(sql) || sql == null) {
                    logError("********检查点验证失败！缺少SQL文！");
                    throw new FindTimeoutException("********检查点验证失败！缺少SQL文！");
                } else if(sql.charAt(sql.length() - 1) == ';' || sql.charAt(sql.length() - 1) == '；' ){
                    sql = sql.substring(0, sql.length() - 1);
                }
                ResultSet rs = null;

                String id = null;
                try {
                    DBMng dbm = DBMng.getInstance(username, password, ip,
                            port, database);
                    rs = dbm.executeQuery(sql);
                    int index = 1;
                    while (rs.next()) {
                        id = String.valueOf(rs.getObject(1));
                        if (a[6] == null || "".equals(a[6]) || index == Integer.valueOf(a[6])) {
                            break;
                        }
                        index++;
                    }

                    if ("".equals(id) || id == null) {
                        throw new FindTimeoutException("未查询到下游单据号，请检查SQL文！");
                    }

                    if (!(a[6] == null || "".equals(a[6])) && index > Integer.valueOf(a[6])) {
                        throw new FindTimeoutException("未查询到下游单据号，指定的索引超出查询结果。请检查值列的索引！");
                    }
                    dbm.close();
                } catch (SQLException e) {
                    System.err.println("Exception:" + e.getMessage());
                    logInfo("ExecuteLine sql" + e.getMessage());
                } catch (Exception e) {
                    logInfo("Exception:" + e.getMessage());
                    e.printStackTrace();
                }
                a[5] = id;
            }
            GuiTestObject to = getMxGraphComponent(a);
            GuiSubitemTestObject container = (GuiSubitemTestObject)to.invoke("getGraphContainer");
            TestObject graph = (TestObject)container.invoke("getGraph");
            Object[] obj = {graph.invoke("getDefaultParent")};
            Object[] vertexObjects = (Object[])graph.invoke(
                    "getChildVertices","(Ljava/lang/Object;)[Ljava/lang/Object",obj);
            boolean click = false;
            for (Object o : vertexObjects) {
                Object[] objCell = {o};
                graph.invoke("setSelectionCell", "(Ljava/lang/Object;)V", objCell);
                TestObject selectedData = (TestObject)(((TestObject)((TestObject)graph.invoke("getModel")).invoke(
                        "getBillFlowModel")).invoke("getSelectedData"));
                String code = selectedData.invoke("getBillCode").toString();
                String type = selectedData.invoke("getBillTypeName").toString();
                if(a[4].equals(type) && a[5].equals(code)) {
                    TestObject mxRec = (TestObject) graph.invoke(
                            "getCellBounds","(Ljava/lang/Object;)Lcom/mxgraph/util/mxRectangle",objCell);
                    Rectangle rec = (Rectangle) mxRec.invoke("getRectangle");
                    to.doubleClick(atPoint(rec.x + rec.width/2, rec.y + rec.height/2));
                    click = true;
                    break;
                }
            }
            if(!click) {
                logError("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
                throw new FindTimeoutException("指定的单据节点未找到，正确格式：字段行-单据类型、字段列-单据号");
            }
        }
        unregisterAll();
    }

    public void 等待(String[] a, String envLang) {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[4]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[4] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[4], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[4], compNm.get(0));
                compNmCacheAdd.put(a[4], compNm.get(0));
                a[4] = compNm.get(0);
            }
        }

        if (a[3].equals("按钮")) {

            to = JavaDomain().find(
                    atList(atDescendant("uIClassID", "ButtonUI", "text", a[4])));

            if (to.length == 0) {
                int i = 0;
                while (i++ == breakTime) {
                    sleep(1);
                    to = JavaDomain().find(
                            atDescendant("uIClassID", "ButtonUI", "text", a[4]));
                }
            }
            sleep(0.5);
        } else if (a[3].equals("标签")) {
            to = JavaDomain().find(
                    atDescendant("uIClassID", "LabelUI", "text", a[4]));
            while (to.length == 0) {
                sleep(1);
                to = JavaDomain().find(
                        atDescendant("uIClassID", "LabelUI", "text", a[4]));
            }
            sleep(0.5);
        } else {
            sleep(Integer.parseInt(a[6]));
        }
        unregisterAll();
    }

    public void 关闭(String[] a, String envLang) throws FindTimeoutException {
        if(a[3].equals("对话框")) {
            GuiTestObject to = getCloseIconInSuspendDlg();
            to.click();
        } else if (a[3].equals("窗口")) {
            GuiTestObject to = getCloseIconOfPopupWindow(a);
            to.click();
        } else if (a[3].equals("顶级窗口")) {
        	try {        		
        		TopLevelTestObject top = getTop(JavaDomain());
        		top.close();
        	} catch (Exception e) {
        		throw new FindTimeoutException(String.format(
        				"获取顶级窗口失败：%s", e.getStackTrace().toString()));
        	}
        } else if (!(a[6] == null || "".equals(a[6]))) {
            FrameTestObject frame = getFrame(a[6]);
            frame.close();
        }
        unregisterAll();
    }

    // ********************************************节点关闭******************
    public void 关闭节点(String[] a, String envLang) throws Exception {
        int st = 0;
        while (getTopNum() > 1) {
            sleep(1);
            if (st++ == 15)
                break;
        }
        GuiSubitemTestObject to = null;

        String store = envLang;
        //对原本是英文的脚本，即使控制界面设置了语言，值列可设置值来覆盖控制界面的语言设置。
        if("1".equals(a[6]) && ("".equals(envLang) || "1".equals(envLang))) {
            envLang = "2";
        }
        //空/1:简体
        //2:English
        //3:French
        //4:繁体
        if (envLang == null || "".equals(envLang) || "4".equals(envLang)) {
            to = getTabbedPane("消息中心");
        } else if ("2".equals(envLang)){
            to = getTabbedPane("Message Center");
        }
        int cont = Integer.parseInt(to.getProperty("tabCount").toString());
        if (cont > 2) {
            Object[] index = { cont - 1 };
            java.awt.Rectangle r = (java.awt.Rectangle) to.invoke(
                    "getBoundsAt", "(I)Ljava/awt/Rectangle", index);
            to.click(atPoint((r.x + (r.width - 18)), 18));
            sleep(1);
        }
        //Check closed.
        try {
            if (envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                to = getTabbedPane("消息中心");
            } else if ("2".equals(envLang)){
                to = getTabbedPane("Message Center");
            }
            int closedCount = Integer.parseInt(to.getProperty("tabCount").toString());

            if (closedCount >= cont) {
                throw new FindTimeoutException(
                        String.format("提示信息：关闭节点失败！请查证是否有未保存或者未处理的动作！！"));
            }
        } catch (FindTimeoutException ex) {
            throw new FindTimeoutException(
                    String.format("提示信息：关闭节点失败！请查证是否有未保存或者未处理的动作！！"));
        }
        envLang = store;

        unregisterAll();
    }

    public void 关闭页签(String[] a, String envLang) throws Exception {
        int st = 0;
        while (getTopNum() > 1) {
            sleep(1);
            if (st++ == 15)
                break;
        }

        GuiSubitemTestObject to = null;
        String store = envLang;
        //对原本是英文的脚本，即使控制界面设置了语言，值列可设置值来覆盖控制界面的语言设置。
        if("1".equals(a[6]) && ("".equals(envLang) || "1".equals(envLang))) {
            envLang = "2";
        }

        if (envLang == null || "".equals(envLang) || "4".equals(envLang)) {
            to = getTabbedPane("消息中心");
        } else if("2".equals(envLang)){
            to = getTabbedPane("Message Center");
        }

        int cont = Integer.parseInt(to.getProperty("tabCount").toString());
        if (cont > 1) {
            Object[] index = { cont - 1 };
            java.awt.Rectangle r = (java.awt.Rectangle) to.invoke(
                    "getBoundsAt", "(I)Ljava/awt/Rectangle", index);
            to.click(atPoint((r.x + (r.width - 10)), 13));
            sleep(1);
        }
        try {
            if (envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                to = getTabbedPane("消息中心");
            } else if("2".equals(envLang)){
                to = getTabbedPane("Message Center");
            }

            int closedCount = Integer.parseInt(to.getProperty("tabCount").toString());

            if (closedCount >= cont) {
                throw new FindTimeoutException(
                        String.format("提示信息：关闭页签失败！请查证是否有未保存或者未处理的动作！！"));
            }
        } catch (FindTimeoutException ex) {
            throw new FindTimeoutException(
                    String.format("提示信息：关闭页签失败！请查证是否有未保存或者未处理的动作！！"));
        }
        envLang = store;
        unregisterAll();
    }

    public void 关闭网页(String[] a, String envLang) {
        BrowserTestObject bto = getWebPart();
        if (bto == null){
            IWindow[] windows = getTopWindowsWithCaptionPattern(".* New Century - .* Internet Explorer",true);
            boolean hasNewCentury = windows.length == 0 ? false : true;
            for(int i=0;i<windows.length;i++){
                windows[i].close();
            }

            windows = getTopWindowsWithCaptionPattern(".* 无法显示该网页 - .* Internet Explorer",true);
            for(int i=0;i<windows.length;i++){
                windows[i].close();
            }

            if (!hasNewCentury) {
                windows = getTopWindowsWithCaptionPattern(".* New Century - .* Internet Explorer",true);
                for(int i=0;i<windows.length;i++){
                    windows[i].close();
                }
            }
        } else {
            getWebPart().close();
        }
        unregisterAll();
    }

    public void 特殊单击(String[] a, String envLang) throws FindTimeoutException{
        if (a[3].equals("") || a[3] == null) {
            GuiTestObject to = getFirst(a);
            to.click();
            return;
        } else if (a[3].toString().indexOf("表体") != -1) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }

            GuiSubitemTestObject to = getTableTeshu(a);
            if (!isCoordinate(a[6])){
                clickTableContent(to, a);
            } else {
                if (!to.hasFocus()) {
                    to.hover();
                }
                clickScreenPoint(getCoordinate(a[6]));
            }
        } else if (a[3].equals("百叶窗")){
            if (!isCoordinate(a[6])) {
                logError("请指定坐标！");
            } else {
                GuiTestObject to = getDrawerBox();
                if (null == to){
                    logError("没有百叶窗");
                    sleep(1);
                    unregisterAll();
                    return;
                }
                if (!to.hasFocus()) {
                    to.hover();
                }
                clickScreenPoint(getCoordinate(a[6]));
            }
        } else {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }

            Property[] pp = { new Property("uIClassID", "ScrollPaneUI"),
                    new Property("name", a[3]),
                    new Property("showing", "true") };
            GuiTestObject to = (GuiTestObject) JavaDomain().find(
                    atDescendant(pp))[0];
            int row = Integer.parseInt(a[4]);
            to.click(atPoint(23, 15 * (row - 1) + 7));
        }
        sleep(1);
        unregisterAll();
    }

    public void 下翻页(String[] a, String envLang) throws FindTimeoutException{
        GuiSubitemTestObject to =  getScrollBar(a);
        if ("".equals(a[6]) || a[6] == null){
            logInfo("请指定翻页次数！");
            return;
        } else {
            int incream = 200;
            for (int i = 0; i < Integer.parseInt(a[6]); i++){
                to.setState(Action.vScroll(i * incream));
                sleep(1);
            }
        }
        unregisterAll();
    }

    public void 上翻页(String[] a, String envLang) throws FindTimeoutException{
        GuiSubitemTestObject to =  getScrollBar(a);
        to.setState(Action.vScroll(0));
        sleep(1);
        unregisterAll();
    }

    public void 右翻页(String[] a, String envLang) throws FindTimeoutException{
        GuiSubitemTestObject to =  getTableScrollBar(a);
        if ("".equals(a[6]) || a[6] == null){
            logInfo("请指定翻页次数！");
            return;
        } else {
            int incream = 200;
            for (int i = 0; i < Integer.parseInt(a[6]); i++){
                if(!to.hasFocus()) {
                    to.hover();
                }
                to.setState(Action.hScroll(i * incream));
                sleep(1);
            }
        }
        unregisterAll();
    }

    public void 左翻页(String[] a, String envLang) throws FindTimeoutException{
        GuiSubitemTestObject to =  getTableScrollBar(a);
        if(!to.hasFocus()) {
            to.hover();
        }
        to.setState(Action.hScroll(0));
        sleep(1);
        unregisterAll();
    }

    public void 拖动(String[] a, String envLang) throws FindTimeoutException {
        Point p1 = Mouse.getCursorPos();
        TestObject to = RootTestObject.getRootTestObject().objectAtPoint(p1);
        if (a[3].equals("列表")) {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[4]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[4] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[4], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[4], compNm.get(0));
                    compNmCacheAdd.put(a[4], compNm.get(0));
                    a[4] = compNm.get(0);
                }
            }

            Point p2 = getListByName(a).getScreenPoint();
            int index = Integer.parseInt(to.getProperty("selectedIndex")
                    .toString());
            ((GuiSubitemTestObject) to).dragToScreenPoint(atIndex(index), p2);
        }
        unregisterAll();
    }

    public void 添加(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("审批流")) {
            if("转移".equals(a[4])) {
                if("开始".equals(a[5])) {
                    GuiTestObject toTool = getToggleButtonOfTransition(a);
                    toTool.click();
                    GuiTestObject graph = getProcessGragh(a);
                    TestObject flowGraphManager = (TestObject)graph.invoke("getFlowGraphManager");
                    Point grapLocation = (Point) graph.getProperty("locationOnScreen");

                    //start activity location info.
                    Point startPoint = new Point();
                    TestObject activityStart = (TestObject) flowGraphManager.invoke("getStart");
                    if(activityStart != null) {
                        Object[] recArgs = {activityStart};
                        TestObject rec2D = (TestObject) graph.invoke(
                                "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
                        int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                        int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                        int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                        int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                        startPoint.x = x + width/2 + grapLocation.x;
                        startPoint.y = y + height/2 + grapLocation.y;
                    }
                    if(a[6].split("/").length != 2) {
                        throw new FindTimeoutException("请指定精确的转移终点，格式【类型/名称】。");
                    }
                    String endType = a[6].split("/")[0];
                    String endName = a[6].split("/")[1];

                    //other activity location info.
                    Point endPoint = new Point();
                    TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
                    if(activitiesNotStartEnd != null) {
                        Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
                        if(actChildren != null) {
                            for(Object actChild : actChildren) {
                                Object tooltip = ((TestObject)actChild).invoke("getTooltip");
                                if(tooltip != null) {
                                    String strTooltip = tooltip.toString();
                                    if(strTooltip.indexOf(endName) == -1) {
                                        continue;
                                    }
                                    strTooltip = strTooltip.replaceAll("<html>", "").replaceAll(
                                            "</html>", "").replaceAll("<strong>", "").replaceAll(
                                                    "</strong>", "").replaceAll("<br>", "");
                                    String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                            strTooltip.indexOf("描述:")).trim();
                                    String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                            strTooltip.indexOf("合并方式: ")).trim();
                                    if(name.equals(endName) && type.equals(endType)) {
                                        Object[] recArgs = {actChild};
                                        TestObject rec2D = (TestObject) graph.invoke("getCellBounds",
                                                "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                        int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                                        int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                                        int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                                        int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                                        endPoint.x = x + width/2 + grapLocation.x;
                                        endPoint.y = y + height/2 + grapLocation.y;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    pressMouseMove(startPoint, endPoint);

                } else if ("结束".equals(a[6])) {
                    GuiTestObject toTool = getToggleButtonOfTransition(a);
                    toTool.click();
                    GuiTestObject graph = getProcessGragh(a);
                    TestObject flowGraphManager = (TestObject)graph.invoke("getFlowGraphManager");
                    Point grapLocation = (Point) graph.getProperty("locationOnScreen");

                    if(a[5].split("/").length != 2) {
                        throw new FindTimeoutException("请指定精确的转移起点，格式【类型/名称】。");
                    }
                    String endType = a[5].split("/")[0];
                    String endName = a[5].split("/")[1];

                    //other activity location info.
                    Point startPoint = new Point();
                    TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
                    if(activitiesNotStartEnd != null) {
                        Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
                        if(actChildren != null) {
                            for(Object actChild : actChildren) {
                                Object tooltip = ((TestObject)actChild).invoke("getTooltip");
                                if(tooltip != null) {
                                    String strTooltip = tooltip.toString();
                                    if(strTooltip.indexOf(endName) == -1) {
                                        continue;
                                    }
                                    strTooltip = strTooltip.replaceAll("<html>", "").replaceAll(
                                            "</html>", "").replaceAll("<strong>", "").replaceAll(
                                                    "</strong>", "").replaceAll("<br>", "");
                                    String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                            strTooltip.indexOf("描述:")).trim();
                                    String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                            strTooltip.indexOf("合并方式: ")).trim();
                                    if(name.equals(endName) && type.equals(endType)) {
                                        Object[] recArgs = {actChild};
                                        TestObject rec2D = (TestObject) graph.invoke("getCellBounds",
                                                "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                        int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                                        int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                                        int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                                        int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                                        startPoint.x = x + width/2 + grapLocation.x;
                                        startPoint.y = y + height/2 + grapLocation.y;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    //end activity location info.
                    Point endPoint = new Point();
                    TestObject activityEnd = (TestObject) flowGraphManager.invoke("getEnds");
                    if(activityEnd != null) {
                        Object[] endChildren = (Object[]) activityEnd.invoke("toArray");
                        if(endChildren != null) {
                            for(Object endChild : endChildren) {
                                if(endChild != null) {
                                    Object[] recArgs = {endChild};
                                    TestObject rec2D = (TestObject) graph.invoke(
                                            "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                    int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                                    int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                                    int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                                    int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                                    endPoint.x = x + width/2 + grapLocation.x;
                                    endPoint.y = y + height/2 + grapLocation.y;
                                }
                                break;
                            }
                        }
                    }
                    pressMouseMove(startPoint, endPoint);
                } else {
                    GuiTestObject toTool = getToggleButtonOfTransition(a);
                    toTool.click();
                    GuiTestObject graph = getProcessGragh(a);
                    TestObject flowGraphManager = (TestObject)graph.invoke("getFlowGraphManager");
                    Point grapLocation = (Point) graph.getProperty("locationOnScreen");

                    if(a[5].split("/").length != 2 && a[6].split("/").length != 2) {
                        throw new FindTimeoutException("请指定精确的转移起点、终点，格式【类型/名称】。");
                    }
                    String startType = a[5].split("/")[0];
                    String startName = a[5].split("/")[1];
                    String endType = a[6].split("/")[0];
                    String endName = a[6].split("/")[1];

                    //other activity location info.
                    Point startPoint = new Point();
                    boolean calcStart = false;
                    //end activity location info.
                    Point endPoint = new Point();
                    boolean calcEnd = false;
                    TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
                    if(activitiesNotStartEnd != null) {
                        Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
                        if(actChildren != null) {
                            for(Object actChild : actChildren) {
                                Object tooltip = ((TestObject)actChild).invoke("getTooltip");
                                if(tooltip != null) {
                                    String strTooltip = tooltip.toString();
                                    if(strTooltip.indexOf(startName) == -1 && strTooltip.indexOf(endName) == -1) {
                                        continue;
                                    }

                                    strTooltip = strTooltip.replaceAll("<html>", "").replaceAll(
                                            "</html>", "").replaceAll("<strong>", "").replaceAll(
                                                    "</strong>", "").replaceAll("<br>", "");
                                    String name = strTooltip.substring(strTooltip.indexOf("名称: ") + 3,
                                            strTooltip.indexOf("描述:")).trim();
                                    String type = strTooltip.substring(strTooltip.indexOf("活动类型: ") + 5,
                                            strTooltip.indexOf("合并方式: ")).trim();

                                    if(name.equals(startName) && type.equals(startType)) {
                                        Object[] recArgs = {actChild};
                                        TestObject rec2D = (TestObject) graph.invoke("getCellBounds",
                                                "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                        int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                                        int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                                        int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                                        int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                                        startPoint.x = x + width/2 + grapLocation.x;
                                        startPoint.y = y + height/2 + grapLocation.y;
                                        calcStart = true;
                                    } else if(name.equals(endName) && type.equals(endType)) {
                                        Object[] recArgs = {actChild};
                                        TestObject rec2D = (TestObject) graph.invoke("getCellBounds",
                                                "(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D", recArgs);
                                        int x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                                        int y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                                        int height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                                        int width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                                        endPoint.x = x + width/2 + grapLocation.x;
                                        endPoint.y = y + height/2 + grapLocation.y;
                                        calcEnd = true;
                                    }
                                }
                                if(calcStart && calcEnd) {
                                    break;
                                }
                            }
                        }
                    }
                    pressMouseMove(startPoint, endPoint);
                }
            } else {
                GuiTestObject toTool = getToggleButtonOfActivity(a);
                Point dragPoint = calcDragToPointApprove(a);
                toTool.click();
                clickScreenPoint(dragPoint);
            }
        } else {
            GuiSubitemTestObject toTree = getTree(a);
            Point dragToPoint = new Point();
            if (a[6].indexOf("查询条件") != -1) {
                if (a[4].indexOf("上下文") != -1) {
                    dragToPoint = new Point(700,128);
                } else {
                    dragToPoint = new Point(60,380);
                }
            } else {
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[6]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[6] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[6], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[6], compNm.get(0));
                        compNmCacheAdd.put(a[6], compNm.get(0));
                        a[6] = compNm.get(0);
                    }
                }
                GuiTestObject toLabel = getLabel(a);
                Point pLabel =(Point) toLabel.getProperty("locationOnScreen");
                dragToPoint = new Point(pLabel.x + 60, pLabel.y + 100);
            }
            toTree.click(atPath(a[3]), atPoint(12, 12));
            Point mPoint = Mouse.getCursorPos();
            TestObject to = RootTestObject.getRootTestObject().objectAtPoint(mPoint);
            ((GuiSubitemTestObject) to).dragToScreenPoint(mPoint,dragToPoint);
        }
        unregisterAll();
    }

    public void 删除(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].indexOf("查询条件") != -1) {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[6]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[6] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[6], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[6], compNm.get(0));
                    compNmCacheAdd.put(a[6], compNm.get(0));
                    a[6] = compNm.get(0);
                }
            }
            GuiTestObject toLabel = getLabel(a);
            GuiTestObject toCellsPane = getCellsPane();
            Point toCellsPanePoint = (Point)toCellsPane.getProperty("locationOnScreen");
            Point toLabelPoint = (Point)toLabel.getProperty("locationOnScreen");
            Rectangle rec = (Rectangle) toCellsPane.getProperty("visibleRect");
            Point dragToPoint = new Point();
            if (toLabelPoint.y < toCellsPanePoint.y) {
                dragToPoint = new Point(toCellsPanePoint.x + 90, toCellsPanePoint.y + rec.height/3);
            } else {
                dragToPoint = new Point(toCellsPanePoint.x + rec.width/3, toLabelPoint.y);
            }
            toLabel.dragToScreenPoint(dragToPoint);
        } else {
            GuiSubitemTestObject toList = getList(a);
            System.out.println(toList.getProperty(".itemText").toString());
            String itemText = toList.getProperty(".itemText").toString();
            String[] items = itemText.substring(1, itemText.length()-1).split(",");
            int selectedIndex = -1;
            for (int i = 0; i < items.length; i++) {
                if (a[6].equals(items[i])) {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex == -1) {
                throw new FindTimeoutException(String.format(
                        "提示信息：搜索维度【%s】超时、【%s】未找到、请查维度名字是否正确！！",a[6]));
            }
            Object[] cs = { new Integer(selectedIndex), new Integer(selectedIndex) };
            Rectangle rec = (Rectangle)(toList.invoke("getCellBounds","(II)Ljava/awt/Rectangle",cs));

            Point toListPosition = (Point)toList.getProperty("locationOnScreen");
            Point cellPoint = new Point(toListPosition.x + rec.width/2, toListPosition.y + rec.height/2);

            GuiTestObject toSuspendDlg = getSplitPane();
            Point toSuspendDlgPoint = (Point)toSuspendDlg.getProperty("locationOnScreen");
            Point dragToPoint = new Point(toSuspendDlgPoint.x - 15, cellPoint.y);

            toList.dragToScreenPoint(atIndex(selectedIndex),dragToPoint);
        }

        unregisterAll();
    }

    public void 单击点(Point p, String envLang) throws FindTimeoutException {
        if (p == null) {
            throw new FindTimeoutException("请指定点！");
        }
        clickPoint(p);
        unregisterAll();
    }

    public void 参照(String[] a, String envLang) throws FindTimeoutException {
    //      int count = getTopNum();
            if ("".equals(a[3].trim()) || a[3] == null) {
                inputKeyF2();
                return;
            }
            if (a[3].indexOf("表体") != -1) {
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[5]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[5] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[5], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[5], compNm.get(0));
                        compNmCacheAdd.put(a[5], compNm.get(0));
                        a[5] = compNm.get(0);
                    }
                }

                GuiSubitemTestObject to = getTable(a);
                doubleClickTable(to, a, envLang);
                sleep(1);
                TextGuiTestObject t = getFieldText(to, null);
                t.click();
                Point p = t.getScreenPoint();

                clickScreenPoint(new Point(p.x
                        + Integer.parseInt(t.getProperty("width").toString()) / 2
                        + 5, p.y));
            } else {
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[3]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[3] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[3], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[3], compNm.get(0));
                        compNmCacheAdd.put(a[3], compNm.get(0));
                        a[3] = compNm.get(0);
                    }
                }
                TextGuiTestObject to = getObjectByPriorLabel(a);
                if(to.getProperty("editable").toString().equalsIgnoreCase("false")) {
                    try {
                        GuiTestObject rftBtn = (GuiTestObject) to.invoke("getRefBtn");
                        rftBtn.click();
                    } catch (Exception e) {
                         to.click();
                         Point p = to.getScreenPoint();
                         clickScreenPoint(new Point(p.x
                              + Integer.parseInt(to.getProperty("width").toString()) / 2
                              + 5, p.y));
                    }
                } else {
                    to.click();
                    Point p = to.getScreenPoint();
                    clickScreenPoint(new Point(p.x
                            + Integer.parseInt(to.getProperty("width").toString()) / 2
                            + 5, p.y));
                }
            }
    //      int st = 0;
    //      while (getTopNum() <= count) {
    //          sleep(0.5);
    //          if (st++ > 40)
    //              break;
    //      }
            unregisterAll();
        }

    public void 参照输入(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }
        if("上下文".equals(a[3])) {
            TextGuiTestObject to = getObjectByClass(a);
            if(a[6] == null || "".equals(a[6])) {
                GuiTestObject rftBtn = (GuiTestObject) to.invoke("getRefBtn");
                rftBtn.click();
            } else {
                //to.setText(a[6]);
                //inputKeyENTER();
                if(to.getText() != null && !"".equals(to.getText())) {
                    to.doubleClick();
                } else {
                    to.click();
                }
                inputString(a[6]);
            }
        } else {
            TextGuiTestObject to = getObjectByPriorLabel(a);
            to.setText(a[6]);
        }
        sleep(1);
        unregisterAll();
    }

    public void 快捷键(String[] a, String envLang) {
        sleep(1);
        IScreen is = getRootTestObject().getScreen();

        //复合键组合的快捷键，格式："ctrl + alt + i"
        if (a[3].split("[+]").length > 1){
            MultiSCKUtil.pressKeys(a[3]);
        } else {
            if (a[4].equals("") || a[4] == null){
                is.inputKeys(a[3]);
            }else {
                for (int i = 0; i < Integer.parseInt(a[4]); i++) {
                    is.inputKeys(a[3]);
                }
            }
        }

        // inputKeyENTER();
        unregisterAll();
    }

    public void 多选(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }
        if(!"".equals(a[3]) && a[3] != null) {
            GuiTestObject to = getCheckBox(a);
            if(!(a[6] == null || "".equals(a[6]))){
                if ("Y".equals(a[6])) {
                    to.click();
                } else if (!"N".equals(a[6])){
                    logInfo("当回放数据池时，多选的值列“N”代表不勾选，“Y”代表勾选。");
                }
            } else {
                to.click();
            }
        } else {
            GuiTestObject to = getCheckBoxByName(a[3], a[4]);
            if(!(a[6] == null || "".equals(a[6]))){
                if ("Y".equals(a[6])) {
                    to.click();
                } else if (!"N".equals(a[6])){
                    logInfo("当回放数据池时，多选的值列“N”代表不勾选，“Y”代表勾选。");
                }
            } else {
                to.click();
            }
        }
        unregisterAll();
    }

    public void 选择(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }

        GuiSubitemTestObject to = getPopupMenu();
        to.click(atPath(a[3]));
        unregisterAll();
    }

    public void 选中(String[] a, String envLang) throws FindTimeoutException {
        if(a[3].equals("单元格")){
            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            Object[] cs =  {rowIndex, colIndex};

            GuiTestObject to = getCellsPane();
            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");
            selectModel.invoke("setAnchorCell", "(II)V", cs);
        } else if (a[3].equals("区域")) {
            if(a[4].length() < 2) {
                logInfo("选中区域的开始位置格式不正确，正确格式类似于:A1");
                throw new FindTimeoutException("选中区域的开始位置格式不正确，正确格式类似于:A1");
            }

            if(a[5].length() < 2) {
                logInfo("选中区域的结束位置格式不正确，正确格式类似于:A1");
                throw new FindTimeoutException("选中区域的开始位置格式不正确，正确格式类似于:A1");
            }

            Integer colIndexStart = calcExcelColumnToIndex(String.valueOf(a[4].toCharArray()[0]));
            Integer rowIndexStart = Integer.valueOf(a[4].substring(1)) - 1;
            Integer colIndexEnd = calcExcelColumnToIndex(String.valueOf(a[5].toCharArray()[0]));
            Integer rowIndexEnd = Integer.valueOf(a[5].substring(1)) - 1;
            GuiTestObject to = getCellsPane();

            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");

            Object[] start =  {rowIndexStart, colIndexStart};
            Object[] indecies =  {rowIndexStart, colIndexStart, colIndexEnd - colIndexStart + 1,
                    rowIndexEnd - rowIndexStart + 1};
            selectModel.invoke("setAnchorCell", "(II)V", start);
            TestObject areaPosition = (TestObject)selectModel.invoke("getSelectedArea");
            Object[] areaP = {areaPosition.invoke("getInstance","(IIII)Lcom/ufsoft/table/AreaPosition;",indecies)};
            selectModel.invoke("setSelectedArea", "(Lcom/ufsoft/table/AreaPosition;)V", areaP);
        }
        unregisterAll();
    }

    public void 子选择(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }

        GuiSubitemTestObject to = getSubMenu(a);
        to.click();
        unregisterAll();
    }

    public void 菜单选择(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }

        GuiTestObject to = getMenuItem(a[3]);
        to.click();
        unregisterAll();
    }

    public void 树选择(String[] a, String envLang) throws FindTimeoutException {
        GuiSubitemTestObject t = getTree(a);
        t.click(atPath(a[3]), atPoint(20, 10));
        unregisterAll();
    }

    public void 树勾选(String[] a, String envLang) throws FindTimeoutException {
        GuiSubitemTestObject t = getTree(a);
        t.click(atPath(a[3]), atPoint(20, 10));
        t.click(atPath(a[3]), atPoint(6, 10));
        unregisterAll();
    }

    public void 树右键(String[] a, String envLang) throws FindTimeoutException {
        GuiSubitemTestObject t = getTree(a);
        if ("节点".equals(a[6])) {
            t.click(atPath(a[3]), atPoint(20, 10));
            t.click(new MouseModifiers(MouseModifiers.MOUSE_RIGHT), atPath(a[3]), atPoint(25, 10));
        } else if ("空白".equals(a[6])) {
            t.click(new MouseModifiers(MouseModifiers.MOUSE_RIGHT), atPoint(1,1));
        }
        unregisterAll();
    }

    public void 下拉框选择(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("表体")
        		|| (a[3].startsWith("表体") && a[3].split("/").length == 2 && isNumber(a[3].split("/")[1]))) {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }

            GuiSubitemTestObject to1 = getTable(a);
            click(to1, a, envLang);
            sleep(1);
            GuiTestObject to = getComboBox(to1, a);
            if (to == null) {
                doubleClickTable(to1, a, envLang);
                to = getComboBox(to1, a);
            }
            if (Integer.parseInt(to.getProperty("maximumRowCount").toString()) > 8) {
                Object[] cs = { Integer.parseInt(a[6]) - 1 };
                to.invoke("setSelectedIndex", "(I)V", cs);
            } else {
                sleep(0.5);
                Point p = to.getScreenPoint();
                clickScreenPoint(new Point(p.x,
                        p.y + (Integer.parseInt(a[6])) * (Integer.parseInt(to.getProperty("height").toString()))));
            }

        } else {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }
            GuiTestObject to = getComboBox(JavaDomain(), a);
            if (to == null) {
                throw new FindTimeoutException("下拉框未找到！！");
            }
            if (Integer.parseInt(to.getProperty("itemCount").toString()) > 5 || "系统".equals(a[3])) {
                to.doubleClick();
                Object[] cs = { Integer.parseInt(a[6]) - 1 };
                to.invoke("setSelectedIndex", "(I)V", cs);
            } else {
                to.click();
                sleep(0.5);
                Point p = to.getScreenPoint();
                clickScreenPoint(new Point(p.x,
                		p.y + (Integer.parseInt(a[6])) * (Integer.parseInt(to.getProperty("height").toString()))));
            }
        }
        unregisterAll();
    }

    public void 页签切换(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                if (a[0] == null || "".equals(a[0])) {
                    compNm = getTransNames(envLang, a[3], null);
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                }
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }

        GuiSubitemTestObject to = getTabbedPane(a[3]);
        if(a[3].split("/").length == 2 
        		&& a[3].split("/")[0].equals("审批流") 
        		&& a[3].split("/")[1].equals("单据函数")){
        	to.click(atIndex(0));
        } else if(a[3].split("/").length == 2 
        		&& a[3].split("/")[0].equals("审批流") 
        		&& a[3].split("/")[1].equals("元数据")){
        	to.click(atIndex(1));
        } else  {
        	to.click(atText(a[3]));
        }
        unregisterAll();
    }

    // *********************************************文本框输入*******************
    public void 输入(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("") || a[3] == null) {
            inputString(a[6]);
            return;
        }
        if (a[3].indexOf("表体") != -1) {
            if (a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                if(!(envLang == null || "".equals(envLang))) {
                    String col = a[5].split("/")[0];
                    String index = a[5].split("/")[1];
                    String compNmInCache = compNmCache.get(col);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[5] = compNmInCache + "/" + index;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, col, module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(col, compNm.get(0));
                        compNmCacheAdd.put(col, compNm.get(0));
                        a[5] = compNm.get(0) + "/" + index;
                    }
                }
            } else {
                if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[5]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[5] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[5], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[5], compNm.get(0));
                        compNmCacheAdd.put(a[5], compNm.get(0));
                        a[5] = compNm.get(0);
                    }
                }
            }
            GuiSubitemTestObject to = getTable(a);
            doubleClickTable(to, a, envLang);
            sleep(1);
            if (a[3].indexOf("表体多语框") != -1) {
                TextGuiTestObject t = getMultiLangCombox(to);
                Point p = t.getScreenPoint();
                clickScreenPoint(p);
                inputString(a[6]);
                inputKeyENTER();
            } else {
                TextGuiTestObject t = getFieldText(to, null);
                t.click();
                sleep(0.2);
                if (t.getProperty("text").toString() != null || !"".equals(t.getProperty("text").toString())) {
                     t.doubleClick();
                }
                if(a[6].trim().length() == a[6].trim().getBytes().length) {
                    inputChars(a[6]);
                } else {
                    inputString(a[6]);
                }
                if (a[6].getBytes().length > 15)
                    inputKeyENTER();
            }
        } else if (a[3].equals("单元格")) {
            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            Object[] cs =  {rowIndex, colIndex};

            GuiTestObject to = getCellsPane();
            TestObject selectModel = (TestObject) to.invoke("getSelectionModel");
            selectModel.invoke("setAnchorCell", "(II)V", cs);
            Object[] cellRectArgs = {selectModel.invoke("getAnchorCell"),Boolean.valueOf(false)};
            Rectangle rect = (Rectangle)to.invoke("getCellRect","(Lcom/ufsoft/table/IArea;Z)Ljava/awt/Rectangle",
                    cellRectArgs);

            Point cellsPanePoint = (Point) to.getProperty("locationOnScreen");

            Point clickPoint = new Point();
            clickPoint.x = cellsPanePoint.x + rect.x + rect.width/2;
            clickPoint.y = cellsPanePoint.y + rect.y + rect.height/2;

            doubleClickScreenPoint(clickPoint);
            inputChars(a[6]);
        } else {
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }
            TextGuiTestObject to = getObjectByPriorLabel(a);
            if (to.getProperty("class").equals("nc.ui.pub.beans.UIMultiLangCombox")) {
                Point p = to.getScreenPoint();
                clickScreenPoint(p);
                inputString(a[6]);
                clickScreenPoint(p);
            } else {
                int st = 0;
                while (to.getProperty("editable").toString().equals("false")) {
                    sleep(1);
                    to = getObjectByPriorLabel(a);
                    if (st > 10)
                        break;
                    st++;
                }
                if (a[3].equals("预警区间"))
                    to.click(new Point(Integer.parseInt(to.getProperty("width").toString()) - 20, 5));
                else
                    to.click();
                inputString(a[6]);
                to.click();
            }
        }
        unregisterAll();
    }

    //个别场景存在固定开始的prefix
    public void 直接输入(String[] a, String envLang) {
    	if (a[6].equals("") || a[6] == null) {
            return;
        }
    	IScreen is = getRootTestObject().getScreen();
        is.click(is.getMousePosition());
    	if(a[6].trim().length() == a[6].trim().getBytes().length) {
            inputCharsDirect(a[6]);
        } else {
            inputStringDirect(a[6]);
        }
        if (a[6].getBytes().length > 15)
            inputKeyENTER();
        unregisterAll();
    }

    public void 判断(String[] a, String envLang) throws FindTimeoutException {
        if (a[3].equals("按钮")) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[4]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[4] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[4], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[4], compNm.get(0));
                    compNmCacheAdd.put(a[4], compNm.get(0));
                    a[4] = compNm.get(0);
                }
            }

            Property[] pp = { new Property("uIClassID", "ButtonUI"),
                    new Property("text", a[4]), new Property("showing", "true") };
            to = JavaDomain().find(atDescendant(pp));
            try {

                while (to[0].getProperty("enabled").toString().equals("false")) {
                    sleep(1);
                    to = JavaDomain().find(atDescendant(pp));
                }
            } catch (Exception e) {
            }
            sleep(1);
        } else if (a[3].equals("文本框")) {
            // editable: true
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[3]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[3] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[3], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[3], compNm.get(0));
                    compNmCacheAdd.put(a[3], compNm.get(0));
                    a[3] = compNm.get(0);
                }
            }
            TextGuiTestObject to = getObjectByPriorLabel(a);
            while (to.getProperty("editable").toString().equals("false")) {
                sleep(1);
                to = getObjectByPriorLabel(a);
            }
            sleep(0.5);
        }
        unregisterAll();
    }

    public void 复制(String[] a, String envLang) throws FindTimeoutException {
        if(!(envLang == null || "".equals(envLang))) {
            String compNmInCache = compNmCache.get(a[3]);
            if (compNmInCache != null && !"".equals(compNmInCache)) {
                a[3] = compNmInCache;
            } else {
                String module = getModuleName(moduleProp, a[0]);
                compNm = getTransNames(envLang, a[3], module);
                if(compNm == null || compNm.size() == 0){
                    logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                    return;
                }
                compNmCache.put(a[3], compNm.get(0));
                compNmCacheAdd.put(a[3], compNm.get(0));
                a[3] = compNm.get(0);
            }
        }
        TextGuiTestObject to = getObjectByPriorLabel(a);
        String copyVal = to.getProperty("text").toString();
        setClipboard(copyVal);
        unregisterAll();
    }

    public void 检查点(String[] a, String envLang) throws Exception {

        if (a[6].equals("dp")) {
            if (rpNow == null)
                return;
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(a[5]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    a[5] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, a[5], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(a[5], compNm.get(0));
                    compNmCacheAdd.put(a[5], compNm.get(0));
                    a[5] = compNm.get(0);
                }
            }
            GuiSubitemTestObject to = getTable(a);
            int row = 1;
            while (true) {
                String antiValue = FileTool.getXLSData(rpNow, 1, row, 0);
                if (antiValue == null || "".equals(antiValue)){
                    logError("********检查点验证失败！缺少期待值！");
                    throw new Exception();
                }

                String value = getTableValue(to, String.valueOf(row), a[5]);
                if (value == null || "".equals(value)){
                    logError("********检查点验证失败！缺少实际值！");
                    throw new Exception();
                }

                if (value.equals(antiValue)) {
                    logInfo("++++++++检查点验证成功！实际值：" + value + " 期望值："
                            + antiValue);
                } else {
                    logError("********检查点验证失败！实际值：" + value + " 期望值："
                            + antiValue);
                    throw new Exception();
                }
                row++;
            }
        } else if (a[3].indexOf("数据库") != -1) {
            // userName, password, IP, port, database
            String username = a[9].trim();
            String password = a[10].trim();
            String ip = a[11].trim();
            String port = a[12].trim();
            String database = a[13].trim();
            String sql = a[14].trim();
            if ("".equals(username) || username == null) {
                logError("********检查点验证失败！缺少数据库用户名！");
                throw new Exception();
            }
            if ("".equals(password) || password == null) {
                logError("********检查点验证失败！缺少数据库密码！");
                throw new Exception();
            }
            if ("".equals(ip) || ip == null) {
                logError("********检查点验证失败！缺少数据库IP！");
                throw new Exception();
            }
            if ("".equals(port) || port == null) {
                logError("********检查点验证失败！缺少数据库地址端口！");
                throw new Exception();
            }
            if ("".equals(database) || database == null) {
                logError("********检查点验证失败！缺少数据库名！");
                throw new Exception();
            }
            if ("".equals(sql) || sql == null) {
                logError("********检查点验证失败！缺少SQL文！");
                throw new Exception();
            } else if(sql.charAt(sql.length() - 1) == ';' || sql.charAt(sql.length() - 1) == '；' ){
                sql = sql.substring(0, sql.length() - 1);
            }

            ResultSet rs = null;

            String expectValue = a[6];
            if (expectValue == null || "".equals(expectValue)){
                logError("********检查点验证失败！缺少期待值！");
                throw new Exception();
            }

            String actualValue = null;
            try {
                DBMng dbm = DBMng.getInstance(username, password, ip,
                        port, database);
                rs = dbm.executeQuery(sql);
                while (rs.next()) {
                    actualValue = String.valueOf(rs.getObject(1));
                    break;
                }
                dbm.close();
            } catch (SQLException e) {
                System.err.println("Exception:" + e.getMessage());
                logInfo("ExecuteLine sql" + e.getMessage());
            } catch (Exception e) {
                logInfo("Exception:" + e.getMessage());
                e.printStackTrace();
            }

            if(expectValue == null || "".equals(expectValue)){
                logError("********检查点验证失败！缺少实际值！");
                throw new Exception("********检查点验证失败！缺少实际值！");
            }

            if (expectValue.equals(actualValue)) {
                logInfo("++++++++检查点验证成功！实际值：" + actualValue + " 期望值："
                        + expectValue);
            } else {
                logError("********检查点验证失败！实际值：" + actualValue + " 期望值："
                        + expectValue);
                throw new Exception("********检查点验证失败！实际值：" + actualValue + " 期望值："
                        + expectValue);
            }
            return;
        } else if (a[3].indexOf("表体") != -1) {
        	if("".equals(a[4]) && "".equals(a[6])) {
        		if(!(envLang == null || "".equals(envLang))) {
                    String compNmInCache = compNmCache.get(a[5]);
                    if (compNmInCache != null && !"".equals(compNmInCache)) {
                        a[5] = compNmInCache;
                    } else {
                        String module = getModuleName(moduleProp, a[0]);
                        compNm = getTransNames(envLang, a[5], module);
                        if(compNm == null || compNm.size() == 0){
                            logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                            return;
                        }
                        compNmCache.put(a[5],compNm.get(0));
                        compNmCacheAdd.put(a[5],compNm.get(0));
                        a[5] = compNm.get(0);
                    }
                }
                GuiSubitemTestObject to = getTable(a);
                ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                if(orderTables.getRowCount() == 0) {
                	logInfo("********检查点验证成功！实际值：" + orderTables.getRowCount() + " 期望值：" + 0);
                    return;
                } else {
                	logError("********检查点验证失败！实际值：" + orderTables.getRowCount() + " 期望值：" + 0);
                    throw new Exception("********检查点验证失败！实际值：" + orderTables.getRowCount() + " 期望值：" + 0);
                }
        	} else {
        		String[] text = a[4].split("/");
        		if (text.length == 2) {
        			if(!(envLang == null || "".equals(envLang))) {
        				String firstText = text[0].toString();
        				String compNmInCache = compNmCache.get(firstText);
        				if (compNmInCache != null && !"".equals(compNmInCache)) {
        					firstText = compNmInCache;
        				} else {
        					String module = getModuleName(moduleProp, a[0]);
        					compNm = getTransNames(envLang, firstText, module);
        					if(compNm == null || compNm.size() == 0){
        						logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
        						return;
        					}
        					compNmCache.put(firstText, compNm.get(0));
        					compNmCacheAdd.put(firstText, compNm.get(0));
        					firstText = compNm.get(0);
        				}
        				a[4] = firstText + "/" + text[1];
        			}
        		}
        		if(!(envLang == null || "".equals(envLang))) {
        			String compNmInCache = compNmCache.get(a[5]);
        			if (compNmInCache != null && !"".equals(compNmInCache)) {
        				a[5] = compNmInCache;
        			} else {
        				String module = getModuleName(moduleProp, a[0]);
        				compNm = getTransNames(envLang, a[5], module);
        				if(compNm == null || compNm.size() == 0){
        					logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
        					return;
        				}
        				compNmCache.put(a[5],compNm.get(0));
        				compNmCacheAdd.put(a[5],compNm.get(0));
        				a[5] = compNm.get(0);
        			}
        		}
        		GuiSubitemTestObject to = getTable(a);
        		String value = getTableValue(to, a[4], a[5]);
        		
        		if (a[6] == null || "".equals(a[6])){
        			if (value == null || "".equals(value)) {
        				logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
        				return;
        			} else {
        				logError("********检查点验证失败！脚本缺少期望值");
        				throw new Exception("********检查点验证失败！脚本缺少期望值");
        			}
        		}
        		if (value == null || "".equals(value)){
        			logError("********检查点验证失败！缺少实际值！");
        			throw new Exception("********检查点验证失败！缺少实际值！");
        		}
        		
        		if (value.equals(a[6])) {
        			logInfo("++++++++检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
        		} else {
        			logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
        			throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
        		}
        	}
        } else if (a[3].indexOf("对话框") != -1){
            TextGuiTestObject messageArea = getTextArea();
            String message = messageArea.getProperty("text") == null? "" : messageArea.getProperty("text").toString();
            if (a[6].equals(message)) {
                logInfo("++++++++检查点验证成功！实际值：" + message + " 期望值：" + a[6]);
            } else {
                logError("********检查点验证失败！实际值：" + message + " 期望值：" + a[6]);
                throw new Exception("********检查点验证失败！实际值：" + message + " 期望值：" + a[6]);
            }
        } else if (a[3].indexOf("下拉框") != -1) {
            if (a[3].indexOf('/') == -1) {
                logError("********检查点验证失败！脚本格式不正确，斜杠为半角。**********");
                throw new Exception("********检查点验证失败！脚本格式不正确，斜杠为半角。**********");
            }

            String comboBoxNm = a[3].split("/")[1];

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(comboBoxNm);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    comboBoxNm = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, comboBoxNm, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(comboBoxNm,compNm.get(0));
                    compNmCacheAdd.put(comboBoxNm,compNm.get(0));
                    comboBoxNm = compNm.get(0);
                }
            }
            a[3] = a[3].split("/")[0] + comboBoxNm;
            GuiTestObject to = getComboBox(JavaDomain(), a);
            if (to == null) {
                throw new Exception("下拉框未找到！！");
            }
            String selectedValue = to.invoke("getSelectedItemName").toString();

            if (a[6].equals(selectedValue)) {
                logInfo("++++++++检查点验证成功！实际值：" + selectedValue + " 期望值：" + a[6]);
            } else {
                logError("********检查点验证失败！实际值：" + selectedValue + " 期望值：" + a[6]);
                throw new Exception("********检查点验证失败！实际值：" + selectedValue + " 期望值：" + a[6]);
            }
        } else if (a[3].indexOf("多选") != -1) {
            String checkBoxNm = a[4];

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(checkBoxNm);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    checkBoxNm = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, checkBoxNm, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(checkBoxNm,compNm.get(0));
                    compNmCacheAdd.put(checkBoxNm,compNm.get(0));
                    checkBoxNm = compNm.get(0);
                    a[4] = checkBoxNm;
                }
            }

            GuiTestObject to = getCheckBoxByName(checkBoxNm, a[5]);

            String selectedValue = to.getProperty("selected").toString().toUpperCase();

            if (a[6].toUpperCase().equals(selectedValue)) {
                logInfo("++++++++检查点验证成功！实际值：" + selectedValue + " 期望值：" + a[6].toUpperCase());
            } else {
                logError("********检查点验证失败！实际值：" + selectedValue + " 期望值：" + a[6].toUpperCase());
                throw new Exception("********检查点验证失败！实际值：" + selectedValue + " 期望值：" + a[6].toUpperCase());
            }
        } else if (a[3].indexOf("透视图") != -1){
            GuiTestObject to = getPerspectiveChart(a);
            String[] chartDetails = to.getProperty("imageMap").toString().split("\\n");
            String value = "";
            for(String detail : chartDetails) {
                if(detail.indexOf(a[4]) != -1 && detail.indexOf(a[5]) != -1) {
                    value = detail.split("value=")[1].replace("\"", "").replace(">", "");
                    break;
                }
            }
            if (a[6].equals(value)) {
                logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
            } else {
                logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
            }
        } else if (a[3].indexOf("单元格") != -1){

            Integer colIndex = calcExcelColumnToIndex(a[5]);
            Integer rowIndex = Integer.valueOf(a[4]) - 1;
            GuiTestObject to = getReportTable();
            Object[] cs =  {rowIndex, colIndex};
            TestObject testObj  = (TestObject)to.invoke("getCell","(II)Lcom/ufsoft/table/Cell", cs);
            Object o = testObj.invoke("getValue");
            String value = null;
            if (o != null ) {
                if (o instanceof Double) {
                    DecimalFormat df = new DecimalFormat();
                    df.setMinimumFractionDigits(0);
                    df.setMaximumFractionDigits(8);
                    value = df.format(o).replaceAll(",", "");
                } else {
                    value = o.toString();
                    if (value.indexOf("objectId") != -1) {
                        TestObject strObj = (TestObject)testObj.invoke("getValue");
                        value = strObj.invoke("toString").toString();
                    }
                }
            }
            if (a[6] == null || "".equals(a[6])) {
                if (value == null || "".equals(value)) {
                    logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
                    return;
                } else {
                    logError("********检查点验证失败！脚本缺少期望值");
                    throw new Exception("********检查点验证失败！脚本缺少期望值");
                }
            }

            String regex = "\\d+\\.?\\%";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(value);
            if (matcher.find() && value.indexOf("%") == value.length() - 1) {
                if (a[6].equals(value)) {
                    logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
                } else {
                    logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                    throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                }
            } else {

                boolean allDecimalZero = true;
                if (value.split("[.]").length > 1) {
                    String  decimals = value.split("[.]")[1];
                    for (int i = 0; i < decimals.length(); i++) {
                        if (Integer.valueOf(String.valueOf(decimals.charAt(i))) != 0) {
                            allDecimalZero = false;
                            break;
                        }
                    }
                }

                if (allDecimalZero) {
                    value = value.split("[.]")[0];
                    if (a[6].split("[.]").length > 1){

                        boolean isAllZero = true;
                        String  decimals = a[6].split("[.]")[1];
                        for (int i = 0; i < decimals.length(); i++) {
                            if (Integer.valueOf(String.valueOf(decimals.charAt(i))) != 0) {
                                isAllZero = false;
                                break;
                            }
                        }

                        if (!isAllZero) {
                            logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                            throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                        } else {
                            if (a[6].split("[.]")[0].equals(value)) {
                                logInfo("********检查点验证成功！实际值：" + a[6] + " 期望值：" + a[6]);
                            } else {
                                logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                                throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                            }
                        }
                    } else {
                        if (a[6].equals(value)) {
                            logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
                        } else {
                            logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                            throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                        }
                    }
                } else {
                    if(a[6].length() < value.length()) {
                        logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                        throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                    } else {
                        String AfterValueLength = a[6].substring(value.length(), a[6].length());

                        boolean hasMoreValidNumber = false;
                        for (int i = 0; i < AfterValueLength.length(); i++) {
                            if (Integer.valueOf(String.valueOf(AfterValueLength.charAt(i))) > 0) {
                                hasMoreValidNumber = true;
                            }
                        }
                        if (hasMoreValidNumber) {
                            logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                            throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                        } else {
                            if (a[6].indexOf(value,0) != -1) {
                                logInfo("********检查点验证成功！实际值：" + a[6] + " 期望值：" + a[6]);
                            } else {
                                logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                                throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                            }
                        }
                    }
                }
            }
        } else {
            TextGuiTestObject to = getObjectByPriorLabel(a);
            String value = to.getProperty("text").toString();

            if (a[6] == null || "".equals(a[6])){
                if (value == null || "".equals(value)) {
                    logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
                    return;
                } else {
                    logError("********检查点验证失败！脚本缺少期望值");
                    throw new Exception("********检查点验证失败！脚本缺少期望值");
                }
            }

            if (value == null || "".equals(value)){
                logError("********检查点验证失败！缺少实际值！");
                throw new Exception("********检查点验证失败！缺少实际值！");
            }

            if (value.equals(a[6])) {
                logInfo("********检查点验证成功！实际值：" + value + " 期望值：" + a[6]);
            } else {
                logError("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
                throw new Exception("********检查点验证失败！实际值：" + value + " 期望值：" + a[6]);
            }
        }
        unregisterAll();
    }

    public void 执行脚本(String[] a, String envLang) {
        SqlFileExec sfe = new SqlFileExec(a[3]);
        sfe.insert();
    }

    public void 流量开始(String[] a, String envLang) {
        try {
            if (url == "")
                url = getnmcUrl();
            startTime = GetFlux.getTime(url);
        } catch (Exception e) {
            logInfo("++++++++未启动NMC！");
        }
    }

    public void 流量结束(String[] a, String envLang) {
        sleep(2);
        if (startTime == null)
            logInfo("++++++++流量结果:  未定义开始点！");
        if (startTime != null) {
            String[] flux = GetFlux.transServerLog(startTime, url);
            if (flux != null) {
                String ss = String.format("++++++++流量结果: 连接数：%s 下行：%s    上行：%s",
                        flux[0], flux[1], flux[2]);
                System.out.println(ss);
                if (a[3] != null && a[3].equals("")) {
                    if (Integer.parseInt(a[3]) < Integer.parseInt(flux[0])) {
                        System.out.println("连接数超出预期值！预期值为" + a[3] + "，实际值为"
                                + flux[0]);
                        logInfo("连接数超出预期值！预期值为" + a[3] + "，实际值为" + flux[0]);
                    }
                }
                logInfo(ss);
            }
        }
    }

    // *******************************EXCEL导入************************
    public void 按住键(String[] a, String envLang) throws Exception {
        if ("".equals(a[3]) || a[3] == null) {
            logError("********按住键动作失败！缺少按键值！");
            throw new Exception();
        }
        MultiSCKUtil.longPressCtrl(a[3]);
    }

    public void 释放键(String[] a, String envLang) throws Exception {
        if ("".equals(a[3]) || a[3] == null) {
            logError("********释放键动作失败！缺少释放键值！");
            throw new Exception();
        }
        MultiSCKUtil.releaseCtrl(a[3]);
    }

    protected int calcExcelColumnToIndex(String column) {
        String col = column.toUpperCase();
        int sum = 0;
        for (int i = 0; i < col.length(); i++) {
            int currentIndex = col.length() - 1 -i;
            if (currentIndex > 0) {
                sum = (int) Math.pow(26, currentIndex) * (col.charAt(i) - 'A' + 1) + sum;
            } else {
                sum = (col.charAt(i) - 'A') + sum;
            }
        }
        return sum;
    }

    protected Point calcDragToPointApprove(String[] a) throws FindTimeoutException {
        GuiTestObject graph = getProcessGragh(a);
        List<Rectangle> childLocation = new ArrayList<Rectangle>(0);

        TestObject flowGraphManager = (TestObject)graph.invoke("getFlowGraphManager");

        //start activity location info.
        TestObject activityStart = (TestObject) flowGraphManager.invoke("getStart");
        if(activityStart != null) {
            Rectangle recStart =  new Rectangle();
            Object[] recArgs = {activityStart};
            TestObject rec2D = (TestObject) graph.invoke(
                    "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
            recStart.x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
            recStart.y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
            recStart.height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
            recStart.width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
            childLocation.add(recStart);
        }

        //end activity location info.
        TestObject activityEnd = (TestObject) flowGraphManager.invoke("getEnds");
        if(activityEnd != null) {
            Object[] endChildren = (Object[]) activityEnd.invoke("toArray");
            if(endChildren != null) {
                for(Object endChild : endChildren) {
                    if(endChild != null) {
                        Rectangle recEnd =  new Rectangle();
                        Object[] recArgs = {endChild};
                        TestObject rec2D = (TestObject) graph.invoke(
                                "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
                        recEnd.x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                        recEnd.y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                        recEnd.height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                        recEnd.width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                        childLocation.add(recEnd);
                    }
                }
            }
        }

        //other activity location info.
        TestObject activitiesNotStartEnd = (TestObject) flowGraphManager.invoke("getActivitiesNotStartEnd");
        if(activitiesNotStartEnd != null) {
            Object[] actChildren = (Object[]) activitiesNotStartEnd.invoke("toArray");
            if(actChildren != null) {
                for(Object actChild : actChildren) {
                    Rectangle recChild = new Rectangle();
                    Object[] recArgs = {actChild};
                    TestObject rec2D = (TestObject) graph.invoke(
                            "getCellBounds","(Ljava/lang/Object;)Ljava/awt/geom/Rectangle2D",recArgs);
                    recChild.x = Double.valueOf(rec2D.invoke("getX").toString()).intValue();
                    recChild.y = Double.valueOf(rec2D.invoke("getY").toString()).intValue();
                    recChild.height = Double.valueOf(rec2D.invoke("getHeight").toString()).intValue();
                    recChild.width = Double.valueOf(rec2D.invoke("getWidth").toString()).intValue();
                    childLocation.add(recChild);
                }
            }
        }

        Point grapLocation = (Point) graph.getProperty("locationOnScreen");
        Rectangle graphRec = (Rectangle) graph.getProperty("bounds");
        Point dragPoint = new Point();
        boolean isOk = false;
        while(!isOk) {
            Random random = new Random();
            dragPoint.x = random.nextInt(graphRec.width - 20) + 20;
            dragPoint.y = random.nextInt(graphRec.height - 20);

            int count = 0;
            for(Rectangle rec : childLocation) {
                if((dragPoint.x != rec.x && (Math.abs(dragPoint.x - rec.x) > rec.width + 10))
                        ||(dragPoint.y != rec.y && (Math.abs(dragPoint.y - rec.y) > rec.height + 10))) {
                    count++;
                }
            }

            if(count == childLocation.size()) {
                isOk = true;
            }
        }

        dragPoint.x = dragPoint.x + graphRec.x + grapLocation.x;
        dragPoint.y = dragPoint.y + graphRec.y + grapLocation.y;

        return dragPoint;
    }

    public void changTab(String a) throws FindTimeoutException {
        GuiSubitemTestObject to = getTabbedPane(a);
        if (!to.getProperty("accessibleContext.accessibleName").equals(a))
            to.click(atText(a));
    }

    public void click(GuiSubitemTestObject to, String[] a, String envLang) {
        String text[] = a[4].split("/");
        if (text.length == 2) {
            String firstText = text[0].toString();
            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(firstText);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    firstText = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, firstText, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(firstText, compNm.get(0));
                    compNmCacheAdd.put(firstText, compNm.get(0));
                    firstText = compNm.get(0);
                }
                if(isNumber(a[5])) {
                	int colIndex = Integer.parseInt(a[5]) - 1;
                	to.click(atCell(atRow(firstText, text[1].toString()),
                			atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else {                	
                	to.click(atCell(atRow(firstText, text[1].toString()),
                			atColumn(a[5])));
                }
            } else {
            	if(isNumber(a[5])) {
                	int colIndex = Integer.parseInt(a[5]) - 1;
                	to.click(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(atIndex(colIndex))));
            	} else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else {            		
            		to.click(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(a[5])));
            	}
            }
        } else {
            if (a[4].equals("last")) {
            	if(isNumber(a[5])) {
            		int colIndex = Integer.parseInt(a[5]) - 1;
            		to.click(atCell(atRow(atIndex(Integer.parseInt(to.getProperty(
                    		"rowCount").toString()) - 1)), atColumn(atIndex(colIndex))));
            	} else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(to.getProperty(
    						"rowCount").toString()) - 1)), atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(to.getProperty(
    						"rowCount").toString()) - 1)), atColumn(atIndex(index))));
                } else {            		
            		to.click(atCell(atRow(atIndex(Integer.parseInt(to.getProperty(
            				"rowCount").toString()) - 1)), atColumn(a[5])));
            	}
            } else {
            	if(isNumber(a[5])){
                    int colIndex = Integer.parseInt(a[5]) - 1;
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                    		atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                    		atColumn(atIndex(index))));
                } else {
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                    		atColumn(a[5])));
                }
            }
        }
    }

    private void clickIcon(String[] a) throws FindTimeoutException {
        if(a[4].indexOf("参照") != -1) {
            GuiTestObject to = getRefIconInSuspendDlg(a);
            to.click();
        } else if (a[4].indexOf("下拉") != -1) {
            GuiTestObject to = getDropdownIcon();
            to.click();
        } else if (a[4].indexOf("查找") != -1) {
            GuiTestObject to = getSearchIcon();
            to.click();
        } else if (a[4].indexOf("粘贴") != -1
                || a[4].indexOf("背景色") != -1
                ||a[4].indexOf("边框") != -1
                ||a[4].indexOf("前景色") != -1) {
            GuiTestObject to = getComboBoxIcon(a);
            to.click();
        }
    }

    public void clickList(String s) throws FindTimeoutException {
        GuiSubitemTestObject to = getList();
        to.click(atText(s));
    }

    public static void clickNode(GuiTestObject gto, String nodeName) {
        gto.click(atPoint(10, 10));
        IScreen is = getRootTestObject().getScreen();
        is.inputChars("1");
        is.inputKeys("{BACKSPACE}");
        setClipboard(nodeName);
        is.inputKeys("^v");
        is.inputKeys("{ENTER}");
        is.inputKeys("{ENTER}");
    }

    public void closeNode(String envLang) throws FindTimeoutException {
        GuiSubitemTestObject to = null;

        if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
            to = getTabbedPane("消息中心");
        } else if("2".equals(envLang)){
            to = getTabbedPane("Message Center");
        }

        if (Integer.parseInt(to.getProperty("tabCount").toString()) > 2) {
            Object[] index = { 2 };
            java.awt.Rectangle r = (java.awt.Rectangle) to.invoke(
                    "getBoundsAt", "(I)Ljava/awt/Rectangle", index);
            to.click(atPoint((r.x + (r.width - 18)), 18));
        }

    }

    public void clickTable(GuiSubitemTestObject to, String[] a, String envLang)
        throws NumberFormatException, FindTimeoutException {

        String text[] = a[4].split("/");
        int after = getTableRow(to, a, envLang);
        if (text.length == 2) {
            if(!(envLang == null || "".equals(envLang))) {
                String firstText = text[0].toString();
                String compNmInCache = compNmCache.get(firstText);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    firstText = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, firstText, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(firstText, compNm.get(0));
                    compNmCacheAdd.put(firstText, compNm.get(0));
                    firstText = compNm.get(0);
                }
                if(isNumber(a[5])) {
                	int colIndex = Integer.parseInt(a[5]) - 1;
                	to.click(atCell(atRow(firstText, text[1].toString()),
                			atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else {                	
                	to.click(atCell(atRow(firstText, text[1].toString()),
                			atColumn(a[5])));
                }
            } else {
            	if(isNumber(a[5])) {
            		int colIndex = Integer.parseInt(a[5]) - 1;
            		to.click(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(atIndex(colIndex))));
            	} else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else {            		
            		to.click(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(a[5])));
            	}
            }

        } else {
            if (a[4].equals("last")) {
            	if (isNumber(a[5])) {
                    int colIndex = Integer.parseInt(a[5]) - 1;
                    to.click(atCell(atRow(atIndex(Integer.parseInt(
                    		to.getProperty("rowCount").toString()) - 1)),atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(
                    		to.getProperty("rowCount").toString()) - 1)), atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(
                    		to.getProperty("rowCount").toString()) - 1)), atColumn(atIndex(index))));
                } else {
                    to.click(atCell(atRow(atIndex(Integer.parseInt(
                    		to.getProperty("rowCount").toString()) - 1)), atColumn(a[5])));
                }
            } else {
                //指定列索引
                if(isNumber(a[5])){
                    int colIndex = Integer.parseInt(a[5]) - 1;
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(index))));
                } else {
                    to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(a[5])));
                }
            }
        }

        int st = 0;
        while (after != Integer.parseInt(getTable(a).getProperty("selectedRow")
                .toString()) && (after + 1) <= Integer.parseInt(getTable(a).getProperty("rowCount")
                        .toString())) {
            sleep(1);
            if (st++ == 3)
                break;
        }
    }

    public void clickTableContent(GuiSubitemTestObject to, String[] a) {

        //表体多选
        if(a[3].toLowerCase().indexOf("多选") != -1){
            to.click(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                    atColumn(atIndex(Integer.parseInt(a[5])-1))));

        } else if (isNumber(a[6])) {

            Point toP = to.getScreenPoint();
            int columnCount = Integer.parseInt(to.getProperty("columnCount")
                    .toString());
            int width = Integer.parseInt(to.getProperty("width").toString());
            int height = Integer.parseInt(to.getProperty("height").toString());
            int rowHeight = Integer.parseInt(to.getProperty("rowHeight")
                    .toString());

            int addX = Math.round(width / columnCount) * Integer.parseInt(a[6])
                    + 5;
            int addY = rowHeight * (Integer.parseInt(a[4]) - 1) + 12;

            int clickPX = toP.x - Math.round(width / 2) + addX;
            int clickPY = toP.y - Math.round(height / 2) + addY;

            Point clickP = new Point(clickPX, clickPY);
            clickPoint(clickP);
        }
    }

    public void clickScreenPoint(Point p) {
        IScreen is = getRootTestObject().getScreen();
        is.click(p);
    }

    public void clickPoint(Point p){
        try {
            Robot r = new Robot();
            r.mouseMove(p.x, p.y);
            sleep(1.5);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void pressMouseMove(Point startPoint, Point endPoint){
        try {
            Robot r = new Robot();
            r.mouseMove(startPoint.x, startPoint.y);
            r.mousePress(InputEvent.BUTTON1_MASK);
            sleep(1.5);
            r.mouseMove(endPoint.x, endPoint.y);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void closeWindow() {
        TopLevelTestObject top = getTop(JavaDomain());
        try {
            GuiTestObject to = getCloseIconOfPopupWindow(null);
            if(to != null) {
                Point p = (Point) top.getProperty("locationOnScreen");
                int wid = Integer.parseInt(top.getProperty("width").toString());
                clickScreenPoint(new Point(p.x + wid - 10, p.y + 10));
                sleep(1);
            }
        } catch (FindTimeoutException e) {
            IScreen is = getRootTestObject().getScreen();
            is.inputKeys("{ESC}");
        }
    }

    public void doubleClickTable(GuiSubitemTestObject to, String[] a, String envLang) {
        String text[] = a[4].split("/");
        if (text.length == 2) {
            if(!(envLang == null || "".equals(envLang))) {
                String firstText = text[0].toString();
                String compNmInCache = compNmCache.get(firstText);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    firstText = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, firstText, module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return;
                    }
                    compNmCache.put(firstText, compNm.get(0));
                    compNmCacheAdd.put(firstText, compNm.get(0));
                    firstText = compNm.get(0);
                }
                if(isNumber(a[5])) {
                	int colIndex = Integer.parseInt(a[5]) - 1;
                	to.doubleClick(atCell(atRow(firstText, text[1].toString()),
                			atColumn(atIndex(colIndex))));
                }  else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(firstText, text[1].toString()),
                            atColumn(atIndex(index))));
                } else {                	
                	to.doubleClick(atCell(atRow(firstText, text[1].toString()),
                			atColumn(a[5])));
                }
            } else {
            	if(isNumber(a[5])) {
            		int colIndex = Integer.parseInt(a[5]) - 1;
            		to.doubleClick(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(atIndex(colIndex))));
            	} else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(text[0].toString(), text[1].toString()),
                            atColumn(atIndex(index))));
                } else {            		
            		to.doubleClick(atCell(atRow(text[0].toString(), text[1].toString()),
            				atColumn(a[5])));
            	}
            }
        } else {
            if (a[4].equals("last")) {
                if (isNumber(a[5])) {
                    int colIndex = Integer.parseInt(a[5]) - 1;
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(to
                            .getProperty("rowCount").toString()) - 1)),
                            atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(to
                            .getProperty("rowCount").toString()) - 1)),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(to
                            .getProperty("rowCount").toString()) - 1)),
                            atColumn(atIndex(index))));
                } else {
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(to
                            .getProperty("rowCount").toString()) - 1)),
                            atColumn(a[5])));
                }
            } else {
                if (isNumber(a[5])) {
                    int colIndex = Integer.parseInt(a[5]) - 1;
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(colIndex))));
                } else if(a[5].split("/").length == 2 && isNumber(a[5].split("/")[1])) {
                    String name = a[5].split("/")[0];
                    String idx = a[5].split("/")[1];
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(index))));
                } else if(a[5].split("/").length > 2 && isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                    String name = a[5].substring(0, a[5].lastIndexOf("/"));
                    String idx = a[5].substring(a[5].lastIndexOf("/") + 1);
                    ITestDataTable orderTables = (ITestDataTable)to.getTestData("contents");
                    int count = 0;
                    int index = -1;
                    for (int col = 0; col < orderTables.getColumnCount(); col++) {
                        if(orderTables.getColumnHeader(col).toString().equals(name)) {
                            count++;
                            if(count == Integer.valueOf(idx)) {
                                index = col;
                                break;
                            }
                        }
                    }
                    to.doubleClick(atCell(atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(atIndex(index))));
                } else {
                    to.doubleClick(atCell(
                            atRow(atIndex(Integer.parseInt(a[4]) - 1)),
                            atColumn(a[5])));
                }
            }
        }
    }

    public void doubleClickScreenPoint(Point p) {
        IScreen is = getRootTestObject().getScreen();
        is.doubleClick(p);
    }

    public void dealError(String envLang) throws FindTimeoutException {
        while (getTopNum() >= 3) {
            closeWindow();
            sleep(0.5);
        }
        if (getTopNum() == 2) {
            closeWindow();
            sleep(0.5);
        }
        if (getTopNum() == 1)
            closeNode(envLang);
        sleep(1);
        while (getTopNum() > 1) {
            try {
                TestObject[] to1 = JavaDomain().find(
                        atList(atDescendant("uIClassID", "ButtonUI", "showing",
                                "true")));
                for (int i = 0; i < to1.length; i++) {
                    try {
                        if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                            if (to1[i].getProperty("text").toString().indexOf("确定") != -1
                                    || to1[i].getProperty("text").toString()
                                    .indexOf("否") != -1) {
                                ((GuiTestObject) to1[i]).click();
                                sleep(1);
                                break;
                            }
                        } else if ("2".equals(envLang)) {
                            if (to1[i].getProperty("text").toString().indexOf("OK") != -1
                                    || to1[i].getProperty("text").toString()
                                    .indexOf("No") != -1) {
                                ((GuiTestObject) to1[i]).click();
                                sleep(1);
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (i == to1.length - 1) {
                        closeWindow();
                    }
                }

                if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                    if (getTopNum() == 1
                            && Integer.parseInt(getTabbedPane("消息中心").getProperty(
                            "tabCount").toString()) > 2) {
                        closeNode(envLang);
                        sleep(1);
                    }
                } else if("2".equals(envLang)) {
                    if (getTopNum() == 1
                            && Integer.parseInt(getTabbedPane("Message Center").getProperty(
                            "tabCount").toString()) > 2) {
                        closeNode(envLang);
                        sleep(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sleep(0.5);
    }

    public void dealErrorOfNullPoint(String envLang) throws FindTimeoutException {
        while (getTopNum() >= 2) {
            IScreen is = getRootTestObject().getScreen();
            is.inputKeys("{ESC}");
            sleep(0.5);
        }
        if (getTopNum() == 1)
            closeNode(envLang);
        sleep(1);
        while (getTopNum() > 1) {
            try {
                TestObject[] to1 = JavaDomain().find(
                        atList(atDescendant("uIClassID", "ButtonUI", "showing",
                        "true")));
                for (int i = 0; i < to1.length; i++) {
                    try {
                        if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                            if (to1[i].getProperty("text").toString().indexOf("确定") != -1
                                    || to1[i].getProperty("text").toString()
                                    .indexOf("否") != -1) {
                                ((GuiTestObject) to1[i]).click();
                                sleep(1);
                                break;
                            }
                        } else if ("2".equals(envLang)) {
                            if (to1[i].getProperty("text").toString().indexOf("OK") != -1
                                    || to1[i].getProperty("text").toString()
                                    .indexOf("No") != -1) {
                                ((GuiTestObject) to1[i]).click();
                                sleep(1);
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (i == to1.length - 1) {
                        closeWindow();
                    }
                }

                if(envLang == null || "".equals(envLang) || "4".equals(envLang)) {
                    if (getTopNum() == 1
                            && Integer.parseInt(getTabbedPane("消息中心").getProperty(
                            "tabCount").toString()) > 2) {
                        closeNode(envLang);
                        sleep(1);
                    }
                } else if("2".equals(envLang)) {
                    if (getTopNum() == 1
                            && Integer.parseInt(getTabbedPane("Message Center").getProperty(
                            "tabCount").toString()) > 2) {
                        closeNode(envLang);
                        sleep(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sleep(0.5);
    }

    public boolean isCoordinate(String s){
        if (!s.contains("(") || !s.contains(")") || !s.contains(",")){
            return false;
        } else if (s.indexOf("(") != s.lastIndexOf("(")) {
            return false;
        } else if (s.indexOf(")") != s.lastIndexOf(")")) {
            return false;
        } else if (s.indexOf(",") != s.lastIndexOf(",")) {
            return false;
        }

        return true;
    }

    public void inputKeyF2() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("{F2}");
    }

    public void inputKeyF10() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("{F10}");
    }

    public void inputKeyCtrlI() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("^i");
    }

    public void inputKeyCtrlD() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("^d");
    }

    public void inputKeyCtrlS() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("^s");
    }

    public void inputKeyENTER() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("{ENTER}");
    }

    public void inputKeyBACK() {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("{BACKSPACE}");
    }

    public void inputKey(String key, int count) {
        IScreen is = getRootTestObject().getScreen();
        for (int i = 0; i < count; i++)
            is.inputKeys(key);
    }

    public void inputString(String value) {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("^a");
        setClipboard(value);
        is.inputKeys("^v");
    }

    public void inputChars(String value) {
        IScreen is = getRootTestObject().getScreen();
        is.inputKeys("^a");
        is.inputChars(value);
    }
    
    public void inputStringDirect(String value) {
    	IScreen is = getRootTestObject().getScreen();
    	setClipboard(value);
    	is.inputKeys("^v");
    }
    
    public void inputCharsDirect(String value) {
    	IScreen is = getRootTestObject().getScreen();
    	is.inputChars(value);
    }

    public boolean isNumber(String s) {
        char[] sArray = s.toCharArray();
        for (int i = 0; i < sArray.length; i++) {
            if (sArray[i] < '0' || sArray[i] > '9') {
                return false;
            }
        }
        return true;
    }

    public Point getCoordinate(String s){
        String strX = s.substring(s.indexOf("(") + 1, s.indexOf(","));
        String strY = s.substring(s.indexOf(",") + 1, s.indexOf(")"));

        int intX = Integer.parseInt(strX);
        int intY = Integer.parseInt(strY);

        Point p = new Point(intX, intY);
        return p;
    }

    /**
     * 获得NC的URL地址
     *
     * @return
     */
    public String getnmcUrl() {
        TestObject to = getRootTestObject().find(
                atDescendant(".class", "Html.HtmlDocument"))[0];
        String url = to.getProperty(".url").toString();
        System.out.println(url);
        String ip = url.split(":")[1];
        return String.format("%s%s%s", "http:", ip, ":9999/remotecall");
    }

    public String getTableValue(TestObject to, String row, String col) {
        ITestDataTable orderTables = (ITestDataTable) to
                .getTestData("contents");
        int cols = orderTables.getColumnCount();
        int rows = orderTables.getRowCount();
        int index = 0;
        int count = 0;
        for (int i = 0; i < cols; i++) {
            if (col.split("/").length == 2 && isNumber(col.split("/")[1])) {
                String name = col.split("/")[0];
                String idx = col.split("/")[1];
                if(orderTables.getColumnHeader(i).toString().equals(name)) {
                    count++;
                    if(count == Integer.valueOf(idx)) {
                        index = i;
                        break;
                    }
                }
            } else if (col.split("/").length > 2 && isNumber(col.substring(col.lastIndexOf("/") + 1))) {
                String name = col.substring(0, col.lastIndexOf("/"));
                String idx = col.substring(col.lastIndexOf("/") + 1);
                if(orderTables.getColumnHeader(i).toString().equals(name)) {
                    count++;
                    if(count == Integer.valueOf(idx)) {
                        index = i;
                        break;
                    }
                }
            } else {
                if (orderTables.getColumnHeader(i).toString().equals(col)) {
                    index = i;
                    break;
                }
            }
        }
        if (row.trim().split("/").length == 2) {
            return orderTables.getCell(orderTables.getRowIndex(
                    atRow(row.trim().split("/")[0], row.trim().split("/")[1])), index).toString();
        } else if (row.trim().equalsIgnoreCase("last")) {
            return orderTables.getCell(rows - 1, index).toString();
        } else {
            return orderTables.getCell(Integer.parseInt(row) - 1, index)
                    .toString();
        }
    }

    public int getTableRow(TestObject to, String[] a, String envLang) {
        ITestDataTable orderTables = (ITestDataTable) to
                .getTestData("contents");
        int cols = orderTables.getColumnCount();
        int rows = orderTables.getRowCount();
        String text[] = a[4].split("/");
        if (text.length == 2) {

            if(!(envLang == null || "".equals(envLang))) {
                String compNmInCache = compNmCache.get(text[0]);
                if (compNmInCache != null && !"".equals(compNmInCache)) {
                    text[0] = compNmInCache;
                } else {
                    String module = getModuleName(moduleProp, a[0]);
                    compNm = getTransNames(envLang, text[0], module);
                    if(compNm == null || compNm.size() == 0){
                        logError("控件名字的翻译结果为空！！当前行的脚本未执行......");
                        return 0;
                    }
                    compNmCache.put(text[0], compNm.get(0));
                    compNmCacheAdd.put(text[0], compNm.get(0));
                    text[0] = compNm.get(0);
                }
            }

            int index = 0;
            for (int i = 0; i < cols; i++) {
                if (orderTables.getColumnHeader(i).toString().equals(text[0])) {
                    index = i;
                    break;
                }
            }
            for (int i = 0; i < rows; i++) {
                if (orderTables.getCell(i, index).toString().equals(text[1]))
                    return i;
            }
        } else if (a[4].equals("last")) {
            return rows - 1;
        } else {
            return Integer.parseInt(a[4]) - 1;
        }
        return 0;
    }

    protected IWindow[] getTopWindowsWithCaptionPattern(String pattern, boolean ignoreCase)
    {
        Regex regex = ( ignoreCase ? new Regex(pattern, Regex.MATCH_CASEINDEPENDENT) :
                                     new Regex(pattern) );
        Vector<IWindow> matches = new Vector<IWindow>(10);
        IWindow[] topWindows = getTopWindows();
        int length = ( topWindows != null ? topWindows.length : 0 );

        for ( int i = 0; i < length; ++i ){
            if (regex.matches(topWindows[i].getText())){
                matches.add(topWindows[i]);
            }
        }

        IWindow[] result = new IWindow[matches.size()];

        for ( int i = 0; i < matches.size(); ++i ){
            result[i] = matches.elementAt(i);
        }
        return result;
    }

    public void rightClickScreenPoint(Point p) {
        IScreen is = getRootTestObject().getScreen();
        is.click(MouseModifiers.create(MouseModifiers.MOUSE_RIGHT),p);
    }

    public static void setClipboard(String s) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(s);
        clip.setContents(ss, ss);
    }
}
