import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import resources.GetNcUIHelper;
import base.FindTimeoutException;
import base.PropertiesRW;
import base.Translator;

import com.rational.test.ft.object.interfaces.BrowserTestObject;
import com.rational.test.ft.object.interfaces.DomainTestObject;
import com.rational.test.ft.object.interfaces.FrameTestObject;
import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.ProcessTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiTestObject;
import com.rational.test.ft.object.interfaces.TopLevelTestObject;
import com.rational.test.ft.script.Property;
import com.rational.test.ft.value.MethodInfo;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;
import com.rational.test.ft.vp.ITestDataTable;
import com.rational.test.ft.vp.ITestDataTree;
import com.rational.test.ft.vp.ITestDataTreeNode;
import com.rational.test.ft.vp.ITestDataTreeNodes;

/**
 * Script Name : <b>GetNcUI</b>
 * Description : Functional Test Script
 *
 * @since 2012/07/16
 * @author qiaorg
 */
public class GetNcUI extends GetNcUIHelper {
    Translator trans = Translator.getInstance();
    TestObject[] to = null;
    int sleepTime = 15;

    public void testMain(Object[] args) {

        try {
            DomainTestObject Domains[] = getDomains();
            for (int i = 0; i <= Domains.length - 1; i++) {
                System.out.println(Domains[i].getName().toString());
                if (Domains[i].getName().toString().equals("Process")) {
                    ProcessTestObject topObjects = Domains[i].getProcess();
                    System.out.println(topObjects.getProperties());

                    // topObjects.getProperty("")
                    // for (int j = 0; j <= topObjects.length - 1; j++) {
                    // System.out.println(topObjects[j].toString());
                    // if (topObjects[j].getProperty(".class").equals(
                    // "Html.HtmlBrowser")) {
                    // // return new BrowserTestObject(topObjects[0]);
                    // }
                    // }
                }
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    public GuiTestObject getFirst(String[] a) {
        to = JavaDomain().find(
                atList(atDescendant("uIClassID", "TextFieldUI", "showing",
                        "true")));
        return (GuiTestObject) to[0];
    }

    // 获得按钮
    public GuiTestObject getButton(String[] a) throws FindTimeoutException {

        Property[] pp = {new Property("uIClassID", "ButtonUI"),
                new Property("text", a[3]),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime){
                st = 0;
                pp[0] = new Property("uIClassID", "ButtonUI");
                pp[1] = new Property("toolTipText", a[3]);
                pp[2] = new Property("showing", "true");
                pp[3] = new Property("enabled", "true");

                to = JavaDomain().find(atDescendant(pp));
                if (to.length == 0) {
                    pp[0] = new Property("uIClassID", "ToggleButtonUI");
                    pp[1] = new Property("toolTipText", a[3]);
                    pp[2] = new Property("showing", "true");
                    pp[3] = new Property("enabled", "true");

                    while (to.length == 0) {
                        sleep(1);
                        to = JavaDomain().find(atDescendant(pp));
                        if (st++ == sleepTime) {
                            throw new FindTimeoutException(
                                    String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！", a[3]));
                        }
                    }
                }
            }
        }

        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[4].equals("") || a[4] == null))
            return (GuiTestObject) to[Integer.parseInt(a[4])];

        return (GuiTestObject) to[to.length - 1];
    }

    // 获得工具箱和属性编辑器按钮
    public GuiTestObject getToggleButton(String[] a) throws FindTimeoutException {

        Property[] pp = {new Property("uIClassID", "ToggleButtonUI"),
                new Property("accessibleContext.accessibleName", a[3]),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime) {
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！",a[3]));
            }
        }

        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[4].equals("") || a[4] == null))
            return (GuiTestObject) to[Integer.parseInt(a[4])];

        return (GuiTestObject) to[to.length - 1];
    }

    // 获得审批流转移按钮
    public GuiTestObject getToggleButtonOfTransition(String[] a) throws FindTimeoutException {

        Property[] pp = {new Property("uIClassID", "ToggleButtonUI"),
                new Property("accessibleContext.accessibleName", a[4]),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime) {
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！",
                                a[4].split("/").length == 2 ? a[4].split("/")[0] : a[4]));
            }
        }

        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (a[4].split("/").length == 2 && isNumber(a[4].split("/")[1])) {
            return (GuiTestObject) to[Integer.parseInt((a[4].split("/")[1])) - 1];
        }
        return (GuiTestObject) to[to.length - 1];
    }

    // 获得审批流活动按钮
    public GuiTestObject getToggleButtonOfActivity(String[] a) throws FindTimeoutException {

        Property[] pp = {new Property("uIClassID", "ToggleButtonUI"),
                new Property("accessibleContext.accessibleName", a[4]),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime) {
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！", a[4]));
            }
        }

        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[6].equals("") || a[6] == null))
            return (GuiTestObject) to[Integer.parseInt(a[6]) - 1];

        return (GuiTestObject) to[to.length - 1];
    }

    // 获得审批流面板控件
    public GuiTestObject getProcessGragh(String[] a) throws FindTimeoutException {
        Property[] pp = {new Property("uIClassID", "GraphUI"),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime) {
                throw new FindTimeoutException("提示信息：获取审批流图超时、检查审批流流程窗口是否打开！！");
            }
        }

        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[4].equals("") || a[4] == null))
            return (GuiTestObject) to[Integer.parseInt(a[4])];

        return (GuiTestObject) to[to.length - 1];
    }

    // 通过名字获得按钮
    public GuiTestObject getButtonByName(String name) throws FindTimeoutException {

        Property[] pp = {new Property("uIClassID", "ButtonUI"),
                new Property("text", name),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));

            if (st++ == sleepTime){
                st = 0;
                pp[0] = new Property("uIClassID", "ButtonUI");
                pp[1] = new Property("toolTipText", name);
                pp[2] = new Property("showing", "true");
                pp[3] = new Property("enabled", "true");

                to = JavaDomain().find(atDescendant(pp));
                if (to.length == 0) {
                    pp[0] = new Property("uIClassID", "ToggleButtonUI");
                    pp[1] = new Property("toolTipText", name);
                    pp[2] = new Property("showing", "true");
                    pp[3] = new Property("enabled", "true");

                    while (to.length == 0) {
                        sleep(1);
                        to = JavaDomain().find(atDescendant(pp));
                        if (st++ == sleepTime) {
                            return null;
                        }
                    }
                }
            }
        }

        if (to.length > 0) {
            return (GuiTestObject) to[0];
        } else {
            return null;
        }
    }

    // class: nc.ui.plaf.basic.BasicActionsBarUI$ActionButton
    // toolTipText: 新增业务数据(Ctrl+Shift+N)
    public GuiTestObject getActionButton() throws FindTimeoutException {
        Property[] pp = {
                new Property("class",
                        "nc.ui.plaf.basic.BasicActionsBarUI$ActionButton"),
                new Property("showing", "true") };
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取新增业务按钮超时、按钮未找到、请查证按钮名字是否正确！！");
        }
        return (GuiTestObject) to[0];

    }

    public GuiTestObject getActionButton(String name) throws FindTimeoutException {
        Property[] pp = {
                new Property("class",
                        "nc.ui.plaf.basic.BasicActionsBarUI$ActionButton"),
                new Property("showing", "true"), new Property("text", name) };
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                    String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！", name));
        }
        return (GuiTestObject) to[0];

    }

    public GuiTestObject getBussinessButton() throws FindTimeoutException {
        Property[] pp = {
                new Property("class","javax.swing.JButton"),
                new Property("accessibleContext.accessibleName", ""),
                new Property("iconDescription", "dropup.png"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                    "提示信息：获取【切换业务日期】按钮超时、按钮未找到、请查证按钮名字是否正确！！");
        }

        return (GuiTestObject) to[0];

    }

    public GuiTestObject getRefButton(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("class","nc.ui.pub.beans.UIRefPane$2"),
                new Property("accessibleContext.accessibleName", ""),
                new Property("iconDescription", "input_search.png"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取【参照】图标超时、指定图标未找到、请查证图标是否存在！！");
        }

        return (GuiTestObject) to[Integer.valueOf(a[6])-1];

    }

    public GuiTestObject getReportTable() {
        to = JavaDomain().find(
                atList(atDescendant("showing", "true", "class",
                        "com.ufida.report.free.view.FreeReportCellsPane")));
        if (to.length == 0) {
            to = JavaDomain().find(
                    atList(atDescendant("showing", "true", "class", "com.ufsoft.table.CellsPane")));
            return (GuiTestObject) to[0];
        }
        return (GuiTestObject) to[0];
    }

    public GuiTestObject getCloseIconInSuspendDlg() throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("accessibleContext.accessibleName", ""),
                new Property("iconDescription", "close.gif"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取窗口的关闭按钮超时、指定按钮未找到、请查证按钮是否存在！！");
        }
        return (GuiTestObject)to[0];
    }

    public GuiTestObject getCloseIconOfPopupWindow(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("accessibleContext.accessibleName", "Close"),
                new Property("iconDescription", "windows_off.png"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            pp[2] = new Property("iconDescription","windows_off_disable.png");
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取窗口的关闭按钮超时、指定窗口的关闭按钮未找到！！");
        }
        return (GuiTestObject)to[0];
    }

    public GuiTestObject getRefIconInSuspendDlg(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("accessibleContext.accessibleName", ""),
                new Property("iconDescription", "ref.gif"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取【参照】图标超时、指定图标未找到、请查证图标是否存在！！");
        }

        if(a[6] != null && !"".equals(a[6]) && to.length > 1) {
            return (GuiTestObject)to[Integer.valueOf(a[6]) - 1];
        }
        return (GuiTestObject)to[0];
    }

    public GuiTestObject getDropdownIcon() throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("iconDescription", "dropdown.gif"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            pp[1] = new Property("iconDescription","refadd.gif");
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取【下拉】图标超时、指定图标未找到、请查证图标是否存在！！");
        }

        return (GuiTestObject)to[0];
    }

    public GuiTestObject getSearchIcon() throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("iconDescription", "search.gif"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                "提示信息：获取【查找】图标超时、指定图标未找到、请查证图标是否存在！！");
        }

        return (GuiTestObject)to[0];
    }

    public GuiTestObject getComboBoxIcon(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ComboBoxUI"),
                new Property("name", a[4]),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(String.format(
                        "提示信息：获取【%s】图标超时、指定图标未找到、请查证图标是否存在！！", a[4]));
        }

        return (GuiTestObject)to[0];
    }

    public GuiTestObject getMxGraphComponent(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("class","com.mxgraph.swing.mxGraphComponent$mxGraphControl"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException("提示信息：获取流程图超时、指定流程图未找到、请查证流程图是否存在！！");
        }

        return (GuiTestObject)to[0];
    }

    public GuiTestObject getCellsPane() throws FindTimeoutException {
    	Property[] pp = {new Property("showing","true"),
    			new Property("class","com.ufsoft.table.CellsPane"),
    			new Property("enabled","true")};
        to = JavaDomain().find(atList(atDescendant(pp)));
        int st = 0;
        while(to.length == 0) {
        	pp[1] = new Property("class","com.ufida.report.free.view.FreeReportCellsPane");
        	sleep(1);
        	to = JavaDomain().find(atList(atDescendant(pp)));
        	if(st++ == sleepTime) {        		
        		throw new FindTimeoutException("提示信息：获取报表超时、指定报表未找到、请查证报表是否存在！！");
        	}
        }
        return (GuiTestObject) to[0];
    }

    public GuiTestObject getButtonByIndex(String[] a) {
        to = JavaDomain()
                .find(
                        atList(atDescendant("uIClassID", "ButtonUI", "showing",
                                "true")));
        ArrayList<TestObject> filter = new ArrayList<TestObject>();
        for (TestObject to1 : to) {
            try {
                if (to1.getProperty("text") != null
                        && !to1.getProperty("text").equals("")) {
                    filter.add(to1);
                }
            } catch (Exception e) {

            }
        }
        return (GuiTestObject) filter.get(Integer.parseInt(a[4]));
    }

    public GuiTestObject getButtonByIcon(String[] a) throws FindTimeoutException {
        Property[] pp = {
                new Property("uIClassID","ButtonUI"),
                new Property("iconDescription", a[5]),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            pp[1] = new Property("iconDescription", a[5]);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】图标超时、指定图标未找到、请查证图标是否存在！！", a[4]));
        }

        return (GuiTestObject)to[0];
    }
    // 获得label对象
    public GuiTestObject getLabel(String[] a) throws FindTimeoutException {
        Property[] pp = { new Property("uIClassID", "LabelUI"),
                new Property("text", a[6]), new Property("showing", "true") };
        to = JavaDomain().find(atDescendant(pp));

        logInfo(String.valueOf(to.length));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            // to = JavaDomain().find(
            // atDescendant("uIClassID", "LabelUI", "text", a[6]));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】按钮超时、按钮未找到、请查证按钮名字是否正确！！", a[6]));
        }
        return (GuiTestObject) to[0];

    }
    // 获得label对象
    public GuiTestObject getLabelByName(String name) throws FindTimeoutException {
        Property[] pp = { new Property("uIClassID", "LabelUI"),
                new Property("text", name), new Property("showing", "true") };
        to = JavaDomain().find(atDescendant(pp));

        if (to.length > 0) {
            return (GuiTestObject) to[0];
        } else {
            return null;
        }
    }

    public GuiTestObject getIconUp() throws FindTimeoutException {
        Property[] pp = {
                new Property("class","nc.ui.pub.beans.UIToolBar$1"),
                new Property("iconDescription", "up.gif"),
                new Property("showing","true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                    "提示信息：获取【向上】按钮超时、按钮未找到、请查证按钮名字是否正确！！");
        }
        return (GuiTestObject) to[0];
    }

    // 获得多选框
    public GuiTestObject getCheckBox(String[] a) throws FindTimeoutException {
        Property[] pp = { new Property("uIClassID", "CheckBoxUI"),
                new Property("text", a[3]), new Property("showing", "true") };
        to = JavaDomain().find(atDescendant(pp));
        // to = para.find(atDescendant("uIClassID", "CheckBoxUI", "text",
        // name));
        if (to.length == 0) { // 等待控件
            sleep(1);
            to = JavaDomain()
                    .find(
                            atDescendant("uIClassID", "CheckBoxUI",
                                    ".priorLabel", a[3]));
        }

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "CheckBoxUI", "text", a[3]));
            if (to.length == 0) { // 等待控件
                sleep(1);
                to = JavaDomain().find(
                        atDescendant("uIClassID", "CheckBoxUI", ".priorLabel",
                                a[3]));
            }
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】多选框超时、多选框未找到、请查证多选框名字是否正确！！", a[3]));
        }
        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[4].equals("") || a[4] == null))
            return (GuiTestObject) to[Integer.parseInt(a[4])];
        return new GuiTestObject(to[0]);
    }

    // 获得多选框
    public GuiTestObject getCheckBoxByName(String name, String index) throws FindTimeoutException {
        Property[] pp = { new Property("uIClassID", "CheckBoxUI"),
                new Property("text", name), new Property("showing", "true") };
        to = JavaDomain().find(atDescendant(pp));
        // to = para.find(atDescendant("uIClassID", "CheckBoxUI", "text",
        // name));
        if (to.length == 0) { // 等待控件
            sleep(1);
            to = JavaDomain()
                    .find(
                            atDescendant("uIClassID", "CheckBoxUI",
                                    ".priorLabel", name));
        }

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "CheckBoxUI", "text", name));
            if (to.length == 0) { // 等待控件
                sleep(1);
                to = JavaDomain().find(
                        atDescendant("uIClassID", "CheckBoxUI", ".priorLabel",
                                name));
            }
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】多选框超时、多选框未找到、请查证多选框名字是否正确！！", name));
        }
        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(index.equals("") || index == null))
            return (GuiTestObject) to[Integer.parseInt(index) - 1];
        return new GuiTestObject(to[0]);
    }
    // 获得单选框
    public GuiTestObject getRadioButton(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "RadioButtonUI", "text", a[3]));
        if (to.length == 0) { // 等待控件
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "RadioButtonUI", ".priorLabel",
                            a[3]));
        }
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "RadioButtonUI", "text", a[3]));
            if (to.length == 0) { // 等待控件
                sleep(1);
                to = JavaDomain().find(
                        atDescendant("uIClassID", "RadioButtonUI",
                                ".priorLabel", a[3]));
            }
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】单选框超时、单选框未找到、请查证单选框名字是否正确！！", a[3]));
        }
        if (to.length == 1)
            return (GuiTestObject) to[0];
        if (!(a[4].equals("") || a[4] == null))
            return (GuiTestObject) to[Integer.parseInt(a[4])];
        return new GuiTestObject(to[0]);

    }

    // 获得文本框
    public TextGuiTestObject getFieldText(TestObject para, String name) throws FindTimeoutException {

        to = para.find(atDescendant("uIClassID", "TextFieldUI", "showing",
                "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = para.find(atDescendant("uIClassID", "TextFieldUI", "showing",
                    "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】文本框超时、文本框未找到、请查证文本框名字是否正确！！", name));
        }
        return new TextGuiTestObject(to[0]);
    }

    public TextGuiTestObject getFieldTextByIndex(String[] a) throws FindTimeoutException {

        to = JavaDomain().find(
                atList(atDescendant("uIClassID", "TextFieldUI", ".classIndex",
                        a[6])));
        int st = 0;
        if (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atList(atDescendant("uIClassID", "TextFieldUI",
                            ".classIndex", a[6])));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】文本框超时、文本框未找到、请查证文本框名字是否正确！！", a[4]));
        }
        return new TextGuiTestObject(to[0]);
    }

    public TextGuiTestObject getFieldTextByName(String[] a) throws FindTimeoutException {

        to = JavaDomain().find(
                atList(atDescendant("uIClassID", "TextFieldUI", "showing",
                        "true")));
        if (to.length > 0) {
            ArrayList<TestObject> fiter = new ArrayList<TestObject>();
            for (TestObject t : to) {
                if (t.getProperty(".priorLabel").toString().equals(a[4])) {
                    fiter.add(t);
                    break;
                }
            }

            if (fiter.size() == 0) {
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】文本框超时、文本框未找到、请查证文本框名字是否正确！！", a[4]));
            }

            return new TextGuiTestObject(fiter.get(0));
        }

        int st = 0;
        if (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atList(atDescendant("uIClassID", "TextFieldUI",
                            ".priorLabel", a[4])));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】文本框超时、文本框未找到、请查证文本框名字是否正确！！", a[4]));
        }
        return new TextGuiTestObject(to[0]);
    }

    // 获得对话框中的文本区域
    public TextGuiTestObject getTextArea() throws FindTimeoutException {

        to = JavaDomain().find(atDescendant("uIClassID", "TextAreaUI", "showing",
                "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant("uIClassID", "TextAreaUI", "showing",
                    "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取对话框中文本区域超时、文本区域未找到、请查证文本区域是否存在！！"));
        }
        return new TextGuiTestObject(to[0]);
    }

    // 获得多语框
    public TextGuiTestObject getMultiLangCombox(TestObject para) throws FindTimeoutException {
        to = para.find(atDescendant("class",
                "nc.ui.pub.beans.UIMultiLangCombox", "showing", "true"));
        int st = 0;
        if (to.length == 0) {
            sleep(1);
            to = para.find(atDescendant("class",
                    "nc.ui.pub.beans.UIMultiLangCombox", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取多语框超时、多语框未找到、请查证多语框名字是否正确！！");
        }
        return new TextGuiTestObject(to[0]);
    }

    // 获得列表
    public GuiSubitemTestObject getList() throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "ListUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "ListUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取列表超时、列表未找到、请查证列表名字是否正确！！");
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得列表
    public GuiSubitemTestObject getList(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "ListUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "ListUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format(
                                "提示信息：获取【%s】列表超时、列表未找到、请查证列表索引名字是否正确！！", a[6]));
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);
        for (int i = 0; i < to.length; i++) {
            ITestDataList nameList = (ITestDataList) to[i].getTestData("list");
            ITestDataElementList nameListElements = nameList.getElements();
            int count = nameList.getElementCount();
            for (int col = 0; col < count; col++) {
                try {
                    if (nameListElements.getElement(col).getElement()
                            .toString().equals(a[6]))
                        return new GuiSubitemTestObject(to[i]);
                } catch (Exception e) {
                }
            }
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得列表
    public GuiSubitemTestObject getListByName(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "ListUI",
                        "accessibleContext.accessibleName", a[4]));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "ListUI",
                            "accessibleContext.accessibleName", a[4]));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format(
                                "提示信息：获取【%s】列表超时、列表未找到、请查证列表名字是否正确！！", a[4]));
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得透视图
    public GuiSubitemTestObject getPerspectiveChart(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "LabelUI",
                        "class", "nc.ui.pivot.plugin.chart.perspecitive.PerspectiveChartComponent$PerspectiveChartViewer"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "LableUI",
                            "class", "nc.ui.pivot.plugin.chart.perspecitive.PerspectiveChartComponent$PerspectiveChartViewer"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format(
                                "提示信息：获取透视图超时、透视图未找到、请查证透视图是否存在！！"));
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得PopupMenu
    public GuiSubitemTestObject getPopupMenu() throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("class", "javax.swing.JPopupMenu", "showing",
                        "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("class", "javax.swing.JPopupMenu", "showing",
                            "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取PopupMenu超时、PopupMenu未找到、请查证PopupMenu名字是否正确！！");
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得SubMenu
    public GuiSubitemTestObject getSubMenu(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(atDescendant("text", a[3], "showing", "true"));

        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant("text", a[3], "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取SubMenu超时、SubMenu未找到、请查证SubMenu名字是否正确！！");
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得MenuItem
    public GuiTestObject getMenuItem(String name) throws FindTimeoutException {
        Property[] pp = {new Property("uIClassID", "MenuItemUI"),
                new Property("showing", "true"),
                new Property("name", name)};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            pp[2] = new Property("text", name);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取MenuItemButton超时、MenuItemButton未找到、请查证MenuItemButton名字是否正确！！");
        }
        return new GuiTestObject(to[0]);
    }

    // 获得树对象
    public GuiSubitemTestObject getTree() throws FindTimeoutException {
        TestObject[] to = JavaDomain().find(
                atDescendant("uIClassID", "TreeUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "TreeUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取树对象超时、树对象未找到、请查证树对象名字是否正确！！");
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);
        if (to.length > 1
                && to[0]
                        .getProperty("class")
                        .equals(
                                "nc.ui.uap.menureg.view.MenuItemTreePanel$MenuItemTree")) {
            return new GuiSubitemTestObject(to[to.length - 1]);
        }

        return new GuiSubitemTestObject(to[0]);
    }

    // 获得树对象
    public GuiSubitemTestObject getTree(String[] a) throws FindTimeoutException {
        TestObject[] to = JavaDomain().find(
                atDescendant("uIClassID", "TreeUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "TreeUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                    String.format("提示信息：获取【%s】树对象超时、树对象未找到、请查证树对象名字是否正确！！", a[3]));
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);
        ArrayList<TestObject> filter = new ArrayList<TestObject>();
        for (int i = 0; i < to.length; i++) {
            ITestDataTree nameList = (ITestDataTree) to[i].getTestData("tree");
            ITestDataTreeNodes nameListElements = nameList.getTreeNodes();
            ITestDataTreeNode[] cdTreeNode = nameListElements.getRootNodes();
            try {
                int count = cdTreeNode.length;
                for (int col = 0; col < count; col++) {
                    try {
                        if (cdTreeNode[col].getNode().toString().equals(
                                a[3].split("->")[0]))
                            filter.add(to[i]);
                        // return new GuiSubitemTestObject(to[i]);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {

            }
        }
        if (filter.size() == 1)
            return new GuiSubitemTestObject(filter.get(0));
        if (a[4] != null && !a[4].toString().equals("")) {
            return new GuiSubitemTestObject(filter.get(Integer.parseInt(a[4]
                    .toString())));
        }
        return new GuiSubitemTestObject(filter.get(0));
        // return new GuiSubitemTestObject(to[0]);
    }

    // 获得业务导航界面TAB
    public GuiTestObject getExpandedTreePanel() throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("class", "nc.sfbase.beans.ExpandedTreePanel",
                        "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("class", "nc.sfbase.beans.ExpandedTreePanel",
                            "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取业务导航界面超时、业务导航界面未找到、请查证业务导航界面名字是否正确！！");
        }
        return new GuiTestObject(to[0]);

    }

    /**
     * 获得节点搜索框
     *
     * @return
     * @throws FindTimeoutException
     */
    public GuiTestObject getSercherField() throws FindTimeoutException {
        // nc.desktop.quickcode.QuickCodeInputPanel$3
        to = JavaDomain().find(
                atDescendant("class",
                        "nc.desktop.quickcode.QuickCodeInputPanel$3",
                        "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("class",
                            "nc.desktop.quickcode.QuickCodeInputPanel$3",
                            "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：搜索节点超时、节点未找到、请查证节点名字是否正确！！");
        }
        return new GuiTestObject(to[0]);
        // return null;
    }

    /**
     * 获得分割条
     *
     * @return
     * @throws FindTimeoutException
     */
    public GuiTestObject getSplitPane() throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("class", "nc.ui.pub.beans.UISplitPane",
                        "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("class", "nc.ui.pub.beans.UISplitPane","showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                "提示信息：搜索对话框超时、对话框未找到、请查对话框名字是否正确！！");
        }
        return new GuiTestObject(to[0]);
    }

    // 获得TABLE对象
    public GuiSubitemTestObject getTable() {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TableUI", "showing", "true"));
        if (to[0].getProperty("class").toString().equals(
                "nc.ui.pub.bill.BillScrollPane$BillTable")) {
            to = JavaDomain().find(
                    atDescendant("name", "Table", "showing", "true"));
            return new GuiSubitemTestObject(to[0]);
        }
        return new GuiSubitemTestObject(to[0]);
    }

    // 获得TABLE对象
    public GuiSubitemTestObject getTable(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TableUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "TableUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                    "提示信息：获取表体超时、表体未找到、请查证表体名字是否正确！！");
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);

        // TestObject[] filter = null;
        ArrayList<TestObject> filter = new ArrayList<TestObject>();
        for (int i = 0; i < to.length; i++) {
            ITestDataTable orderTables = (ITestDataTable) to[i]
                    .getTestData("contents");
            for (int col = 0; col < orderTables.getColumnCount(); col++) {
                try {
                    Object colname = orderTables.getColumnHeader(col);
                    String name = "";
                    if(a[5].split("/").length == 1) {
                        name = a[5];
                    } else if(a[5].split("/").length == 2) {
                        if(isNumber(a[5].split("/")[1])) {
                            name = a[5].split("/")[0];
                        } else {
                            name = a[5];
                        }
                    } else if (a[5].split("/").length > 2) {
                        if(isNumber(a[5].substring(a[5].lastIndexOf("/") + 1))) {
                            name = a[5].substring(0, a[5].lastIndexOf("/"));
                        } else {
                            name = a[5];
                        }
                    }
                    if (colname.toString().equals(name)) {
                        filter.add(to[i]);
                        break;
                    }
                    if (a[3].toString().indexOf("多选") != -1
                            && (colname.toString().equals("   ") || colname
                                    .toString().equals(""))) {
                        return new GuiSubitemTestObject(to[i]);
                    }
                    // return new GuiSubitemTestObject(to[i]);
                } catch (Exception e) {
                }
            }
        }
        if (filter.size() == 1)
            return new GuiSubitemTestObject(filter.get(0));
        if (a[3].toString().indexOf("右") != -1
                || a[3].toString().indexOf("下") != -1) {
            return new GuiSubitemTestObject(filter.get(1));
        }
        if (a[3].split("/").length == 2 && isNumber(a[3].split("/")[1])) {
            return (GuiSubitemTestObject) filter.get(Integer.valueOf(a[3].split("/")[1]) - 1);
        }
        return new GuiSubitemTestObject(filter.get(0));
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

    //获取特殊
    public GuiSubitemTestObject getTableTeshu(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TableUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "TableUI", "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取特殊表体超时、特殊表体未找到、请查证特殊标题名字是否正确！！");
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);
        for (int i = 0; i < to.length; i++) {
            ITestDataTable orderTables = (ITestDataTable) to[i]
                    .getTestData("contents");
            for (int col = 0; col < orderTables.getColumnCount(); col++) {
                try {
                    Object colname = orderTables.getColumnHeader(col);
                    if (colname.toString().equals(a[5])) {
                        // 非坐标的情况
                        if (!isCoordinate(a[6])){
                            a[6] = Integer.valueOf(col).toString();
                        }
                        return new GuiSubitemTestObject(to[i]);
                    }
                } catch (Exception e) {
                }
            }
        }
        if (to.length == 2
                && (a[3].toString().toLowerCase().indexOf("多选") != -1)) {
            return new GuiSubitemTestObject(to[1]);
        } else {
            return new GuiSubitemTestObject(to[0]);
        }
    }

    public GuiSubitemTestObject getTableHeader(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TableHeaderUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("uIClassID", "TableHeaderUI", "showing",
                            "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取表头超时、表头未找到、请查证表头名字是否正确！！");
        }
        if (to.length == 1)
            return new GuiSubitemTestObject(to[0]);
        ArrayList<TestObject> filter = new ArrayList<TestObject>();
        for (int i = 0; i < to.length; i++) {
            ITestDataList orderTables = (ITestDataList) to[i]
                    .getTestData("TableHeader");
            if("多选".equals(a[5]) && orderTables.getElementCount()==2) {
                filter.add(to[i]);
            } else {
                for (int col = 0; col < orderTables.getElementCount(); col++) {
                    try {
                        Object colname = orderTables.getElements().getElement(col)
                        .getElement().toString();

                        if (colname.toString().equals(a[5])) {
                            filter.add(to[i]);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (filter.size() == 1) {
            return new GuiSubitemTestObject(filter.get(0));
        } else if (a[6] != null && !"".equals(a[6]) && isNumber(a[6])) {
            return new GuiSubitemTestObject(filter.get(Integer.valueOf(a[6]) - 1));
        } else {
            return new GuiSubitemTestObject(filter.get(0));
        }
    }

    public boolean tableCompare(TestObject to) {

        return false;
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

    public GuiSubitemTestObject getScrollBar(String[] a) throws FindTimeoutException{

        Property[] pp = {
                new Property("class","nc.ui.pub.beans.UIScrollPane"),
                new Property("visible", "true") };
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取滚动条超时、滚动条未找到、请查证滚动条名字是否正确！！");
        }

        return (GuiSubitemTestObject) to[Integer.parseInt(a[4])];
    }

    public GuiSubitemTestObject getTableScrollBar(String[] a) throws FindTimeoutException{

        Property[] pp = {
                new Property("class","com.ufsoft.table.TableScrollBar"),
                new Property("visible", "true") };
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                "提示信息：获取滚动条超时、滚动条未找到、请查证滚动条名字是否正确！！");
        }

        return (GuiSubitemTestObject) to[Integer.parseInt(a[4])];
    }

    // 获得下拉框对象
    public GuiTestObject getComboBox(TestObject para, String[] a) throws FindTimeoutException {
    	String name = "";
    	if(a[3].split("/").length == 2) {
    		name = a[3].split("/")[1];
    	} else {
    		name = a[3];
    	}
        // uIClassID: ComboBoxUI
        if (name != null && !"".equals(name)) {
            Property[] pp = { new Property("uIClassID", "ComboBoxUI"),
                    new Property(".priorLabel", name),
                    new Property("showing", "true") };
            to = para.find(atDescendant(pp));
            int st = 0;
            while (to.length == 0) {
                sleep(1);
                to = para.find(atDescendant(pp));
                if (st++ == sleepTime)
                    throw new FindTimeoutException(
                            "提示信息：获取下拉框超时、下拉框未找到、请查证下拉框名字是否正确！！");
            }
        } else {
            to = para.find(atDescendant("uIClassID", "ComboBoxUI", "showing",
                    "true"));
        }
        if (to.length < 1) {
            to = para.find(atDescendant("uIClassID", "ComboBoxUI", "showing",
                    "true"));
        }
        if (to.length == 0) {
            return null;
        }
        if(a[4] != null && !"".equals(a[4]) && isNumber(a[4]) && to.length >= Integer.valueOf(a[4])) {
        	return new GuiTestObject(to[Integer.valueOf(a[4]) - 1]);
        }
        return new GuiTestObject(to[0]);
    }

    public void waitLogin() {

    }

    // 获得指定名称的页签
    public GuiSubitemTestObject getTabbedPane(String name) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TabbedPaneUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain()
                    .find(
                            atDescendant("uIClassID", "TabbedPaneUI",
                                    "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取指定名称的页签超时、指定名称的页签未找到、请查证指定名称的页签名字是否正确！！");
        }
        for (int i = 0; i < to.length; i++) {
            try {
                if ((to[i].getProperty(".tabs").toString().indexOf(name + ",") != -1 || to[i]
                        .getProperty(".tabs").toString().indexOf(name + "}") != -1)
                        && Integer.parseInt(to[i].getProperty("tabCount")
                                .toString()) > 1) {
                    return new GuiSubitemTestObject(to[i]);
                }
            } catch (Exception e) {
            }
        }
        return new GuiSubitemTestObject(to[to.length - 1]);
    }

    // 获得包含Toolbar的页签
    public GuiSubitemTestObject getTabbedPaneForToolBar(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TabbedPaneUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant("uIClassID", "TabbedPaneUI","showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取指定名称的页签超时、指定名称的页签未找到、请查证指定名称的页签名字是否正确！！");
        }

        List<TestObject> filter = new ArrayList<TestObject>();
        for (int i = 0; i < to.length; i++) {
            try {
                if(a[4] == null || "".equals(a[4])){
                    TestObject[] tos = to[i].getChildren();
                    for (TestObject t : tos){
                        if(t.getProperty("class").toString().equals(
                                "nc.ui.plaf.basic.UIExtTabbedPaneUI$TabAreaComponentPanel")) {
                            filter.add(to[i]);
                            break;
                        }
                    }
                } else {
                    if ((to[i].getProperty(".tabs").toString().indexOf(a[4] + ",") != -1
                            || to[i].getProperty(".tabs").toString().indexOf(a[4] + "}") != -1)
                            && Integer.parseInt(to[i].getProperty("tabCount").toString()) > 0) {
                        filter.add(to[i]);
                    }
                }
            } catch (Exception e) {
            }
        }

        if (filter.size() == 1) {
            return new GuiSubitemTestObject(filter.get(0));
        } else if (filter.size() > 1) {
            if (a[6] == null || "".equals(a[6]) || !isNumber(a[6])) {
                return new GuiSubitemTestObject(filter.get(0));
            } else {
                return new GuiSubitemTestObject(filter.get(Integer.valueOf(a[6]) - 1));
            }
        }

        return null;
    }

    // 获得指定名称的页签
    public Point getTabbedPaneButtonByTabbedPaneName(String name, int index) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TabbedPaneUI", "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain()
                    .find(
                            atDescendant("uIClassID", "TabbedPaneUI",
                                    "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取指定名称的页签超时、指定名称的页签未找到、请查证指定名称的页签名字是否正确！！");
        }
        for (int i = 0; i < to.length; i++) {
            try {
                if ((to[i].getProperty(".tabs").toString().indexOf(name + ",") != -1 || to[i]
                        .getProperty(".tabs").toString().indexOf(name + "}") != -1)
                        && Integer.parseInt(to[i].getProperty("tabCount")
                                .toString()) > 0) {
                    TestObject[] tos = to[i].getChildren();
                    for (TestObject to2 : tos) {
                        if (to2
                                .getProperty("class")
                                .toString()
                                .equals(
                                        "nc.ui.plaf.basic.UIExtTabbedPaneUI$TabAreaComponentPanel")) {

                            Dimension d = (Dimension) to2.getProperty("compSize");
                            Point p = (Point) to2.getProperty("locationOnScreen");
                            if(!((GuiTestObject)to2).hasFocus()) {
                                ((GuiTestObject)to2).hover();
                            }
                            return new Point(50 * index + p.x + 30, p.y + d.height / 2);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    // Tab右上角的图标
    public Point getTabbedPaneButton(int index) {
        to = JavaDomain().find(
                atDescendant("uIClassID", "TabbedPaneUI", "showing", "true"));

        for (TestObject to1 : to) {
            if (to1.getProperty("class").toString().equals(
                    "nc.desktop.ui.WorkbenchSpace$1")) {
                continue;
            }
            TestObject[] tos = to1.getChildren();
            for (TestObject to2 : tos) {
                if (to2
                        .getProperty("class")
                        .toString()
                        .equals(
                                "nc.ui.plaf.basic.UIExtTabbedPaneUI$TabAreaComponentPanel")) {

                    Dimension d = (Dimension) to2.getProperty("compSize");
                    Point p = (Point) to2.getProperty("locationOnScreen");
                    if(!((GuiTestObject)to2).hasFocus()) {
                        ((GuiTestObject)to2).hover();
                    }
                    return new Point(50 * index + p.x + 30, p.y + d.height / 2);
                }
            }
        }
        return null;
    }

    // 获得.priorLabel属性对象
    public TextGuiTestObject getObjectByPriorLabel(String[] a) throws FindTimeoutException {
        to = JavaDomain().find(
                atDescendant(".priorLabel", a[3], "showing", "true"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant(".priorLabel", a[3], "showing", "true"));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取.priorLable属性对象超时、指定对象未找到、请查证指定属性值是否正确！！", a[3]));
        }
        if (to.length == 1) {
            return new TextGuiTestObject(to[0]);
        } else if (a[4] != null && !a[4].equals("")) {
            return new TextGuiTestObject(to[Integer.parseInt(a[4].toString())]);
        } else {
            return new TextGuiTestObject(to[0]);
        }
    }

    // 获得.class属性对象
    public TextGuiTestObject getObjectByClass(String[] a) throws FindTimeoutException {
        Property[] pp = {new Property("class", "nc.ui.pivot.context.PivotContextTextRefComponent"),
                new Property(".classIndex", String.valueOf(Integer.valueOf(a[4]) - 1)),
                new Property("showing", "true"),
                new Property("enabled", "true")};
        to = JavaDomain().find(atDescendant(pp));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(atDescendant(pp));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        "提示信息：获取class属性对象超时、指定对象未找到、请查证指定属性值是否正确！！");
        }
        if (to.length == 1) {
            return new TextGuiTestObject(to[0]);
        } else if (a[4] != null && !a[4].equals("")) {
            return new TextGuiTestObject(to[Integer.parseInt(a[4].toString()) - 1]);
        } else {
            return new TextGuiTestObject(to[0]);
        }
    }

    public GuiTestObject getNcApplet() {
        to = getRootTestObject().find(
                atDescendant("class", "nc.sfbase.applet.NCApplet"));
        return new GuiTestObject(to[0]);
    }

    public FrameTestObject getFrame(String frame) throws FindTimeoutException {
        to = getRootTestObject().find(
                atDescendant("class", "javax.swing.JFrame", "title", frame));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = getRootTestObject().find(
                    atDescendant("class", "javax.swing.JFrame", "title", frame));
            if (st++ == sleepTime)
                throw new FindTimeoutException(
                        String.format("提示信息：获取【%s】窗口超时、指定对象未找到、请查证指定属性值是否正确！！", frame));
        }
        return new FrameTestObject(to[0]);
    }

    public TestObject JavaDomain() {
        // getRootTestObject()
        DomainTestObject domains[] = getDomains();
        for (int i = 0; i < domains.length; i++) {
            if (domains[i].getName().toString().equals("Java")) {
                TestObject[] topObjects = domains[i].getTopObjects();
                // logInfo("*********");
                // for (TestObject to : topObjects)
                // logInfo("1:"+to.getProperty("class").toString());
                if (topObjects.length == 1)
                    // return getNcApplet();
                    return topObjects[0];
                if (topObjects.length > 1)
                    if (topObjects[0].getProperty(".class").toString().equals(
                            "javax.swing.Popup$HeavyWeightWindow"))
                        // javax.swing.Popup$HeavyWeightWindow
                        return domains[i];
                    else
                        return topObjects[0];
            }
        }
        return getNcApplet();
    }

    // public TestObject JavaDomain() {
    // DomainTestObject domains[] = getDomains();
    // for (int i = 0; i < domains.length; i++) {
    // if (domains[i].getName().toString().equals("Java")) {
    // TestObject[] topObjects = domains[i].getTopObjects();
    // for(TestObject to : topObjects){
    // if(to.getProperty("class").toString().equals("sun.plugin2.main.client.PluginEmbeddedFrame"))
    // {
    // if (topObjects.length == 1)
    // return topObjects[0];
    // if (topObjects.length > 1)
    // if (topObjects[0].getProperty(".class").toString().equals(
    // "javax.swing.Popup$HeavyWeightWindow"))
    // return topObjects[1];
    // else
    // return topObjects[0];
    // }
    // }
    // }
    // }
    // return getNcApplet();
    // }

    public int getTopNum() {
        DomainTestObject domains[] = getDomains();
        for (int i = 0; i < domains.length; i++) {
            if (domains[i].getName().toString().equals("Java")) {
                TestObject topObjects[] = domains[i].getTopObjects();
                if (topObjects.length > 1)
                    if (topObjects[0].getProperty(".class").toString().equals(
                            "javax.swing.Popup$HeavyWeightWindow")
                            && topObjects.length == 2)
                        return 1;
                    else
                        return topObjects.length;

            }
        }
        return 1;
    }

    public BrowserTestObject getWebPart() {
        try {
            DomainTestObject Domains[] = getDomains();
            for (int i = 0; i <= Domains.length - 1; i++) {
                if (Domains[i].getName().toString().equals("Html")) {
                    TestObject topObjects[] = Domains[i].getTopObjects();
                    for (int j = 0; j <= topObjects.length - 1; j++) {
                        if (topObjects[j].getProperty(".class").equals(
                                "Html.HtmlBrowser")) {
                            return new BrowserTestObject(topObjects[0]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
        return null;
    }

    public TopLevelTestObject getTop(TestObject t) {
        TopLevelTestObject top = (TopLevelTestObject) JavaDomain()
                .getTopParent();
        return top;
    }

    public TestObject RootTestObject() {

        return getRootTestObject();

    }

    public void printProperties(TestObject to) {
        Hashtable<?, ?> htable = to.getProperties();
        int i = 1;
        Enumeration<?> eKey = htable.keys();
        Enumeration<?> eElems = htable.elements();
        while (eKey.hasMoreElements()) {
            System.out.println("Property" + i + ": name=" + eKey.nextElement()
                    + "         value:" + eElems.nextElement());
            i++;
        }
    }

    public void printMethods(TestObject to) {
        MethodInfo[] m = to.getMethods();
        for (int i = 1; i < m.length; i++) {
            System.out.println("Method" + i + ":name=" + m[i].getName()
                    + "   signatrue=" + m[i].getSignature());
        }
    }

    public void printAllInfo(TestObject to) {
        MethodInfo[] ms = to.getMethods();
        for (MethodInfo m : ms) {
          System.out.println(m.getDeclaringClass() + "  " + m.getName() + "  " + m.getSignature() + "  " + m.getClass());
        }
    }

    public GuiTestObject getDrawerBox() {
        to = JavaDomain().find(
                atDescendant("class", "nc.ui.pub.beans.DrawerBox"));
        int st = 0;
        while (to.length == 0) {
            sleep(1);
            to = JavaDomain().find(
                    atDescendant("class", "nc.ui.pub.beans.DrawerBox",
                            "showing", "true"));
            if (st++ == sleepTime)
                return null;
        }
        for (int i = 0; i < to.length; i++)
            printProperties(to[i]);

        return new GuiTestObject(to[0]);
    }

    public List<String> getTransNames(String envLang, String compNm, String module) {
        List<String> compNms = trans.transSwich(envLang, compNm, module);

        if(compNms == null) {
            logError("指定的测试语言值不正确！请输入指定语言的编码。注：（空:简体中文、1：英文、2：法语、3：繁体中文）");
            return new ArrayList<String>(0);
        } else if (compNms.size() == 0) {
            logError("指定的测试语言的多语资源未找到！");
            return new ArrayList<String>(0);
        }
        logInfo(String.format("++++++++  获得【%s】的多语资源为【%s】++++++++  ", compNm, compNms.get(0)));
        return compNms;
    }

    public String getModuleName(File moduleProp, String module){
        PropertiesRW pp = new PropertiesRW(moduleProp);
        String moduleName = pp.readValue(module);
        if (moduleName == null) {
            logError("指定的模块的翻译器未找到！");
        }
        return moduleName;
    }
}
