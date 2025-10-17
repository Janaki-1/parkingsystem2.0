package com.example.parking.controller;

import com.example.parking.model.*;
import com.example.parking.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class UserController {
    private final SlotRepository slotRepo;
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public UserController(SlotRepository slotRepo, BookingRepository bookingRepo, UserRepository userRepo) {
        this.slotRepo = slotRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";
        List<ParkingSlot> available = slotRepo.findByOccupiedFalse();
        List<Booking> myBookings = bookingRepo.findByUsername(current.getUsername());
        model.addAttribute("user", current);
        model.addAttribute("available", available);
        model.addAttribute("bookings", myBookings);
        return "dashboard";
    }

    @PostMapping("/book")
    public String book(@RequestParam String label, HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";
        var opt = slotRepo.findByLabel(label);
        if (opt.isEmpty()) { model.addAttribute("error","Slot not found"); return "redirect:/dashboard"; }
        ParkingSlot slot = opt.get();
        if (slot.isOccupied()) { model.addAttribute("error","Slot occupied"); return "redirect:/dashboard"; }
        Booking b = new Booking(current.getUsername(), slot.getLabel(), LocalDateTime.now());
        // price will be computed on release
        bookingRepo.save(b);
        slot.setOccupied(true);
        slotRepo.save(slot);
        return "redirect:/dashboard";
    }

    @PostMapping("/release")
    public String release(@RequestParam Long bookingId, HttpSession session) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";
        var opt = bookingRepo.findById(bookingId);
        if (opt.isEmpty()) return "redirect:/dashboard";
        Booking b = opt.get();
        if (!b.isActive() || !b.getUsername().equals(current.getUsername())) return "redirect:/dashboard";
        LocalDateTime end = LocalDateTime.now();
        b.setEndTime(end);
        // compute duration and price
        var slotOpt = slotRepo.findByLabel(b.getSlotLabel());
        double price = 0.0;
        if (slotOpt.isPresent()) {
            ParkingSlot s = slotOpt.get();
            Duration dur = Duration.between(b.getStartTime(), end);
            double hours = Math.max(0.25, dur.toMinutes() / 60.0); // minimum quarter hour
            price = Math.ceil(hours * 100.0) / 100.0 * s.getPricePerHour();
            b.setPricePaid(price);
            s.setOccupied(false);
            slotRepo.save(s);
        }
        bookingRepo.save(b);
        return "redirect:/dashboard";
    }
}