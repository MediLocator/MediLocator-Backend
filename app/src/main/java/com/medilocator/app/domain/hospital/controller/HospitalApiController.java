package com.medilocator.app.domain.hospital.controller;

import com.medilocator.app.domain.hospital.domain.Hospital;
import com.medilocator.app.domain.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hospital")
public class HospitalApiController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalApiController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping("/nearby-hospital")
    public ResponseEntity<List<Hospital>> getNearbyHospitals(@RequestBody Double lat, Double lng) {

        List<Hospital> nearByHospitals = hospitalService.getNearByHospitals(lat, lng);

        return ResponseEntity.ok(nearByHospitals);
    }
}
