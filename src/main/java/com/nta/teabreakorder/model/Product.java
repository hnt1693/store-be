package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.enums.ProductType;
import com.nta.teabreakorder.model.common.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "key")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private long basePrice;

    @NotNull
    private long price;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductType type;

    private String code;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<File> photoList;
}
