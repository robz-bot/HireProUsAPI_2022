package com.promantus.hireprous;

import org.apache.poi.ss.usermodel.Sheet;

public final class HireProUsDefaultMethods {

	public static final void cleanSheet(Sheet sheet) {
		int numberOfRows = sheet.getPhysicalNumberOfRows();

		if (numberOfRows > 0) {
			for (int i = 3; i <= sheet.getLastRowNum(); i++) {
				if (sheet.getRow(i) != null) {
					sheet.removeRow(sheet.getRow(i));
				} else {
					System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... skip line: " + i);
				}
			}
		} else {
			System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... is empty");
		}
	}

}
