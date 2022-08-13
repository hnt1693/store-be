package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Table(name = "order_detail")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "key")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="product_id",referencedColumnName = "id")
    private Product product;

    @Min(1)
    private int quantity;

}
