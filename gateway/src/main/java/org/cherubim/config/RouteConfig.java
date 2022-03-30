package org.cherubim.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class RouteConfig {

    private static final String LB_PLATFORM = "lb://auth-service";

    @Bean
    public RouterFunction<ServerResponse> funRouterFunction() {
        return RouterFunctions.route().build();
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("platform", r -> r.path("/platform/**").uri(LB_PLATFORM))
                .build();
    }

}
