参考： https://blog.csdn.net/moshowgame/article/details/80275084
websocket 和 springboot 的 REST 接口使用相同的端口： server.port
websocket TCP建立前，建立的是 HTTP连接，然后客户端发握手信息：
GET / HTTP/1.1
Host: 49.234.88.103:10083
Connection: Upgrade
Pragma: no-cache
Cache-Control: no-cache
Upgrade: websocket
Origin: file://
Sec-WebSocket-Version: 13
User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0
Accept-Encoding: gzip, deflate, sdch
Accept-Language: zh-CN,zh;q=0.8
Sec-WebSocket-Key: 6ceJHk7kVVuSW1ekJSAbEA==
Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits
服务端握手响应消息指定了
HTTP/1.1 101 Switching Protocols
Upgrade:websocket
Connection: Upgrade
Sec-WebSocket-Accept: SFflHjeKJY/feW3QXbHipo6ggdA=
WebSocket-Location: ws://49.234.88.103:10083

握手完成再建 Websocket TCP连接。

https://blog.csdn.net/zj20142213/article/details/100641070
