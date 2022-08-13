package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author AD
 *
 *
 * **/

@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order extends AuditingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "key")
    private Long id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private OrderType orderType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    List<OrderDetail> orderDetailList;

    private long total;

    @OneToOne
    @JoinColumn(name="customer_id",referencedColumnName = "id")
    private Customer customer;

    @Column(name="is_paid", columnDefinition = "bool default false")
    private boolean isPaid;

}
