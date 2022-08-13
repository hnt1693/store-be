package com.nta.teabreakorder.controller;

import com.nta.teabreakorder.common.Pageable;
import com.nta.teabreakorder.model.WarHouseItem;
import com.nta.teabreakorder.service.WarHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warhouse")
public class WarHouseController {

    @Autowired
    private WarHouseService warHouseService;

    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String searchData,
                                 @RequestParam(required = false) String sortData) throws Exception {
        Pageable pageable = Pageable.ofValue(page, pageSize, searchData, sortData, null);
        return warHouseService.get(pageable);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody WarHouseItem warHouseItem) throws Exception {
        return warHouseService.create(warHouseItem);
    }
    @PutMapping("")
    public ResponseEntity update(@RequestBody WarHouseItem warHouseItem) throws Exception {
        return warHouseService.update(warHouseItem);
    }
    @DeleteMapping("")
    public ResponseEntity deletes(@RequestBody List<Long> ids) throws Exception {
        return warHouseService.deletes(ids);
    }

}
