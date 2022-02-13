package org.seilea.cherubim.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * 安全策略配置
 *
 * @author panhong
 */
@Slf4j
@EnableWebFluxSecurity
public class DefaultSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        // 输出加密密码
        String encodePassword = passwordEncoder().encode("123456");
        log.info("encodePassword:" + encodePassword);
        // 内存中缓存权限数据
        User.UserBuilder userBuilder = User.builder();
        UserDetails admin = userBuilder.username("admin").password(encodePassword)
                .roles("USER", "ADMIN").build();
        return new MapReactiveUserDetailsService(admin);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .anonymous().disable().csrf().disable().logout().disable().httpBasic().disable()
                .authorizeExchange().pathMatchers("/admin/**")
                .authenticated().pathMatchers("/**").permitAll()
                .and().requestCache().disable().build();
    }

}
