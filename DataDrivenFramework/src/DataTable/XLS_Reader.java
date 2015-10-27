package DataTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;


public class XLS_Reader {
	private String path;
	private FileInputStream fis = null;
	private FileOutputStream fileOut = null;
	protected XSSFWorkbook workbook = null;
	protected XSSFSheet sheet = null;
	protected XSSFRow row = null;
	protected XSSFCell cell = null;

	public XLS_Reader(String path) throws IOException {
		File file = new File(path);
		this.path = file.getCanonicalPath();
		FileInputStream fis = new FileInputStream(file);
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheetAt(0);
		fis.close();
	}

	// returns the row count in a sheet
	public int getRowCount(String sheetName) {
		if (workbook == null)
			return -1;
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return -1;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}

	// returns entire row if inputed value exist
	public Hashtable<String, String> getRowByValue(String value, String sheet) {

		int rows_count = getRowCount(sheet);

		for (int i = 1; i < rows_count; i++) {
			Hashtable<String, String> myArr = getRowAsHashtable(i, sheet);
			if (myArr.values().toString().contains(value)) {
				return myArr;
			}
		}
		return null;
	}

	// returns entire row if inputed key and value exist
	public Hashtable<String, String> getRowByValue(String key, String value, String sheet) {
		int rows_count = getRowCount(sheet);

		for (int i = 1; i < rows_count; i++) {
			Hashtable<String, String> myArr = getRowAsHashtable(i, sheet);
			if (myArr.get(key).equalsIgnoreCase(value))
				return myArr;
		}
		return null;
	}

	// returns the data from a cell
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);

			if (cell == null)
				return "";
			// System.out.println(cell.getCellType());
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	public Hashtable<String, String> getRowAsHashtable(int rowNum, String sheetName) {

		Hashtable<String, String> hMap = new Hashtable<String, String>();

		if (rowNum <= 0)
			return null;

		int index = workbook.getSheetIndex(sheetName);

		if (index == -1)
			return null;

		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(0);
		List<String> keys = new ArrayList<String>();

		for (int i = 0; i < row.getLastCellNum(); i++) {
			String cellValue = row.getCell(i).getStringCellValue();
			if (cellValue != null && !cellValue.trim().equals(""))
				keys.add(row.getCell(i).getStringCellValue());
		}

		row = sheet.getRow(rowNum);

		for (int i = 0; i < row.getLastCellNum(); i++) {

			String cellValue = getCellValueAsString(row, i);
			if (cellValue != null && !cellValue.trim().equals(""))
				hMap.put(keys.get(i), getCellValueAsString(row, i));
			else
				hMap.put(keys.get(i), "");
		}

		return hMap;
	}

	private String getCellValueAsString(XSSFRow row, int i) {

		XSSFCell cell = row.getCell(i);
		if (cell != null) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			try {

				return cell.getStringCellValue();
			} catch (Exception e) {
			}
			try {
				return String.valueOf(cell.getNumericCellValue());
			} catch (Exception e) {
			}
			try {
				return cell.getBooleanCellValue() + "";
			} catch (Exception e) {
			}
			try {
				return cell.getDateCellValue().toString();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public Object[][] getAllRowsAsHashtableArrayWithNoDBInteract(String sheetName) {
		
		sheetName="Sheet1";
		int numOfRows = getRowCount(sheetName);
		if (numOfRows <= 0)
			return new Object[0][0];
		List<Object> list = new ArrayList<Object>();
		Hashtable<String, String> mainRowTable;
		Hashtable<String, String> newRowTable = new Hashtable<>();

		for (int i = 1; i < numOfRows; i++) {
			mainRowTable = getRowAsHashtable(i, sheetName);
			newRowTable.putAll(mainRowTable);
			list.add(new Hashtable<>(newRowTable));
		}
		Object[][] table = new Object[list.size()][1];
		int index = 1;
		for (Object obj : list)
			table[index++ - 1][0] = obj;
		return table;
		
	}
	// INFO: Customised for 3m project !!
	// returns all excel sheet rows + sub-sheet rows if specified "Sheet Name : Column Name" in excel header.
	/*
	 * public Object[][] getAllRowsAsHashtableArray(String sheetName) {
		int numOfRows = getRowCount(sheetName);
		if (numOfRows <= 0)
			return new Object[0][0];
		List<Object> list = new ArrayList<Object>();
		Hashtable<String, String> mainRowTable;
		Hashtable<String, String> subRowTable = null;
		Hashtable<String, String> newRowTable = new Hashtable<>();

		for (int i = 1; i < numOfRows; i++) {
			mainRowTable = getRowAsHashtable(i, sheetName);
			if (mainRowTable.get(ExcelDataProvider.getDependencyKey()) == null || mainRowTable.get(ExcelDataProvider.getDependencyKey()).equals(""))
				mainRowTable.put(ExcelDataProvider.getDependencyKey(), "false");
			if (mainRowTable.isEmpty())
				continue;
			for (Map.Entry<String, String> entry : mainRowTable.entrySet()) {
				String[] subSheet = entry.getKey().split("\\s*:\\s*");
				if (subSheet.length == 2) {
					try {
						subRowTable = getRowByValue(subSheet[1], entry.getValue(), subSheet[0]);
					} catch (Exception e) {
						System.out.println(i);
					}

					if (subRowTable != null)
						newRowTable.putAll(subRowTable);
				}
			}
			newRowTable.putAll(mainRowTable);
			// added to not run disabled tests in Excel.
			String needToRun = newRowTable.get(ExcelDataProvider.getNeedToRunKey());
			if (needToRun == null || !needToRun.matches("(?i:\\s*(NO|FALSE)\\s*)"))
				list.add(new Hashtable<>(newRowTable));
		}
		Object[][] table = new Object[list.size()][1];
		int index = 1;
		for (Object obj : list)
			table[index++ - 1][0] = obj;
		return table;
	}
*/
	// returns the data from a cell
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;
			}
			if (colNum == -1)
				return false;

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			// cell style
			// CellStyle cs = workbook.createCellStyle();
			// cs.setWrapText(true);
			// cell.setCellStyle(cs);
			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		// System.out.println("setCellData setCellData******************");
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			// System.out.println("A");
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
					colNum = i;
			}

			if (colNum == -1)
				return false;
			sheet.autoSizeColumn(colNum); // ashish
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();

			// cell style for hyperlinks
			// by default hypelrinks are blue and underlined
			CellStyle hlink_style = workbook.createCellStyle();
			XSSFFont hlink_font = workbook.createFont();
			hlink_font.setUnderline(XSSFFont.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
			// hlink_style.setWrapText(true);

			XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
			link.setAddress(url);
			cell.setHyperlink(link);
			cell.setCellStyle(hlink_style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if sheet is created successfully else false
	public boolean addSheet(String sheetname) {

		FileOutputStream fileOut;
		try {
			workbook.createSheet(sheetname);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if sheet is removed successfully else false if sheet does
	// not exist
	public boolean removeSheet(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return false;

		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// returns true if column is created successfully
	public boolean addColumn(String sheetName, String colName) {
		// System.out.println("**************addColumn*********************");

		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			if (row == null)
				row = sheet.createRow(0);

			// cell = row.getCell();
			// if (cell == null)
			// System.out.println(row.getLastCellNum());
			if (row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());

			cell.setCellValue(colName);
			cell.setCellStyle(style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try {
			if (!isSheetExist(sheetName))
				return false;
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			@SuppressWarnings("unused")
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			style.setFillPattern(HSSFCellStyle.NO_FILL);

			for (int i = 0; i < getRowCount(sheetName); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(colNum);
					if (cell != null) {
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	// find whether sheets exists
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	// returns number of columns in a sheet
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

	}

	// String sheetName, String testCaseName,String keyword ,String URL,String
	// message
	public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url, String message) {
		// System.out.println("ADDING addHyperLink******************");

		url = url.replace('\\', '/');
		if (!isSheetExist(sheetName))
			return false;

		sheet = workbook.getSheet(sheetName);

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
				// System.out.println("**caught "+(i+index));
				setCellData(sheetName, screenShotColName, i + index, message, url);
				break;
			}
		}

		return true;
	}

	public int getCellRowNum(String sheetName, String colName, String cellValue) {

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;

	}

	public String getPath() {
		return path;
	}
}