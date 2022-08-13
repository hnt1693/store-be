package com.nta.teabreakorder.config.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nta.teabreakorder.common.CommonUtil;
import com.nta.teabreakorder.enums.WsActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsAction {
    private WsActionType type;
    private Object data;

    public String toJson() throws JsonProcessingException {
       return CommonUtil.getObjectMapper().writeValueAsString(this);
    }
}
