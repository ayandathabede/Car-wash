package com.carwash.carwash;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final List<String> BOOKING_STATUSES = List.of("PENDING", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED");

    @Autowired
    private BookingRepository bookingRepository;

    @RequestMapping({ "", "/", "/dashboard", "/bookings" })
    public String bookings(Model model) {
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        Map<String, Long> statusCounts = bookings.stream()
                .map(Booking::getStatus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", BOOKING_STATUSES);
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("pendingBookings", statusCounts.getOrDefault("PENDING", 0L));
        model.addAttribute("confirmedBookings", statusCounts.getOrDefault("CONFIRMED", 0L));
        model.addAttribute("completedBookings", statusCounts.getOrDefault("COMPLETED", 0L));
        model.addAttribute("cancelledBookings", statusCounts.getOrDefault("CANCELLED", 0L));
        return "admin-dashboard";
    }

    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (!BOOKING_STATUSES.contains(status)) {
            redirectAttributes.addFlashAttribute("error", "Please select a valid booking status.");
            return "redirect:/admin/dashboard";
        }

        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            redirectAttributes.addFlashAttribute("error", "Booking could not be found.");
            return "redirect:/admin/dashboard";
        }

        booking.setStatus(status);
        bookingRepository.save(booking);
        redirectAttributes.addFlashAttribute("success", "Booking status updated.");

        return "redirect:/admin/dashboard";
    }
}
