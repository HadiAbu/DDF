package tests;

import java.io.IOException;

import DataTable.XLS_Reader;

public class Test {

	public static void main(String[] args) throws IOException {

		XLS_Reader dataTable = 
				new XLS_Reader(
				"C:\\Users\\Galil1\\Desktop\\eclipse\\tempGit"
				+ "\\MonkeyFiles\\monkey.xlsx");

		System.out.println(dataTable.getRowCount("TestCases"));
		for(int i=2;i<=dataTable.getRowCount("TestCases");i++)
			System.out.println(dataTable.getCellData("TestCases", "TCID", i));
	}

}
