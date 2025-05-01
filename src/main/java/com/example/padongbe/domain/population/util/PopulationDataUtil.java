package com.example.padongbe.domain.population.util;

import com.example.padongbe.domain.population.entity.Population;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PopulationDataUtil {

    private final static String filePath = "data/population.csv";
    private final static String dongneRegex = "^서울특별시.*구.*\\(\\d{10}\\)$";

    public static List<Population> readPopulationFromCsv() {
        List<Population> result = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(filePath);

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(resource.getInputStream(), "EUC-KR"))) {

            String[] fields;
            boolean isFirstLine = true;

            while ((fields = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String rawRegion = fields[0].trim();
                if (!rawRegion.matches(dongneRegex)) {
                    continue;
                }

                String dongneCode = rawRegion.substring(rawRegion.indexOf("(") + 1, rawRegion.indexOf(")")).trim();

                // 전체 (계)
                result.add(Population.builder()
                        .dongneCode(dongneCode)
                        .gender("계")
                        .age0to9(parse(fields[4]))
                        .age10to19(parse(fields[5]))
                        .age20to29(parse(fields[6]))
                        .age30to39(parse(fields[7]))
                        .age40to49(parse(fields[8]))
                        .age50to59(parse(fields[9]))
                        .age60plus(parse(fields[10]) + parse(fields[11]) + parse(fields[12]) + parse(fields[13]))
                        .build());

                // 남성
                result.add(Population.builder()
                        .dongneCode(dongneCode)
                        .gender("남")
                        .age0to9(parse(fields[16]))
                        .age10to19(parse(fields[17]))
                        .age20to29(parse(fields[18]))
                        .age30to39(parse(fields[19]))
                        .age40to49(parse(fields[20]))
                        .age50to59(parse(fields[21]))
                        .age60plus(parse(fields[22]) + parse(fields[23]) + parse(fields[24]) + parse(fields[25]))
                        .build());

                // 여성
                result.add(Population.builder()
                        .dongneCode(dongneCode)
                        .gender("여")
                        .age0to9(parse(fields[29]))
                        .age10to19(parse(fields[30]))
                        .age20to29(parse(fields[31]))
                        .age30to39(parse(fields[32]))
                        .age40to49(parse(fields[33]))
                        .age50to59(parse(fields[34]))
                        .age60plus(parse(fields[35]) + parse(fields[36]) + parse(fields[37]) + parse(fields[38]))
                        .build());
            }

        } catch (Exception e) {
            log.error("Error reading CSV file", e);
            throw new RuntimeException("Error reading CSV file", e);
        }

        System.out.println("최종 수집된 Population 수: " + result.size());
        return result;
    }

    private static double parse(String value) {
        if (value == null || value.isBlank())
            return 0.0;

        return Double.parseDouble(value.replace(",", "").replace("\"", "").trim());
    }
}
