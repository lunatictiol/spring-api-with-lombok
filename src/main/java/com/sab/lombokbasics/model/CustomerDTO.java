package com.sab.lombokbasics.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
    @Builder
    public class CustomerDTO {

        private UUID id;
        @NotNull
        @NotBlank
        private String name;

        private Integer version;
        private LocalDateTime createdDate;
        private LocalDateTime updateDate;
    }

