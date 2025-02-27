package com.tcs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="account")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @EqualsAndHashCode.Include
    @Column("account_id")
    private Long accountId;

    @Column("account_number")
    private String accountNumber;

    @Column("account_type")
    private String accountType;

    @Column("initial_balance")
    private BigDecimal initialBalance;

    private Boolean status;

    @Column("person_id")
    private Long personId;

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
