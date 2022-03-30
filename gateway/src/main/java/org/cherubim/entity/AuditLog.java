package org.cherubim.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    private String module;

    private String method;

    private String url;

    private Set<Map.Entry<String, List<String>>> headers;

    private Map<String, Object> queryParams;

    private Object body;

}
