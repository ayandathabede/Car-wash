package com.carwash.carwash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppointmentStatusController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/appointment-status")
    public String appointmentStatus(
            @RequestParam(required = false) Long bookingId,
            Model model) {

        if (bookingId != null) {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            model.addAttribute("booking", booking);
            model.addAttribute("searched", true);
        } else {
            model.addAttribute("searched", false);
        }

        return "appointment-status";
    }
}
