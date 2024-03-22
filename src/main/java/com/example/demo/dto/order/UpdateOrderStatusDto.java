package com.example.demo.dto.order;

import com.example.demo.model.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusDto(
        @NotNull
        Status status
) {
}
