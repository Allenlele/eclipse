package base;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {
	WritableWorkbook book;
	private WritableSheet sheet;
	private WritableSheet[] sheets;

	public WritableSheet[] getSheets() {
		return sheets;
	}

	public void setSheets(WritableSheet[] sheets) {
		this.sheets = sheets;
	}

	public WriteExcel(File fn) {
		try {
			if (!fn.exists()) {
				book = Workbook.createWorkbook(fn);
				sheet = book.createSheet("test", 0);
			} else {
				Workbook wb = Workbook.getWorkbook(fn);
				book = Workbook.createWorkbook(fn, wb);
				sheet = book.getSheet(0);
			}

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WriteExcel(File fn, String[] sheets) {
		try {
			if (!fn.exists()) {
				book = Workbook.createWorkbook(fn);
			} else {
				Workbook wb = Workbook.getWorkbook(fn);
				book = Workbook.createWorkbook(fn, wb);
			}
			// sheet=book.createSheet("导出数据",0);
			this.sheets = new WritableSheet[sheets.length];
			WritableSheet wsheet = null;
			for (int i = 0; i < sheets.length; i++) {
				wsheet = book.createSheet(sheets[i], i);
				this.sheets[i] = wsheet;
			}

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setValue(String value, int col, int row) {
		try {
			jxl.write.Label label = new jxl.write.Label(col, row, value);
//			if (features != null) {
//				WritableCellFeatures cellFeatures = new WritableCellFeatures();
//				cellFeatures.setComment(features);
//				label.setCellFeatures(cellFeatures);
//			}
			sheet.addCell(label);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	public WritableSheet getSheet() {
		return sheet;
	}

	public void setSheet(WritableSheet sheet) {
		this.sheet = sheet;
	}

	public void write() {
		try {
			book.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			book.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
