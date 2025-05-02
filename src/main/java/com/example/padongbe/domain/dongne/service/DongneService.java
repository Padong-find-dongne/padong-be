package com.example.padongbe.domain.dongne.service;

import com.example.padongbe.domain.dongne.dto.DongMappingDto;
import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.entity.DongMapping;
import com.example.padongbe.domain.dongne.entity.LegalDong;
import com.example.padongbe.domain.dongne.repository.AdminDongRepository;
import com.example.padongbe.domain.dongne.repository.DongMappingRepository;
import com.example.padongbe.domain.dongne.repository.LegalDongRepository;
import com.example.padongbe.domain.dongne.util.DongneDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DongneService {

    private final AdminDongRepository adminDongRepository;
    private final LegalDongRepository legalDongRepository;
    private final DongMappingRepository dongMappingRepository;

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
        System.out.println("adminDongCode: "+adminDongCode);
        return adminDongRepository.findByAdminDongCode(adminDongCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행정동 코드입니다."));

    }
}
