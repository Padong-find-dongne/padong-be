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
                Long trafficAccidentGrade = (long) row.getCell(1).getNumericCellValue();
                Long fireGrade = (long) row.getCell(2).getNumericCellValue();
                Long crimeGrade = (long) row.getCell(3).getNumericCellValue();
                Long livingGrade = (long) row.getCell(4).getNumericCellValue();
                Long suicideGrade = (long) row.getCell(5).getNumericCellValue();
                Long epidemicGrade = (long) row.getCell(6).getNumericCellValue();

                SafetyGrade safetyGrade = SafetyGrade.builder()
                        .districtName(districtName)
                        .trafficAccidentGrade(trafficAccidentGrade)
                        .fireGrade(fireGrade)
                        .crimeGrade(crimeGrade)
                        .livingGrade(livingGrade)
                        .suicideGrade(suicideGrade)
                        .epidemicGrade(epidemicGrade)
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
