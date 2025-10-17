package com.example.parking.controller;

import com.example.parking.model.*;
import com.example.parking.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final SlotRepository slotRepo;
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public AdminController(SlotRepository slotRepo, BookingRepository bookingRepo, UserRepository userRepo) {
        this.slotRepo = slotRepo;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String adminHome(Model model, HttpSession session) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null || !current.isAdmin()) return "redirect:/login";
        List<ParkingSlot> slots = slotRepo.findAll();
        List<Booking> bookings = bookingRepo.findAll();
        List<User> users = userRepo.findAll();
        model.addAttribute("user", current);
        model.addAttribute("slots", slots);
        model.addAttribute("bookings", bookings);
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/slot/add")
    public String addSlot(@RequestParam String label, @RequestParam String type, @RequestParam double price, HttpSession session) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null || !current.isAdmin()) return "redirect:/login";
        if (slotRepo.findByLabel(label).isEmpty()) {
            slotRepo.save(new ParkingSlot(label, type, price));
        }
        return "redirect:/admin";
    }

    @PostMapping("/slot/remove")
    public String removeSlot(@RequestParam Long slotId, HttpSession session) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null || !current.isAdmin()) return "redirect:/login";
        slotRepo.findById(slotId).ifPresent(slot -> {
            if (!slot.isOccupied()) slotRepo.delete(slot);
        });
        return "redirect:/admin";
    }
}