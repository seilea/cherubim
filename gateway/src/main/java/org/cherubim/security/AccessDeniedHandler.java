package org.cherubim.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class AccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        log.info("您无权限访问, 异常类型: [{}]", denied.getClass().getName());

        ServerHttpResponse response = exchange.getResponse();
        // 设置http响应状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置响应头信息Content-Type类型
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON.toString());
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap("{\"code\":-4,\"message\":\"您无权限访问\"}".getBytes()) ;

        return exchange.getResponse().writeAndFlushWith(Mono.just(Mono.just(buffer))) ;

    }

}
