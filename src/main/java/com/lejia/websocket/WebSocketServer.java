package com.lejia.websocket;

import com.lejia.dto.Result;
import net.sf.json.JSONArray;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/websocket/{numId}/{gId}/{erIsPack}")
@Component
//@WebListener
public class WebSocketServer {
    public static  final org.slf4j.Logger log = LoggerFactory.getLogger(WebSocketServer.class);
/*    @Autowired private AuctionMapper auctionMapper;
    @Autowired private AuctionDepositMapper auctionDepositMapper;
    @Autowired private AuctionDepositService auctionDepositService;*/
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
   // private  static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    //private static Set<WebSocketServer> wsSet =new HashSet<WebSocketServer>();
   // private static  ConcurrentHashMap<String,Set<WebSocketServer>> webSocketSet = new ConcurrentHashMap<String,Set<WebSocketServer>>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //当前会话的httpsession
    private HttpSession httpSession;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config,@PathParam("numId") String numId,@PathParam("gId") String gId,@PathParam("erIsPack") Integer erIsPack) {

        this.session =session;
        // 得到httpSession
      //  HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
       // log.info("config:{}", config.getUserProperties().get("name"));
       // log.info("session:{}", config.getUserProperties().get("sessionid"));
        String keyId="";
        String keyStr="";//log_Str
        if(erIsPack==0) {//商品是否打包 erIsPack
             keyId=numId+"_"+gId;
             keyStr="非打包竟拍 numId_gId";
        } else if(erIsPack==1)
        {
             keyId="_"+gId;
            keyStr="打包竟拍 _gId";
        }
        if(WebSocketUtil.webSocketSetSize()>0)
        {
            WebSocketUtil.setWsSet(WebSocketUtil.getWebSocketSet().get(keyId));
            if(WebSocketUtil.getWsSet()!=null&&WebSocketUtil.wsSetSize()>0)
            {
                WebSocketUtil.getWebSocketSet().get(keyId).add(this);
            }else
            {
                if(WebSocketUtil.getWsSet()==null)
                {
                    WebSocketUtil.setWsSet(new HashSet<WebSocketServer>());
                }
                WebSocketUtil.getWsSet().add(this);
                WebSocketUtil.getWebSocketSet().put(keyId,WebSocketUtil.getWsSet());
            }
        }else
        {
            if(WebSocketUtil.getWsSet()==null)
            {
                WebSocketUtil.setWsSet(new HashSet<WebSocketServer>());
            }
            WebSocketUtil.getWsSet().add(this);
            WebSocketUtil.getWebSocketSet().put(keyId,WebSocketUtil.getWsSet());
        }
        //webSocketSet.put(keyId,this);     //加入set中
        //webSocketSet.putIfAbsent(keyId,this);     //加入set中
        WebSocketUtil.addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线总人数为" +WebSocketUtil.getOnlineCount()+";*********** "+keyStr+":"+keyId+";当前在线人数"+WebSocketUtil.wsSetSize());
        log.info("有新连接加入！当前在线总人数为" + WebSocketUtil.getOnlineCount()+"*********** +keyId+:"+keyStr+";当前在线人数"+WebSocketUtil.wsSetSize());
        log.info("【websocket消息】有新的连接, 商品总数:", WebSocketUtil.webSocketSetSize());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("numId") String numId,@PathParam("gId") String gId,@PathParam("erIsPack") Integer erIsPack) {

        String keyId="";
        String sessionId="";
        String keyStr="";//log_Str
        if(erIsPack==0) {//商品是否打包 erIsPack
            keyStr="非打包竟拍 numId_gId";
        } else if(erIsPack==1)
        {
            keyStr="打包竟拍 _gId";
        }
        if(WebSocketUtil.getWebSocketSet().size()>0)
        {
            Iterator<String> keys=WebSocketUtil.getWebSocketSet().keySet().iterator();
            while (keys.hasNext())
            {
                keyId=keys.next();
                if(WebSocketUtil.getWebSocketSet().get(keyId).size()>0)
                {
                    if(WebSocketUtil.getWebSocketSet().get(keyId).contains(this))
                    {
                        WebSocketUtil.getWebSocketSet().get(keyId).remove(this); //从set中删除
                    }
                  //  sessionId=webSocketSet.get(keyId).session.getId();

                }
            }
        }
        this.session=null;
       /* try {
            session.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }*/
        WebSocketUtil.subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前总在线人数为" + WebSocketUtil.getOnlineCount()+"*********** "+keyStr+keyId);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message,@PathParam("numId") String numId,@PathParam("gId") String gId,@PathParam("erIsPack") Integer erIsPack, Session session) {

        String msg="";
        //String keyId=numId+"_"+gId;
        String keyId="";
        String keyStr="";//log_Str
        String keyStr2="";//log_Str
        if(erIsPack==0) {//商品是否打包 erIsPack
            keyId=numId+"_"+gId;
            keyStr="numId:"+numId+";gId:"+gId;
            keyStr2="非打包竟拍 numId_gId";
        } else if(erIsPack==1)
        {
            keyId="_"+gId;
            keyStr="gId:"+gId;
            keyStr2="打包竟拍 _gId";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> mapData= new HashMap<String, Object>();
        int priceCount=0;//出价次数
        if(!message.trim().contains("与服务器建立连接成功"))
        {

            msg=""+map;
            msg = "{\"code\":\"" +  Result.OK + "\", \"data\":" + JSONArray.fromObject(mapData) + "}";
        }
        else
        {
           // msg="来自客户端的消息:" + message+"numId:"+numId+";gId:"+gId;
            msg="来自客户端的消息:" + message+keyStr;
        }

        System.out.println(msg);

        // 群发消息
        if(WebSocketUtil.webSocketSetSize()>0)
        {
            //wsSet=WebSocketUtil.getWebSocketSet().get(keyId);//socket_Set
            WebSocketUtil.setWsSet(WebSocketUtil.getWebSocketSet().get(keyId));
            if(WebSocketUtil.getWsSet()!=null&&WebSocketUtil.getWsSet().size()>0)
            {
                log.info("************************************************");
                log.info("信息广播开始:"+keyStr2+":"+keyId+"*****");
                log.info("************************************************");
                for (WebSocketServer item :WebSocketUtil.getWsSet())
                {
                   try {
                       item.sendMessage(msg);
                       log.info("信息广播:"+keyStr2+"_sessionId:"+keyId+"_"+item.session.getId());
                       log.info("信息广播:信息msg:"+msg);
                   }catch (IOException e) {
                       e.printStackTrace();
                       continue;
                   }
                }
                log.info("***********************************************");
                log.info("信息广播结束:"+keyStr2+":"+keyId+"*****");
                log.info("***********************************************");
            }
        }
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(@PathParam("numId") String numId,@PathParam("gId") String gId,@PathParam("erIsPack") Integer erIsPack) throws IOException {

        String keyId="";
        String keyStr="";//log_Str
        String keyStr2="";//log_Str
        if(erIsPack==0) {//商品是否打包 erIsPack
            keyId=numId+"_"+gId;
            keyStr2="非打包竟拍 numId_gId";
        } else if(erIsPack==1)
        {
            keyId="_"+gId;
            keyStr2="打包竟拍 _gId";
        }
        String msg=auctionAfterInfo(Integer.valueOf(numId),Integer.valueOf(gId),erIsPack);
        // 群发消息
        if(WebSocketUtil.webSocketSetSize()>0&&msg.trim().length()>0)
        {
           // wsSet=WebSocketUtil.getWebSocketSet().get(keyId);//socket_Set
            WebSocketUtil.setWsSet(WebSocketUtil.getWebSocketSet().get(keyId));
            if(WebSocketUtil.getWsSet()!=null&&WebSocketUtil.wsSetSize()>0)
            {
                log.info("************************************************");
                log.info("信息广播开始:"+keyStr2+":"+keyId+"*****");
                log.info("************************************************");
                for (WebSocketServer item :WebSocketUtil.getWsSet())//socket_Set
                {
                    try {
                        item.sendMessage(msg);
                        log.info("出价成功、保证金支付成功，广播信息【最近10次出价的记录，状态=2支付成功的保证金列表】");
                        log.info(msg);
                        log.info("信息广播:"+keyStr2+"_sessionId:"+keyId+"_"+item.session.getId());
                        log.info("信息广播:信息msg:"+msg);
                    }catch (IOException e) {
                        e.printStackTrace();
                        log.info(String.format("出价成功、保证金支付成功，广播信息异常【[%s]",e.getMessage())+"】");
                        continue;
                    }
                }
                log.info("***********************************************");
                log.info("信息广播结束:"+keyStr2+":"+keyId+"*****");
                log.info("***********************************************");
            }
        }
    }

    /*
	  出价成功后或保证金支付成功后
	  返回广播信息
	 */
    public static String auctionAfterInfo(Integer numId,Integer gId,Integer erIsPack) {

        //******************************出价后的向所有WebSocket客户端广播信息
        String msg = "";
        return  msg;
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }



}