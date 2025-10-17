package com.example.parking.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // simple reference to user.username

    private String slotLabel;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double pricePaid;

    public Booking() {}

    public Booking(String username, String slotLabel, LocalDateTime startTime) {
        this.username = username;
        this.slotLabel = slotLabel;
        this.startTime = startTime;
    }

    // getters/setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getSlotLabel() { return slotLabel; }
    public void setSlotLabel(String s) { this.slotLabel = s; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime t) { this.startTime = t; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime t) { this.endTime = t; }
    public double getPricePaid() { return pricePaid; }
    public void setPricePaid(double p) { this.pricePaid = p; }
    public boolean isActive() { return endTime == null; }
}