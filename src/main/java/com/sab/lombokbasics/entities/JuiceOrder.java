package com.sab.lombokbasics.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity

@NoArgsConstructor
public class JuiceOrder {
    public JuiceOrder(UUID id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String customerRef, Set<JuiceOrderLine> juiceOrderLines, Customer customer,
                      JuiceOrderShipment juiceOrderShipment) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.customerRef = customerRef;
        this.juiceOrderLines = juiceOrderLines;
        this.setCustomer(customer );
        this.juiceOrderShipment = juiceOrderShipment;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
    public boolean isNew() {
        return this.id == null;
    }

    private String customerRef;

    @OneToMany(mappedBy = "juiceOrder")
    private Set<JuiceOrderLine> juiceOrderLines;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getJuiceOrders().add(this);
    }

    public void setJuiceOrderShipment(JuiceOrderShipment juiceOrderShipment) {
        this.juiceOrderShipment = juiceOrderShipment;
        juiceOrderShipment.setJuiceOrder(this);
    }
    @ManyToOne
    private Customer customer;
    @OneToOne(cascade = CascadeType.PERSIST)
    private JuiceOrderShipment juiceOrderShipment;
}
