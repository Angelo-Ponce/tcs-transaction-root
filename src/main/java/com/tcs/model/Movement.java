package com.tcs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="movement")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movement {

    @Id
    @EqualsAndHashCode.Include
    private Long movementId;

    @Column("account_id")
    private Long accountId;

    @Column("movement_date")
    private LocalDateTime movementDate;

    @Column("movement_type")
    private String movementType;

    @Column("movement_value")
    private BigDecimal movementValue;

    @Column("balance")
    private BigDecimal balance;

    private Boolean status;

    // Campos de auditoria
    @CreatedBy
    @Column("created_by_user")
    private String createdByUser;

    @CreatedDate
    @Column("created_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column("last_modified_by_user")
    private String lastModifiedByUser;

    @LastModifiedDate
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;
}
