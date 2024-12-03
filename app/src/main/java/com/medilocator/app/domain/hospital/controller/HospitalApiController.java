package com.medilocator.app.domain.hospital.controller;

import com.medilocator.app.domain.hospital.domain.Hospital;
import com.medilocator.app.domain.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/hospital")
public class HospitalApiController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalApiController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Operation(summary = "주변 병원 조회", description = "현재 위치를 기반으로 주변 병원을 조회합니다.")
    @PostMapping("/nearby")
    public ResponseEntity<List<Hospital>> getNearbyHospitals(@RequestBody Double lat, Double lng) {

        List<Hospital> nearByHospitals = hospitalService.getNearByHospitals(lat, lng);

        return ResponseEntity.ok(nearByHospitals);
    }

    @Operation(summary = "병원 상세 조회", description = "병원의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> getHospitalDetails(@PathVariable Long id) {
        Hospital hospital = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(hospital);
    }
}
