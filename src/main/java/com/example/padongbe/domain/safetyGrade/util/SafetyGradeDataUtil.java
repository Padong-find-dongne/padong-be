package com.example.padongbe.domain.safetyGrade.util;

import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SafetyGradeDataUtil {

    private final static String filePath = "data/safetyGrade.xlsx";

    public static List<SafetyGrade> readSafetyGradeFromExcel() {
        List<SafetyGrade> result = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream is = resource.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String districtName = row.getCell(0).getStringCellValue();
                double a = row.getCell(1).getNumericCellValue();
                double b = row.getCell(2).getNumericCellValue();
                double c = row.getCell(3).getNumericCellValue();
                double d = row.getCell(4).getNumericCellValue();
                double e = row.getCell(5).getNumericCellValue();
                double f = row.getCell(6).getNumericCellValue();

                SafetyGrade safetyGrade = SafetyGrade.builder()
                        .districtName(districtName)
                        .avgGrade((a+b+c+d+e+f)/6.0)
                        .build();

                result.add(safetyGrade);
            }
        } catch (IOException e) {
            log.error("Error reading Excel file");
            throw new RuntimeException("Error reading Excel file", e);
        } catch (Exception e) {
            log.error("Error processing Excel file");
            throw new RuntimeException("Error processing Excel file", e);
        }
        return result;
    }

}
