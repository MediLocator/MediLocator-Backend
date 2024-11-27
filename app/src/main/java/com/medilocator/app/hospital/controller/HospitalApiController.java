package com.medilocator.app.hospital.controller;

import com.medilocator.app.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HospitalApiController {

    HospitalService hospitalService;

    @Autowired
    public HospitalApiController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public ResponseEntity<Object> getNearbyHospitals(@RequestBody String al, String ll) {

        /**
         * REQUEST DTO를 통해 위도 경도 데이터 수집
         * Service 단에서 가까운 목록 받아옴
         */

        hospitalService.getNearbyHospitals();


    }


}
