package com.nta.teabreakorder.config.socket;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WsManager {

    static Map<String, Session> SESSION_POOL = new HashMap<>();


    public static void addNewSession(Session session) {
        SESSION_POOL.put(session.getId(), session);
    }

    public static void remoteSession(Session session) {
      SESSION_POOL.remove(session.getId());
    }

    public static void putToAll(WsAction data) {
        SESSION_POOL.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText(data.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
