package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Notification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService extends CommonService<Notification> {
    ResponseEntity countUnread();
    ResponseEntity markReadByIds(List<Long> ids);
}
