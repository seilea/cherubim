package org.cherubim.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        // oauth2 认证失败导致的，还有一种可能是非oauth2认证失败导致的，比如没有传递token，但是访问受权限保护的方法
        if (ex instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oAuth2AuthenticationException = (OAuth2AuthenticationException) ex;
            OAuth2Error error = oAuth2AuthenticationException.getError();
            log.info("认证失败, 异常类型: [{}], 异常: [{}]", ex.getClass().getName(), error);
        }
        return Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            // 设置http响应状态码
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            // 设置响应头信息Content-Type类型
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON.toString());
            response.writeAndFlushWith(Flux.just(Flux.just(response.bufferFactory()
                    .wrap("{\"code\":-3,\"message\":\"您无权限访问\"}".getBytes(StandardCharsets.UTF_8)))));
        });
    }

}
