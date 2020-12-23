package com.whz.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
 
import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
 
/**
 * @ClassName ServerConfigurator
 * @Description: server 配置，进行行为修改，鉴权等曹邹
 * @Author 
 * @Date 2020/1/4 11:11
 * @Version V1.0
 **/
@Slf4j
@Component
public class ServerConfigurator  extends ServerEndpointConfig.Configurator {
    /**
     * token鉴权认证 临时写死123
     * @param originHeaderValue
     * @return
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getParameter("token");
        log.info("---->>>>>  token {0}",token);
        if("123".equals(token)) {
            return super.checkOrigin(originHeaderValue);
        }else{
            return false;
        }
    }
 
    /**
     * Modify the WebSocket handshake response
     * 修改websocket 返回值
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
    }
 
 
}
