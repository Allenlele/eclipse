package base;

import java.io.File;
import java.util.HashMap;

import jxl.Sheet;
import jxl.Workbook;

public class DataPool {
    private static final String DP = "dp";
    private static final String SPLASH = "/";

	public static HashMap<String, String> getDPhead(File file, int row) {
		try {
			HashMap<String, String> dp = new HashMap<String, String>();
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(1); // 获得第一个工作表对象
			int i = 0;
			String cell1 = "";
			String cell2 = "";
			while (true) {
				try {
					cell1 = sheet.getCell(i, 0).getContents().trim(); // 得到第一列第一行的单元格
					cell2 = sheet.getCell(i, row).getContents().trim(); // 得到第一列第一行的单元格
				} catch (Exception e) {
					break;
				}
				if (i == 0 && (cell2.equals("") || cell2 == null))
					return null;
				if (!(cell1 == null || cell1.equals(""))) {
					dp.put(cell1, cell2);
					i++;
				} else {
					break;
				}
			}
			book.close();
			if (!dp.isEmpty())
				return dp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static String getValue(HashMap<String, String> dp,String col) {
	// return dp.get(col);
	// }

	public static String[] setValue(String[] a, HashMap<String, String> dp) {
	    String key = null;
	    String value = null;
	    if (a[2].equals("输入")) {
	        if (a[3].equals("表体")){
                if (a[4].split(SPLASH).length == 2) {
                    String[] text = a[4].split(SPLASH);
                    if (text[0].startsWith(DP)) {
                        key = text[0].substring(3,text[0].length() - 1);
                        value = dp.get(key);
                        if(value == null || "".equals(value)) {
                            return null;
                        } else {
                            a[4] = value + SPLASH;
                        }
                    } else {
                        a[4] = text[0] + SPLASH;
                    }

                    if (text[1].startsWith(DP)) {
                        key = text[1].substring(3,text[1].length() - 1);
                        value = dp.get(key);
                        if(value == null || "".equals(value)) {
                            return null;
                        } else {                            
                            a[4] = a[4] + value;
                        }
                    } else {
                        a[4] = a[4] + text[1];
                    }
                } else if(a[4].startsWith(DP)){
                    key = a[4].substring(3,a[4].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {                        
                        a[4] = value;
                    }
                }

                if (a[5].startsWith(DP)) {                    
                    key = a[5].substring(3, a[5].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {                        
                        a[5] = value;
                    }
                }
            }
	        if(a[6].startsWith("dp")) {	        	
	        	key = a[6].substring(3, a[6].length() - 1);
	        	value = dp.get(key);
	        	if(value == null || "".equals(value)) {
	        		return null;
	        	} else {                
	        		a[6] = value;
	        	}
	        }
        } else if (a[2].equals("树选择") && a[3].contains(DP)) {
            String[] nodes = a[3].split("->");
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].contains(DP)) {
                    key = nodes[i].substring(3, nodes[i].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {                        
                        if (i == 0) {
                            a[3] = value;
                        } else {
                            a[3] = a[3] + "->" + value;
                        }
                    }
                } else {
                    if(i == 0) {                        
                        a[3] = nodes[0];
                    } else {
                        a[3] = a[3] + "->" + nodes[i];
                    }
                }
            }
        } else if (a[2].equals("下拉框选择") && a[6].startsWith(DP)) {
            key = a[6].substring(3, a[6].length() - 1);
            value = dp.get(key);
            if(value == null || "".equals(value)) {
                return null;
            } else {                
                a[6] = value;
            }
        } else if (a[2].equals("打开节点") && a[3].startsWith(DP)) {
            key = a[3].substring(3, a[3].length() - 1);
            value = dp.get(key);
            if(value == null || "".equals(value)) {
                return null;
            } else {                
                a[3] = value;
            }
        } else if (a[2].equals("单击")) {
            if (a[3].equals("表体")){

                if (a[4].split(SPLASH).length == 2) {
                    String[] text = a[4].split(SPLASH);
                    if (text[0].startsWith(DP)) {
                        key = text[0].substring(3,text[0].length() - 1);
                        value = dp.get(key);
                        if(value == null || "".equals(value)) {
                            return null;
                        } else {                            
                            a[4] = value + SPLASH;
                        }
                    } else {
                        a[4] = text[0] + SPLASH;
                    }

                    if (text[1].startsWith(DP)) {
                        key = text[1].substring(3,text[1].length() - 1);
                        value = dp.get(key);
                        if(value == null || "".equals(value)) {
                            return null;
                        } else {                            
                            a[4] = a[4] + value;
                        }
                    } else {
                        a[4] = a[4] + text[1];
                    }
                } else if(a[4].startsWith(DP)){
                    key = a[4].substring(3,a[4].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {
                        a[4] = value;
                    }
                }

                if (a[5].startsWith(DP)) {                    
                    key = a[5].substring(3, a[5].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {                        
                        a[5] = value;
                    }
                }
                if (a[6].startsWith(DP)) {                    
                    key = a[6].substring(3, a[6].length() - 1);
                    value = dp.get(key);
                    if(value == null || "".equals(value)) {
                        return null;
                    } else {                        
                        a[6] = value;
                    }
                }
            } else if ((a[3].equals("按钮") || a[3].equals("文本框")) && a[4].startsWith(DP)){
                key = a[4].substring(3, a[4].length() - 1);
                value = dp.get(key);
                if(value == null || "".equals(value)) {
                    return null;
                } else {
                    a[4] = value;
                }
            } else if ((a[3].equals("标签") || a[3].equals("列表")) && a[6].startsWith(DP)) {
                key = a[6].substring(3, a[6].length() - 1);
                value = dp.get(key);
                if(value == null || "".equals(value)) {
                    return null;
                } else {                    
                    a[6] = value;
                }
            } else if (a[3].startsWith(DP)) {
                key = a[3].substring(3, a[3].length() - 1);
                value = dp.get(key);
                if(value == null || "".equals(value)) {
                    return null;
                } else {                    
                    a[3] = value;
                }
            }
        } else if (a[2].equals("选择") && a[3].startsWith("dp")) {
            key = a[3].substring(3, a[3].length() - 1);
            value = dp.get(key);
            if(value == null || "".equals(value)) {
                return null;
            } else {                
                a[3] = value;
            }
        } else if (a[2].equals("多选") && a[3].startsWith("dp")) {
            key = a[3].substring(3, a[3].length() - 1);
            value = dp.get(key);
            if(value == null || "".equals(value)) {
                return null;
            } else {
                a[3] = value;
            }
        }

        return a;
	}

	public static void main(String[] args) {
		String key = "dp(aaa)".substring(3, "dp(aaa)".length() - 1);
		System.out.println(key);
	}

}
