package com.gdrc.microservice_arq.store.customer.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Builder
@Table(name = "customers")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The id number field most not be blank")
    @Size(min = 8, max = 8, message = "The number id length most be 8")
    @Column(name = "id_number")
    private String idNumber;

    @NotEmpty(message = "The first name field most not be blank")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "The last name field most not be blank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "The email field most not be blank")
    @Email(message = "Wrong email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    @NotNull(message = "The region most not be empty")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Region region;

    private String state;

}
