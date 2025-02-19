package com.tcs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Immutable;

@Data
@Immutable
@Entity(name = "person")
public class PersonView {

    @Id
    @Column(name = "person_id")
    private Long personId;

    private String identificacion;

    private String name;

    private String gender;

    private Integer age;

    private String address;

    private String phone;
}
