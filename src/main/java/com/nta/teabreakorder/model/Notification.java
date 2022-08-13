package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.common.Const;
import com.nta.teabreakorder.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "key")
    private Long id;

    private String content;

    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean isRead;
}
