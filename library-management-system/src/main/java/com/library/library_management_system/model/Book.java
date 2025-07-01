package com.library.library_management_system.model;

import com.library.library_management_system.emun.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private Category category;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "Base price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(name = "extra_days_rental_price", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "Extra days price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal extraDaysRentalPrice = BigDecimal.ZERO;

    @Column(name = "insurance_fees", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "Insurance fees cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal insuranceFees = BigDecimal.ZERO;

    public BigDecimal calculateTotalPrice(int durationDays) {
        if (durationDays < 1) {
            throw new IllegalArgumentException("Duration must be at least 1 day");
        }

        BigDecimal total = this.basePrice;

        // Calculate extra days charge (after 1 week)
        int extraDays = Math.max(0, durationDays - 7);
        if (extraDays > 0) {
            BigDecimal extraCharge = this.extraDaysRentalPrice
                    .multiply(BigDecimal.valueOf(extraDays));
            total = total.add(extraCharge);
        }

        // Add insurance fee
        total = total.add(this.insuranceFees);

        return total;
    }

    public boolean isAvailableForBorrow() {
        return this.available &&
                this.basePrice != null &&
                this.extraDaysRentalPrice != null &&
                this.insuranceFees != null;
    }
}