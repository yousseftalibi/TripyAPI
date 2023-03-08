package com.isep.trippy.Models;

import jakarta.persistence.*;

@Entity
public class homeAppliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int id;

    @Column(name="type")
    public String type;

    @Column(name="frequency")
    public int frequency;

    @Column(name="energy_rating")
    public int energy_rating;
}
