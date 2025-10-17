package com.example.parking.repository;

import com.example.parking.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SlotRepository extends JpaRepository<ParkingSlot, Long> {
    Optional<ParkingSlot> findByLabel(String label);
    List<ParkingSlot> findByOccupiedFalse();
}