package com.medilocator.app.domain.hospital.service;

import com.medilocator.app.domain.hospital.domain.Hospital;
import com.medilocator.app.domain.hospital.repository.HospitalMemoryRepository;
import com.medilocator.app.domain.hospital.utils.Calculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {

    private final HospitalMemoryRepository hospitalMemoryRepository;

    @Autowired
    public HospitalService(HospitalMemoryRepository hospitalMemoryRepository) {
        this.hospitalMemoryRepository = hospitalMemoryRepository;
    }

    public List<Hospital> getNearByHospitals(Double lat1, Double lng1) {
        List<Hospital> hospitalList = hospitalMemoryRepository.getHospitalList();

        List<Hospital> result = new ArrayList<>();

        for (Hospital hospital : hospitalList) {

            Double lat2 = hospital.getXCoordinate();
            Double lng2 = hospital.getYCoordinate();

            if (lat2 != null && lng2 != null && Calculate.calculateDistance(lat1, lng1, lat2, lng2) <= 300) {
                result.add(hospital);
            }
        }
        return result;
    }

    // 병원 정보 상세 조회
    public Hospital getHospitalById(Long id) {
        List<Hospital> hospitalList = hospitalMemoryRepository.getHospitalList();

        return hospitalList.stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. ID: " + id));
    }
}
