package com.example.padongbe.domain.activityMobility.util;

import com.example.padongbe.domain.activityMobility.entity.Mobility;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ActivityMobilityDataUtill {

    private final static String filePath = "data/HW_이동인구_요약_2025_03.xlsx";

    public static List<Mobility> readActivityMobilityFromExcel() {
        long start = System.currentTimeMillis();
        List<Mobility> result = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(filePath);
        try (InputStream is = resource.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String month = String.valueOf(row.getCell(0));
                String departureCode = String.valueOf(row.getCell(1));
                String arrivalCode = String.valueOf(row.getCell(2));
                double totalMobility = Double.parseDouble(String.valueOf(row.getCell(3)));
                double avgTime= Double.parseDouble(String.valueOf(row.getCell(4)));


                Mobility mobility = Mobility.builder()
                        .month(month)
                        .departureCode(departureCode)
                        .arrivalCode(arrivalCode)
                        .totalMobility(totalMobility)
                        .avgTime(avgTime)
                        .build();

                result.add(mobility);
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

}
