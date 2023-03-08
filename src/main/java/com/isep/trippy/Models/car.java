package com.isep.trippy.Models;

import jakarta.persistence.*;

@Entity
public class car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int id;

    @Column(name="engineType")
    public String engineType;

    @Column(name="drivingFrequency")
    public int drivingFrequency;

    @Column(name="carAge")
    public int carAge;
}
