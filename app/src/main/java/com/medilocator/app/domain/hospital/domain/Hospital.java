package com.medilocator.app.domain.hospital.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private Integer totalDoctors;

    @Column
    private Double xCoordinate;

    @Column
    private Double yCoordinate;
}
