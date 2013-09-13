package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class FileTool {

	public static String[] parameter(String[] a, int loop) {
		if (a == null)
			return null;
		if (a[2].equals("输入") && a[6].endsWith("*")) {
			a[6] = a[6].replace("*", String.valueOf(loop));
		}
		return a;
	}

	public static String[] getLine(File file, int row) {
		try {
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0); // 获得第一个工作表对象
			if(row > sheet.getRows()-1){
			    return null;
			}
			String[] a = new String[7];
			for (int i = 0; i < 7; i++) {
				Cell cell1 = sheet.getCell(i, row); // 得到第一列第一行的单元格
				a[i] = cell1.getContents();
			}
			book.close();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String[] getLineWithDBInfo(File file, int row) {
		try {
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0); // 获得第一个工作表对象
			String[] a = new String[15];
			for (int i = 0; i < 15; i++) {
				Cell cell1 = sheet.getCell(i, row); // 得到第一列第一行的单元格
				a[i] = cell1.getContents().trim();
			}
			book.close();
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getXLSData(File Path, int row, int col) {
		try {
			Workbook book = Workbook.getWorkbook(Path);
			Sheet sheet = book.getSheet(0); // 获得第一个工作表对象
			String result = null;
			Cell cell1 = sheet.getCell(col, row); // 得到第一列第一行的单元格
			result = cell1.getContents().trim();
			book.close();
			return result;
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getXLSData(File Path, int shet ,int row, int col) {
		try {
			Workbook book = Workbook.getWorkbook(Path);
			Sheet sheet = book.getSheet(shet); // 获得第一个工作表对象
			String result = null;
			Cell cell1 = sheet.getCell(col, row); // 得到第一列第一行的单元格
			result = cell1.getContents().trim();
			book.close();
			return result;
		} catch (Exception e) {
		}
		return null;
	}

	public static boolean copyFile(String srcFile, String destFile) {
        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	public static String[] getModuleAndNode(File Path) {
		String[] moduleAndNode = new String[2];
		try {
			Workbook book = Workbook.getWorkbook(Path);
			Sheet sheet = book.getSheet(0); // 获得第一个工作表对象
			for(int i = 1; i < sheet.getRows(); i++) {
				Cell mCell = sheet.getCell(0, i);
				Cell nCell = sheet.getCell(1, i);
				if(mCell == null || nCell == null) {
					continue;
				}
				String module = mCell.getContents();
				String node = nCell.getContents();
				if( module!= null && !"".equals(module) && node != null && !"".equals(node)) {
					moduleAndNode[0] = module.trim();
					moduleAndNode[1] = node.trim();
					break;
				}
			}
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return moduleAndNode;
	}
}
