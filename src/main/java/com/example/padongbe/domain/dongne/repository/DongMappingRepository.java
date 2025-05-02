package com.example.padongbe.domain.dongne.repository;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import com.example.padongbe.domain.dongne.entity.DongMapping;
import com.example.padongbe.domain.dongne.entity.LegalDong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DongMappingRepository extends JpaRepository<DongMapping, Long>{
    boolean existsByAdminDongAndLegalDong(AdminDong admin, LegalDong legal);
}
