package com.tcs.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "client")
public class ClientView extends PersonView {

    private String clientId;

    private String password;

    private Boolean status;
}
