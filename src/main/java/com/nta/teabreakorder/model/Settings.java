package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "key")
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Column(unique = true, name = "code")
    private String code;
    @NotNull
    private String value;
}
