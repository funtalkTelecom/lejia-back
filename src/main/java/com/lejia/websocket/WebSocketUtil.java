package com.lejia.websocket;/**
 * @author: Zhu.QF
 * @Date: 2020/2/19 16:20
 */

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: egt365-hk
 *
 * @description:
 *
 * @author: Zhuqf
 *
 * @Date: 2020/2/19 16:20 
 */
public class WebSocketUtil {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private  static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static Set<WebSocketServer> wsSet =new HashSet<WebSocketServer>();
    private static ConcurrentHashMap<String,Set<WebSocketServer>> webSocketSet = new ConcurrentHashMap<String,Set<WebSocketServer>>();

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    public static synchronized int webSocketSetSize() {
        return webSocketSet.size();
    }

    public static synchronized int wsSetSize() {
        return wsSet.size();
    }

    public static Set<WebSocketServer> getWsSet() {
        return wsSet;
    }

    public static void setWsSet(Set<WebSocketServer> wsSet) {
        WebSocketUtil.wsSet = wsSet;
    }

    public static ConcurrentHashMap<String, Set<WebSocketServer>> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(ConcurrentHashMap<String, Set<WebSocketServer>> webSocketSet) {
        WebSocketUtil.webSocketSet = webSocketSet;
    }
}
