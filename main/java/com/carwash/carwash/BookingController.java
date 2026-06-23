package com.carwash.carwash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/book")
public String makeBooking(
        @RequestParam String customerName,
        @RequestParam(required = false) String surname,
        @RequestParam String email,
        @RequestParam String phone,
        @RequestParam String serviceType,
        @RequestParam String bookingDate,
        @RequestParam String description
) {

    Booking booking = new Booking();
    if (surname != null && !surname.isBlank()) {
        booking.setCustomerName(customerName + " " + surname);
    } else {
        booking.setCustomerName(customerName);
    }
    booking.setEmail(email);
    booking.setPhone(phone);
    booking.setServiceType(serviceType);
    booking.setBookingDate(bookingDate);
    booking.setDescription(description);
    booking.setStatus("PENDING");

    bookingRepository.save(booking);

    return "redirect:/appointment-status?bookingId=" + booking.getId();
}
}
