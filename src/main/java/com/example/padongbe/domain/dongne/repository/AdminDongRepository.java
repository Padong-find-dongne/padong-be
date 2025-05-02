package com.example.padongbe.domain.dongne.repository;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminDongRepository extends JpaRepository<AdminDong, Long> {

    Optional<AdminDong> findByAdminDongCode(String adminDongCode);

}
