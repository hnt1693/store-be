package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "warhouse")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarHouseItem extends AuditingModel implements Cloneable {
    @JsonProperty(value = "key")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Min(value = 0, message = "Value not wrong")
    private int quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "warhouse_id", referencedColumnName = "id")
    private List<Transaction> transactionList = new ArrayList<>();


    private int version = 0;

    public void addTransaction() {
        this.transactionList.add(new Transaction(0L, EntityType.WARHOUSE, cloneTransaction()));
    }

    public void removeTransaction(Transaction transaction) {
        this.setTransactionList(transactionList.stream().filter(t -> t.getId() != transaction.getId()).collect(Collectors.toList()));
    }

    public void updateTransaction(int version, WarHouseItem warHouseItem) {
        Transaction transactionDB = transactionList.stream().filter(t -> {
            try {
                if (CommonUtil.getObjectMapper().readValue(t.getContent(), new TypeReference<WarHouseItem>() {
                }).getVersion() == version) {
                    return true;
                } else {
                    return false;
                }
            } catch (JsonProcessingException e) {
                return false;
            }
        }).findFirst().get();
        transactionDB.setContent(warHouseItem);
        transactionDB.setEntityType(EntityType.WARHOUSE);
    }

    public WarHouseItem cloneTransaction() {
        try {
            WarHouseItem clone = (WarHouseItem) this.clone();
            clone.setTransactionList(null);
            return clone;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void addNewVersionTransaction() {
        this.version++;
    }
}
