package org.cherubim.filter.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.cherubim.entity.AuditLog;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class AuditAssistant {

    public static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    @Resource
    private ObjectMapper mapper;

    public void audit(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getRawPath();

        Set<Map.Entry<String, List<String>>> headers = request.getHeaders().entrySet();
        Map<String, Object> params = Maps.newHashMap();
        request.getQueryParams().forEach((k, v) -> params.put(k, v.get(0)));

        HttpMethod method = request.getMethod();
        String methodName = Optional.ofNullable(method).map(Enum::name).orElse("NONE");

        Object cachedBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);

        AuditLog auditLog = AuditLog.builder().module(this.getClass().getName()).method(methodName).headers(headers)
                .queryParams(params).url(url).body(cachedBody).build();
        try {
            log.info(mapper.writeValueAsString(auditLog));
        } catch (JsonProcessingException e) {
            log.error("", e);
        }

    }

}
