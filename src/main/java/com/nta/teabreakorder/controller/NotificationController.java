package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.config.socket.WsAction;
import com.nta.teabreakorder.config.socket.WsManager;
import com.nta.teabreakorder.enums.WsActionType;
import com.nta.teabreakorder.model.Notification;
import com.nta.teabreakorder.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;


    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return notificationService.get(pageable);
    }

    @GetMapping("/unread")
    public ResponseEntity getUnread() throws Exception {
        return notificationService.countUnread();
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) throws Exception {
        return notificationService.getById(id);
    }


    @PostMapping("")
    public ResponseEntity create(@RequestBody Notification menu) throws Exception {
        return notificationService.create(menu);
    }


    @PutMapping("/markread")
    public ResponseEntity markRead(@RequestBody List<Long> ids) throws Exception {
        return notificationService.markReadByIds(ids);
    }

    @PutMapping("")
    public ResponseEntity update(@RequestBody Notification menu) throws Exception {
        return notificationService.update(menu);
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return notificationService.deletes(ids);
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/reload")
    public void reload() throws Exception {
        WsManager.putToAll(new WsAction(WsActionType.NEWS,"Bạn có tin nhắn mới"));
    }
}
