package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.model.Settings;
import com.nta.teabreakorder.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping("")
    public ResponseEntity getAll() throws Exception {
        return settingService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) throws Exception {
        return settingService.getById(id);
    }

    @GetMapping("/by-key")
    public ResponseEntity getAll(@RequestParam String key) throws Exception {
        return settingService.getByKey(key);
    }


    @PostMapping("")
    public ResponseEntity create(@RequestBody Settings s) throws Exception {
        return settingService.create(s);
    }


    @PutMapping("")
    public ResponseEntity update(@RequestBody Settings s) throws Exception {
        return settingService.update(s);
    }


    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return settingService.deletes(ids);
    }
}
