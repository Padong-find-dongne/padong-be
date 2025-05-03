package com.example.padongbe.domain.activityMobility.repository;

import com.example.padongbe.domain.activityMobility.entity.Mobility;
import java.util.List;

import com.example.padongbe.domain.dongne.entity.AdminDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MobilityRepository extends JpaRepository<Mobility,Long> {

//  List<Mobility> findByArrivalCodeOrderByTotalMobilityDesc(String arrivalCode);
  List<Mobility> findByArrivalDongOrderByTotalMobilityDesc(AdminDong adminDong);
}
