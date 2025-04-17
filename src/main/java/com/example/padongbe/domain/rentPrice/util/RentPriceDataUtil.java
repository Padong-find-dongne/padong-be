package com.example.padongbe.domain.rentPrice.util;

import com.example.padongbe.domain.rentPrice.entity.RentPrice;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RentPriceDataUtil {

    private final static String filePath = "../src/data/rentPriceData.xlsx";

    public static List<RentPrice> readRentPricesFromExcel() {
        long start = System.currentTimeMillis();
        List<RentPrice> result = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("data/rentPriceData.xlsx");
        try (InputStream is = resource.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String dongName = getCellValue(row.getCell(0));
                String districtName = getCellValue(row.getCell(1));
                String districtCode = getCellValue(row.getCell(2));
                String dongCode = getCellValue(row.getCell(3));
                String avgJeonseDeposit = getCellValue(row.getCell(4));
                String avgMonthlyDeposit = getCellValue(row.getCell(5));
                String avgMonthlyRent = getCellValue(row.getCell(6));

                RentPrice rent = RentPrice.builder()
                        .dongName(dongName)
                        .districtName(districtName)
                        .districtCode(districtCode.isEmpty() ? null : Long.parseLong(districtCode))
                        .dongCode(dongCode.isEmpty() ? null : Long.parseLong(dongCode))
                        .avgJeonseDeposit(avgJeonseDeposit.isEmpty() ? null : Long.parseLong(avgJeonseDeposit))
                        .avgMonthlyDeposit(avgMonthlyDeposit.isEmpty() ? null : Long.parseLong(avgMonthlyDeposit))
                        .avgMonthlyRent(avgMonthlyRent.isEmpty() ? null : Long.parseLong(avgMonthlyRent))
                        .build();

                result.add(rent);
            }
        } catch (IOException e) {
            log.error("Error reading Excel file");
            throw new RuntimeException("Error reading Excel file", e);
        } catch (Exception e) {
            log.error("Error processing Excel file");
            throw new RuntimeException("Error processing Excel file", e);
        }
        long end = System.currentTimeMillis();
        log.info("RentPrice: 총 {}건, 소요 시간: {}ms", result.size(), (end - start));
        return result;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            double doubleValue = cell.getNumericCellValue();
            long longValue = (long) doubleValue;
            return String.valueOf(longValue);
        } else {
            return cell.getStringCellValue().trim();
        }
    }
}
