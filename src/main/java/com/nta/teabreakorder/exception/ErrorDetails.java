package com.nta.teabreakorder.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nta.teabreakorder.common.Const;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDetails {
    @JsonFormat(pattern = Const.DATETIME_PATTERN)
    private Date timestamp;
    private String message;
    private String details;
    private String code;

    public ErrorDetails(Date timestamp, String message, String details) {
         super();
         this.timestamp = timestamp;
         this.message = message;
         this.details = details;
    }


    public Date getTimestamp() {
         return timestamp;
    }

    public String getMessage() {
         return message;
    }

    public String getDetails() {
         return details;
    }
}
