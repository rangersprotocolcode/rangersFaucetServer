package com.tuntunhz.tools.faucet.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * 用于接收钱包客户端的消息
 */
@ServerEndpoint(value = "/api/faucet", configurator = HttpSessionConfigurator.class)
@Component
public class WebSocket {
    static MessageHandler messageHandler;

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private Session webSocketSession;

    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("ws server is open!");
        this.webSocketSession = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //System.out.println(this.httpSession.getId());
    }

    @OnClose
    public void onClose() {
        logger.info("ws server is close!");
    }

    @OnMessage
    public void onMessage(String message) {
        logger.debug("Server receive message:" + message);
        messageHandler.handleMessage(message, this.webSocketSession, this.httpSession);
    }

    @OnError
    public void onError(Throwable error) {
        logger.info("ws server is error!" + error.getMessage());
        error.printStackTrace();
    }

    @Autowired
    public void setMessageParser(MessageHandler messageHandler) {
        WebSocket.messageHandler = messageHandler;
    }

}

