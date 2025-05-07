package com.example.padongbe.domain.activityMobility.repository;

import com.example.padongbe.domain.activityMobility.entity.Mobility;
import java.util.List;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MobilityRepository extends JpaRepository<Mobility,Long> {

  Page<Mobility> findByArrivalDongOrderByTotalMobility(AdminDong adminDong, Pageable pageable);

  List<Mobility> findByArrivalDong(AdminDong adminDong);
}
