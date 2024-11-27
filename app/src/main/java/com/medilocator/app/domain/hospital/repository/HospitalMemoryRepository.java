package com.medilocator.app.domain.hospital.repository;

import com.medilocator.app.domain.hospital.domain.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HospitalMemoryRepository {

    private static List<Hospital> hospitalList;

    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalMemoryRepository(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Scheduled(fixedRate = 600000)
    public void updateHospitalList() {
        hospitalList = hospitalRepository.findAll();
    }

    public List<Hospital> getHospitalList(){
        return hospitalList;
    }

}
