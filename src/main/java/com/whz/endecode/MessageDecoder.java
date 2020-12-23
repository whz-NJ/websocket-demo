package com.whz.endecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whz.vo.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {
  @Override public Message decode(String jsonMessage) throws DecodeException {
    ObjectMapper mapper = new ObjectMapper();
    Message message = null;
    try {
      message = mapper.readValue(jsonMessage, Message.class);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return message;
  }

  @Override public boolean willDecode(String jsonMessage) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.readValue(jsonMessage, Message.class);
      return true;
    }
    catch (IOException e) {
      return false;
    }
  }

  @Override public void init(EndpointConfig endpointConfig) {

  }

  @Override public void destroy() {

  }
}