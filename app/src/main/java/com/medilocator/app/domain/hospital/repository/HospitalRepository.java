package com.medilocator.app.domain.hospital.repository;

import com.medilocator.app.domain.hospital.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
