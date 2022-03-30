package org.cherubim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * 资源服务器配置
 */
@EnableWebFluxSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                // 对于 userInfo 这个api 需要 s
                .pathMatchers("/userInfo").authenticated().and()
                .csrf().disable().httpBasic().disable().anonymous().disable().formLogin().disable()
                // 设置session是无状态的
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2ResourceServer().jwt()
                .and()
                // 此时是认证失败
                .authenticationEntryPoint(new AuthenticationEntryPoint())
                // 认证成功后，无权限访问
                .accessDeniedHandler(new AccessDeniedHandler())
                .and().build();
    }

}
