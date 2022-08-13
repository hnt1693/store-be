package com.nta.teabreakorder.config.socket;


import com.nta.teabreakorder.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@ServerEndpoint(value = "/ws/{token}" )
public class WebSocket {

    @Autowired
    private JwtUtils jwtUtils;

    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) throws IOException {
        token = token.replaceAll("Bearer ", "");
        try {
            WsManager.addNewSession(session);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnMessage
    public void onMessage(String txt, Session session) throws IOException {
        session.getBasicRemote().sendText("HELLO2");
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        WsManager.remoteSession(session);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.error(String.format("Error in WebSocket session %s%n", session == null ? "null" : session.getId()), t);
    }
}