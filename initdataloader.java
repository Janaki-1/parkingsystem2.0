package com.example.parking.service;

import com.example.parking.model.*;
import com.example.parking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Component
public class InitDataLoader implements CommandLineRunner {
    private final UserRepository userRepo;
    private final SlotRepository slotRepo;
    private final BookingRepository bookingRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public InitDataLoader(UserRepository userRepo, SlotRepository slotRepo, BookingRepository bookingRepo) {
        this.userRepo = userRepo;
        this.slotRepo = slotRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepo.existsByUsername("admin")) {
            User admin = new User("admin", encoder.encode("admin"), "Administrator", true);
            userRepo.save(admin);
        }
        if (!userRepo.existsByUsername("alice")) {
            User u = new User("alice", encoder.encode("password"), "Alice Kumar", false);
            userRepo.save(u);
        }
        if (slotRepo.count() == 0) {
            slotRepo.save(new ParkingSlot("A1","regular",15.0));
            slotRepo.save(new ParkingSlot("A2","compact",10.0));
            slotRepo.save(new ParkingSlot("B1","large",20.0));
        }

        // optional sample booking
        if (bookingRepo.count() == 0) {
            Booking b = new Booking("alice","A2", LocalDateTime.now().minusHours(1));
            b.setPricePaid(10.0);
            bookingRepo.save(b);
            // mark slot occupied
            slotRepo.findByLabel("A2").ifPresent(s -> { s.setOccupied(true); slotRepo.save(s); });
        }
    }
}