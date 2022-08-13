package com.nta.teabreakorder.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.config.AuditingModel;
import com.nta.teabreakorder.enums.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends AuditingModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EntityType entityType;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    public Transaction(Long id, @NotNull EntityType entityType, Object content) {
        this.entityType = entityType;
        this.id = id;
        this.setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(Object content) {
        try {
            this.content = CommonUtil.getObjectMapper().writeValueAsString(content);
        } catch (JsonProcessingException e) {
            this.content = null;
        }
    }
}
