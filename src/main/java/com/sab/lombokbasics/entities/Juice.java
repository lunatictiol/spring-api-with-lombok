package com.sab.lombokbasics.entities;

import com.sab.lombokbasics.model.JuiceStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Juice {
   @Id
   @GeneratedValue(generator = "UUID")
   @UuidGenerator
   @JdbcTypeCode(SqlTypes.CHAR)
   @Column(length = 36,columnDefinition = "varchar(36)",updatable = false,nullable = false)
    private UUID id;
   @Version
    private Integer version;

   @NotNull
   @NotBlank
   @Column(length = 50)
   @Size(max = 50)
    private String juiceName;
    @NotNull
    @NotBlank
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    @JdbcTypeCode(value = SqlTypes.SMALLINT)
    private JuiceStyle juiceStyle;
    @NotNull
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
