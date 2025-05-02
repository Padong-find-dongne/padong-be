package com.example.padongbe.domain.dongne.service;

import com.example.padongbe.domain.dongne.dto.DongMappingDto;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.entity.DongMapping;
import com.example.padongbe.domain.dongne.entity.LegalDong;
import com.example.padongbe.domain.dongne.repository.AdminDongRepository;
import com.example.padongbe.domain.dongne.repository.DongMappingRepository;
import com.example.padongbe.domain.dongne.repository.LegalDongRepository;
import com.example.padongbe.domain.dongne.util.DongneDataUtil;
import com.example.padongbe.domain.safetyGrade.entity.SafetyGrade;
import com.example.padongbe.domain.safetyGrade.service.SafetyGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DongneService {

    private final AdminDongRepository adminDongRepository;
    private final LegalDongRepository legalDongRepository;
    private final DongMappingRepository dongMappingRepository;
    private final SafetyGradeService safetyGradeService;

    public void addDongneDate() {
        List<DongMappingDto> rows = DongneDataUtil.readDongneFromExcel();

        for (DongMappingDto dto : rows) {
            AdminDong admin = adminDongRepository.findByAdminDongCode(dto.getAdminDongCode())
                    .orElseGet(() -> adminDongRepository.save(new AdminDong(dto)));

            LegalDong legal = legalDongRepository.findByLegalDongCode(dto.getLegalDongCode())
                    .orElseGet(() -> legalDongRepository.save(new LegalDong(dto)));

            if (!dongMappingRepository.existsByAdminDongAndLegalDong(admin, legal))
                dongMappingRepository.save(new DongMapping(admin, legal));
        }

    }

    public AdminDong findAdminDongByCode(String adminDongCode) {
        return adminDongRepository.findByAdminDongCode(adminDongCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행정동 코드입니다."));

    }

    public LegalDong findLegalDongByName(String dongCode) {
        System.out.println(dongCode);
        return legalDongRepository.findByLegalDongCode(dongCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 법정동 이름입니다."));
    }

    @Transactional
    public void addDongneSafetyGradeData() {
        List<AdminDong> adminDongList = adminDongRepository.findAll();
        Map<String, SafetyGrade> safetyGradeMap = safetyGradeService.getSafetyGradeMap();
        adminDongList.forEach(adminDong -> {
            SafetyGrade safetyGrade = safetyGradeMap.get(adminDong.getDistrict());
            adminDong.setSafetyGrade(safetyGrade);
            System.out.println(safetyGrade.getDistrictName());
        });
//        List<AdminDong> adminDongList = adminDongRepository.findAll();
//        adminDongList.forEach(adminDong -> {
//                    SafetyGrade safetyGrade = safetyGradeService.findByDistrictName(adminDong.getDistrict());
//                    adminDong.setSafetyGrade(safetyGrade);
//                    adminDongRepository.save(adminDong);
//                });
    }
}
