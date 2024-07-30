package utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import static base.BasePage.log;

public class ReadDataFromFile {

    private static FileInputStream fis;
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;

    @DataProvider(name="readTestData")
    public static Object[][] getTestData(Method m) throws IOException {
        log.debug("Data provider is innitiated");
        return readDataFromExcel(m.getName());
    }

    public static Object[][] readDataFromExcel(String sheetName) throws IOException {
        log.debug("Sheet name: " + sheetName);
        String excelFilePath = System.getProperty("user.dir") + "/src/test/resources/testData/ExcelDataSheet.xlsx" ;
        File excelFile = new File(excelFilePath);
        fis = new FileInputStream(excelFile);
        workbook = new XSSFWorkbook(fis);
        sheet= workbook.getSheet(sheetName);
        int totRows = sheet.getLastRowNum();
        short totCells = sheet.getRow(0).getLastCellNum();
        Object[][] data = new Object[totRows][totCells];
        for (int i = 0; i < totRows; i++) {
            XSSFRow row = sheet.getRow(i+1);
            for (int j = 0; j < totCells; j++) {
                XSSFCell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        data[i][j] = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        data[i][j] = (int)cell.getNumericCellValue();
                        break;
                    case BOOLEAN:
                        data[i][j] = cell.getBooleanCellValue();
                        break;
                    default:
                        System.out.println("Unknown cell type");
                        log.debug("Unknown cell type");
                        break;
                }
            }
        }
        return data;
    }
}
