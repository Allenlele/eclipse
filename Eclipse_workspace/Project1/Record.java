import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import resources.RecordHelper;
import base.PropertiesRW;
import base.WriteExcel;

import com.rational.test.ft.object.interfaces.DomainTestObject;
import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.Row;
import com.rational.test.ft.sys.graphical.Keyboard;
import com.rational.test.ft.sys.graphical.Mouse;
import com.rational.test.ft.value.MethodInfo;
import com.rational.test.ft.vp.ITestDataTable;
import com.rational.test.ft.vp.ITestDataTree;
import com.rational.test.ft.vp.ITestDataTreeNode;
import com.rational.test.ft.vp.ITestDataTreeNodes;
/**
 * Script Name : <b>Record</b>
 * Description : Functional Test Script `
 *
 * @since 2012/11/13
 * @author Qiaorg
 */
public class Record extends RecordHelper {

    String[] line = null;
    String[] linetemp = null;
    File moduleProp = new File(System.getProperty("user.dir") + File.separator +
            "conf" + File.separator + "module_code_name.properties");

    public void testMain(Object[] args) {
        String path = null;
        if (args != null && args.length > 0) {
            path = (String) args[0];
        }
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        int n = 0;
        int row = 0;
        boolean isRecord = true;
        WriteExcel we = new WriteExcel(new File(path));
        while (isRecord) {
            n = Mouse.getMouseState();
            if (n == 1 && !Keyboard.isKeyDown(KeyEvent.VK_SHIFT) && !Keyboard.isKeyDown(KeyEvent.VK_CONTROL)) {
                try {
                    TestObject to = RootTestObject.getRootTestObject()
                            .objectAtPoint(Mouse.getCursorPos());
                    String[] value = AnalyseTestObject(to);
                    if(value != null) {
                        for (int i = 0; i < value.length; i++) {
                            try {
                                if (value[i] != null || !value[i].equals(""))
                                    we.setValue(value[i], i, row + 1);
                            } catch (Exception e) {

                            }
                        }
                        // expEXCEL(path, row, value);
                        row++;
                        sleep(0.4);
                    }
                    unregisterAll();
                } catch (Exception e) {
                }
            } else if (n == 1 && Keyboard.isKeyDown(KeyEvent.VK_SHIFT)) {
                try {
                    TestObject to = RootTestObject.getRootTestObject()
                            .objectAtPoint(Mouse.getCursorPos());
                    String[] value = AnalyseTestObject(to);
                    value[2] = "双击";
                    if(value != null) {
                        for (int i = 0; i < value.length; i++) {
                            try {
                                if (value[i] != null || !value[i].equals(""))
                                    we.setValue(value[i], i, row + 1);
                            } catch (Exception e) {

                            }
                        }
                        // expEXCEL(path, row, value);
                        row++;
                        sleep(0.4);
                    }
                    unregisterAll();
                } catch (Exception e) {
                }
            } else if (n == 1 && Keyboard.isKeyDown(KeyEvent.VK_CONTROL)) {
                try {
                    TestObject to = RootTestObject.getRootTestObject()
                            .objectAtPoint(Mouse.getCursorPos());
                    String[] value = AnalyseTestObjectOfVerify(to);
                    value[2] = "检查点";
                    if(value != null) {
                        for (int i = 0; i < value.length; i++) {
                            try {
                                if (value[i] != null || !value[i].equals(""))
                                    we.setValue(value[i], i, row + 1);
                            } catch (Exception e) {

                            }
                        }
                        // expEXCEL(path, row, value);
                        row++;
                        sleep(1);
                    }
                    unregisterAll();
                } catch (Exception e) {
                }
            } else if (Keyboard.isKeyDown(KeyEvent.VK_DECIMAL)) {
                String[] value = new String[7];
                value[2] = "快捷键";
                if(Keyboard.isKeyDown(13)) {
                    value[3] = "{Enter}";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_TAB)) {
                    value[3] = "{Tab}";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_C)) {
                    value[3] = "^c";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_V)) {
                    value[3] = "^v";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_N)) {
                    value[3] = "^n";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_R)) {
                    value[3] = "^r";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_E)) {
                    value[3] = "^e";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_P)) {
                    value[3] = "^p";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_Q)) {
                    value[3] = "^q";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_S)) {
                    value[3] = "^s";
                } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_A)) {
                    value[3] = "^a";
                }
                if(value[3] != null && !"".equals(value[3])) {
                    for (int i = 0; i < value.length; i++) {
                        try {
                            if (value[i] != null || !value[i].equals(""))
                                we.setValue(value[i], i, row + 1);
                        } catch (Exception e) {

                        }
                    }
                    row++;
                    sleep(1);
                }
                unregisterAll();
            } else if (n == 4) {
                isRecord = false;
                we.setValue("2", 7, 1);
                we.setValue(String.valueOf(row + 1), 7, 3);
                we.setValue("1", 8, 1);
                we.setValue("循环次数", 8, 0);
                we.setValue("开始行", 7, 0);
                we.setValue("结束行", 7, 2);
                we.setValue("模块", 0, 0);
                we.setValue("节点", 1, 0);
                we.setValue("动作", 2, 0);
                we.setValue("对象", 3, 0);
                we.setValue("字段行", 4, 0);
                we.setValue("字段列", 5, 0);
                we.setValue("值", 6, 0);
                unregisterAll();
            }
        }
        we.write();
        we.close();
    }

    public void printlnStrings(String[] s) {
        for (int i = 0; i < s.length; i++)
            System.out.print(s[i] + ",");
        System.out.println();
    }

    public boolean compara(String[] ne, String[] old) {
        boolean boo = true;
        try {
            if (old == null)
                return false;

            // a==b;
            // if(a[2].toString())
            for (int i = 0; i < 7; i++) {
                try {
                    if (!ne[i].toString().equals(old[i].toString())) {
                        boo = false;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            try {
                if (old[2].equals("下拉框选择")
                        && (ne[2].equals("单击") && ne[3].equals("列表")))
                    boo = true;
                if (ne[3].toString().indexOf("确定") != -1
                        || ne[3].indexOf("下一步") != -1)
                    boo = false;
            } catch (Exception e) {
            }
            return boo;
        } catch (Exception e) {
            return false;
        }

    }

    public void expEXCEL(String path, int row, String[] value) {
        WriteExcel we = new WriteExcel(new File(path));
        for (int i = 0; i < value.length; i++) {
            try {
                if (value[i] != null || !value[i].equals(""))
                    we.setValue(value[i], i, row + 1);
            } catch (Exception e) {

            }
        }
        we.write();
        we.close();

    }

    public void expEXCEL(String path, String[] value) {
        WriteExcel we = new WriteExcel(new File(path));
        try {
            we.setValue(value[0], 7, 1);
            we.setValue(value[1], 7, 3);
            we.setValue(value[2], 8, 1);
        } catch (Exception e) {

        }
        we.write();
        we.close();

    }

    public String[] AnalyseTestObject(TestObject to) {
        if (to == null)
            return null;
        if (!to.getDomain().toString().contains("objectId:Java"))
            return null;
        String classid = to.getProperty("uIClassID").toString();
        // uIClassID: CheckBoxMenuItemUI
        if (classid.equals("ButtonUI")) {
            return AnalyseButton(to);
        } else if (classid.equals("TextFieldUI")) {
            return AnalyseTextField(to);
        } else if (classid.equals("TabbedPaneUI")) {
            return AnalyseTabbedPane(to);
        } else if (classid.equals("TreeUI")) {
            return AnalyseTree(to);
        } else if (classid.equals("PopupMenuUI")) {
            return AnalysePopupMenu(to);
        } else if (classid.equals("CheckBoxMenuItemUI")) {
            return AnalyseMenuItem(to);
        } else if (classid.equals("MenuItemUI")) {
            return AnalyseMenuItem(to);
        } else if (classid.equals("ComboBoxUI")) {
            return AnalyseComboBox(to);
        } else if (classid.equals("ListUI")) {
            return AnalyseList(to);
        } else if (classid.equals("TableUI")) {
            return AnalyseTable(to);
        } else if (classid.equals("LabelUI")) {
            return AnalyseLabel(to);
        } else if (classid.equals("CheckBoxUI")) {
            return AnalyseCheckBox(to);
        } else if (classid.equals("RadioButtonUI")) {
            return AnalyseRadioButton(to);
        } else if (classid.equals("ComponentUI")) {
            return AnalyseComponent(to);
        } else if (classid.equals("PasswordFieldUI")) {
            return AnalyseTextField(to);
        } else {
            // System.out.println("无法识别的对象！");
        }
        return null;
    }
    public String[] AnalyseTestObjectOfVerify(TestObject to) {
        if (to == null)
            return null;
        if (!to.getDomain().toString().contains("objectId:Java"))
            return null;
        String classid = to.getProperty("uIClassID").toString();
        if (classid.equals("TextFieldUI")) {
            return AnalyseTextField(to);
        } else if (classid.equals("ComboBoxUI")) {
            return AnalyseComboBoxVerify(to);
        } else if (classid.equals("TableUI")) {
            return AnalyseTableVerify(to);
        }
        return null;
    }
    private String[] AnalyseLabel(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[3] = "标签";
        line[2] = "单击";
        line[6] = to.getProperty("text").toString();
        line[1] = getNodeName();
        return line;
    }

    private String[] AnalysePopupMenu(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[3] = getPopupMenu(to);
        line[2] = "选择";
        line[1] = getNodeName();
        return line;
    }

    public String getPopupMenu(TestObject to) {
        ITestDataTree cdTree;
        ITestDataTreeNodes cdTreeNodes;
        ITestDataTreeNode[] cdTreeNode;

        cdTree = (ITestDataTree) to.getTestData("extendedMenu");
        cdTreeNodes = cdTree.getTreeNodes();
        cdTreeNode = cdTreeNodes.getRootNodes();
        Point p = Mouse.getCursorPos();
        Point loc = (Point) to.getProperty("locationOnScreen");
        int y = p.y - loc.y;
        int height = Integer.valueOf(to.getProperty("height").toString())
                / cdTreeNode.length;
        int index = y / height;

        return cdTreeNode[index].getNode().toString();
    }

    public String[] AnalyseCheckBox(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "多选";
        line[3] = to.getProperty("text").toString();
        if (line[3].equals("") || line[3] == null)
            line[3] = to.getProperty(".priorLabel").toString();
        return line;

    }

    private String[] AnalyseRadioButton(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "单选";
        line[3] = to.getProperty("text").toString();
        if (line[3].equals("") || line[3] == null)
            line[3] = to.getProperty(".priorLabel").toString();
        return line;
    }

    public String[] AnalyseMenuItem(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "选择";
        line[3] = to.getProperty("text").toString();
        return line;
    }

    public String[] AnalyseComponent(TestObject to) {
        line = new String[7];
        if (to.getProperty("class").equals("nc.sfbase.beans.ExpandedTreePanel")) {

            line[2] = "打开节点";
            String tiptext = to.getProperty("toolTipText").toString();
            line[1] = tiptext.split("◆")[tiptext.split("◆").length - 1];
            nodeName = line[1];
            line[3] = line[1];
            TestObject selectionPath = (TestObject) to.invoke("getSelectionPath");
            Object[] path = (Object[]) selectionPath.invoke("getPath");
            line[0] = ((TestObject)path[0]).invoke("toString").toString();
            moduleName = line[0];
            return line;
        }
        return null;
    }

    public String[] AnalyseButton(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        if (to.getProperty("class").equals("nc.ui.pub.beans.UIRefPane$2")) {
            line[2] = "参照";
            if (to.getParent().getParent().getProperty("uIClassID").toString()
                    .equals("TableUI")) {
                line[3] = "表体";
                String[] rc = getTableRow(to.getParent().getParent());
                line[4] = rc[0];
                line[5] = rc[1];
            } else {
                try {
                    line[3] = to.getParent().getChildren()[0].getProperty(
                            ".priorLabel").toString();
                } catch (Exception e) {
                }
            }
        } else {
            line[2] = "单击";
            if (to.getProperty("class").toString()
            // class: nc.login.ui.LoginUISupport$5
                    .equals("nc.login.ui.LoginUISupport$5")) {
                line[3] = "登录";
            } else if (to.getProperty("text").toString().equals("")) {

                line[3] = "按钮";
                line[4] = to.getProperty(".classIndex").toString();
            } else {
                line[3] = to.getProperty("text").toString();
            }
        }
        return line;

    }

    public String[] AnalyseTextField(TestObject to) {

        line = new String[7];
        line[0] = getModuleName();
        line[2] = "输入";
        if (to.getParent().getParent().getProperty("uIClassID").toString()
                .equals("TableUI")) {
            line[3] = "表体";
            String[] rc = getTableRow(to.getParent().getParent());
            line[4] = rc[0];
            line[5] = rc[1];
        } else {
            try {
                line[3] = to.getProperty(".priorLabel").toString();
            } catch (Exception e) {
            }
        }
        line[6] = to.getProperty("text").toString();
        line[1] = getNodeName();
        if ((line[6].equals("") || line[6] == null))
            return null;
        return line;

    }

    public String[] AnalyseList(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "单击";
        line[3] = "列表";
        String items = to.getProperty(".itemText").toString();
        int index = Integer
                .parseInt(to.getProperty("selectedIndex").toString());
        String[] item = items.split(",");
        line[6] = item[index];
        if (line[6].contains("{"))
            line[6] = line[6].replace("{", "");
        if (line[6].contains("}"))
            line[6] = line[6].split("}")[0];
        return line;
    }

    public String[] AnalyseTabbedPane(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        // class: nc.desktop.ui.WorkbenchSpace$1
        if (to.getProperty("class").equals("nc.desktop.ui.WorkbenchSpace$1")) {
            if (isCloseNode(Integer.parseInt(to.getProperty("tabCount").toString()))) {
                line[1] = getNodeName();
                line[2] = "关闭节点";
            } else {
                line[1] = getNodeName();
                line[2] = "页签切换";
                sleep(0.2);
                line[3] = to.getProperty("accessibleContext.accessibleName")
                        .toString();
            }
        } else {
            line[1] = getNodeName();
            line[2] = "页签切换";
            sleep(0.2);
            line[3] = to.getProperty("accessibleContext.accessibleName")
                    .toString();
        }
        return line;
    }

    private boolean isCloseNode(int preClickNodes) {
        boolean isCloseNode = false;
        sleep(1.5);
        TestObject[] toes = JavaDomain().find(
                atDescendant("uIClassID", "TabbedPaneUI", "showing", "true"));
        for (int i = 0; i < toes.length; i++) {
            if(toes[i].getProperty(".tabs").toString().indexOf("消息中心") != -1
                    || toes[i].getProperty(".tabs").toString().indexOf("Message Center") != -1) {
                if(preClickNodes > Integer.valueOf(
                        ((GuiSubitemTestObject)toes[i]).getProperty("tabCount").toString())) {
                    isCloseNode = true;
                    break;
                }
            }
        }
        return isCloseNode;
    }

    String nodeName = "";

    // 获得节点名称
    public String getNodeName() {
        if (!nodeName.equals("") && nodeName != null){
            return nodeName;
        }
        try {
            TestObject to = JavaDomain().find(
                    atDescendant("class", "nc.desktop.ui.WorkbenchSpace$1"))[0];
            nodeName = to.getProperty("accessibleContext.accessibleName").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeName;
    }

    String moduleName = "";

    // 获取模块名称
    public String getModuleName() {
        if (!moduleName.equals("") && moduleName != null){
            return moduleName;
        }
        try {
            TestObject currentTab = JavaDomain().find(
                    atDescendant("class", "nc.desktop.ui.WorkbenchSpace$1"))[0];
            TestObject selectedComponent = (TestObject) currentTab.invoke("getSelectedComponent");
            if (((TestObject)selectedComponent.invoke("getClass")).invoke("toString").toString().indexOf(
                    "nc.sfbase.beans.AsynComponentWrapperPanel") == -1) {
                TestObject funcRegisterVO = (TestObject) selectedComponent.invoke("getFuncRegisterVO");
                Object ownModule = funcRegisterVO.invoke("getOwn_module");
                moduleName = getModuleName(moduleProp, ownModule.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduleName;
    }

    public GuiTestObject getNcApplet() {
        TestObject[] to = getRootTestObject().find(
                atDescendant("class", "nc.sfbase.applet.NCApplet"));
        return (GuiTestObject) to[0];

    }

    public String[] AnalyseComboBox(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        if (to.getProperty("class").equals("nc.ui.pub.beans.UIMultiLangCombox")) {
            line[2] = "输入";
            TestObject para = to.getParent();
            if (para.getProperty("uIClassID").equals("TableUI")) {
                line[3] = "表体多语框";
                String[] rc = getTableRow(para);
                line[4] = rc[0];
                line[5] = rc[1];
            } else {
                try {
                    line[3] = to.getProperty(".priorLabel").toString();
                } catch (Exception e) {
                }
            }
            line[6] = to.getProperty("text").toString();
            return line;
        }
        line[2] = "下拉框选择";
        // selectedIndex: 1
        line[6] = to.getProperty("selectedIndex").toString();
        try {
            line[3] = to.getProperty(".priorLabel").toString();
        } catch (Exception e) {
            line[3] = "表体";
            TestObject para = to.getParent();
            if (para.getProperty("uIClassID").equals("TableUI")) {
                String[] rc = getTableRow(para);
                line[4] = rc[0];
                line[5] = rc[1];
            }
        }
        return line;
    }

    public String[] AnalyseComboBoxVerify(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        if (to.getProperty("class").equals("nc.ui.pub.beans.UIMultiLangCombox")) {
            line[2] = "输入";
            TestObject para = to.getParent();
            if (para.getProperty("uIClassID").equals("TableUI")) {
                line[3] = "表体多语框";
                String[] rc = getTableRow(para);
                line[4] = rc[0];
                line[5] = rc[1];
            } else {
                try {
                    line[3] = to.getProperty(".priorLabel").toString();
                } catch (Exception e) {
                }
            }
            line[6] = to.getProperty("text").toString();
            return line;
        }
        line[2] = "下拉框选择";
        line[6] = to.invoke("getSelectedItemName").toString();
        try {
            line[3] = "下拉框/" + to.getProperty(".priorLabel").toString();
        } catch (Exception e) {
            line[3] = "表体";
            TestObject para = to.getParent();
            if (para.getProperty("uIClassID").equals("TableUI")) {
                String[] rc = getTableRow(para);
                line[4] = rc[0];
                line[5] = rc[1];
            }
        }
        return line;
    }

    public String[] AnalyseTable(TestObject to) {
        Point p = Mouse.getCursorPos();
        Point loc = (Point) to.getProperty("locationOnScreen");
        Rectangle rec = (Rectangle) to.getProperty("visibleRect");
        int maxTableContentX = rec.width + loc.x - 14;
        int maxTableContentY = rec.height + loc.y - 14;
        if(p.y > maxTableContentY || p.x > maxTableContentX) {
            return null;
        }

        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "单击";
        line[3] = "表体";
        String[] rc = getTableRow(to);
        line[4] = rc[0];
        line[5] = rc[1];
        return line;
    }

    public String[] AnalyseTableVerify(TestObject to) {
        Point p = Mouse.getCursorPos();
        Point loc = (Point) to.getProperty("locationOnScreen");
        Rectangle rec = (Rectangle) to.getProperty("visibleRect");
        int maxTableContentX = rec.width + loc.x - 14;
        int maxTableContentY = rec.height + loc.y - 14;
        if(p.y > maxTableContentY || p.x > maxTableContentX) {
            return null;
        }

        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "检查点";
        line[3] = "表体";
        String[] rc = getTableRow(to);
        line[4] = rc[0];
        line[5] = rc[1];
        line[6] = rc[2];
        sleep(1);
        to.invoke("clearSelection");
        return line;
    }

    public String[] AnalyseTree(TestObject to) {
        line = new String[7];
        line[0] = getModuleName();
        line[1] = getNodeName();
        line[2] = "树选择";
        line[3] = getTreePath(to);

        return line;
    }

    // TreePath的获得
    public String path;

    public String getTreePath(TestObject to) {

        ITestDataTree cdTree;
        ITestDataTreeNodes cdTreeNodes;
        ITestDataTreeNode[] cdTreeNode;

        cdTree = (ITestDataTree) to.getTestData("selected");
        cdTreeNodes = cdTree.getTreeNodes();
        cdTreeNode = cdTreeNodes.getRootNodes();

        path = new String();
        showTree(cdTreeNode[0]);
        return path;
    }

    public void showTree(ITestDataTreeNode node)

    {
        if (path == null || path.equals(""))
            path = node.getNode().toString();
        else
            path = String.format("%s->%s", path, node.getNode());
        ITestDataTreeNode[] children = node.getChildren();
        int childCount = (children != null ? children.length : 0);

        for (int i = 0; i < childCount; ++i)
            showTree(children[i]);
    }

    // 获得Table选定单元的位置
    public String[] getTableRow(TestObject to) {
        int index = Integer
                .valueOf(to.getProperty("selectedColumn").toString());
        int row = Integer
            .valueOf(to.getProperty("selectedRow").toString());
        ITestDataTable orderTables = (ITestDataTable) to
                .getTestData("contents");
        ITestDataTable orderTable = (ITestDataTable) to.getTestData("selected");
        Object[] s = new String[orderTable.getColumnCount() * 2];
        for (int col = 0; col < orderTable.getColumnCount(); col++) {
            s[col * 2] = orderTable.getColumnHeader(col);
            s[col * 2 + 1] = orderTable.getCell(0, col);
        }
        Row r = new Row(s);
        String[] rc = { String.valueOf(orderTables.getRowIndex(r) + 1),
                orderTable.getColumnHeader(index).toString(), orderTables.getCell(row, index).toString()};
        return rc;
    }

    public TestObject JavaDomain() {
        DomainTestObject domains[] = getDomains();
        for (int i = 0; i < domains.length; i++) {
            if (domains[i].getName().toString().equals("Java")) {
                TestObject topObjects[] = domains[i].getTopObjects();
                for (int j = 0; j <= topObjects.length - 1; j++) {
                    if (topObjects[j].getProperty("class").equals(
                            "sun.plugin.viewer.frame.IExplorerEmbeddedFrame")) {
                        return topObjects[0];
                    }
                }
            }
        }
        return getNcApplet();
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

    public String getModuleName(File moduleProp, String module){
        PropertiesRW pp = new PropertiesRW(moduleProp);
        String moduleName = pp.readValue(module);
        if (moduleName == null) {
            logError("指定的模块未找到！");
        }
        return moduleName;
    }
}
