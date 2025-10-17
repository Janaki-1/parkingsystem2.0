package com.example.parking.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slots")
public class ParkingSlot implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String label; // e.g., A1

    private String type; // compact/regular/large

    private boolean occupied = false;

    private double pricePerHour = 10.0; // default price

    public ParkingSlot() {}

    public ParkingSlot(String label, String type, double pricePerHour) {
        this.label = label;
        this.type = type;
        this.pricePerHour = pricePerHour;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getLabel() { return label; }
    public void setLabel(String l) { this.label = l; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean o) { this.occupied = o; }
    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double p) { this.pricePerHour = p; }
}