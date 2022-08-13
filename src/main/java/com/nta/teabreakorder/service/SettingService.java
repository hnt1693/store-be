package com.nta.teabreakorder.service;

import com.nta.teabreakorder.model.Settings;
import org.springframework.http.ResponseEntity;

public interface SettingService extends CommonService<Settings> {
    ResponseEntity getAll() throws Exception;
    ResponseEntity getByKey(String key) throws Exception;
}
