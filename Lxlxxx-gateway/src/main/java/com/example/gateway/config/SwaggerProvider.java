package com.example.gateway.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Primary
@Component
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String SOURCE_URI = "http://%s/swagger-resources";
    private final RouteLocator routeLocator;
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final RestTemplate restTemplate;

    /**
     * 通过网关路由聚合其他服务接口
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        routeDefinitionLocator.getRouteDefinitions()
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .subscribe(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> resources
                                .addAll(swaggerResource(routeDefinition))));

        return resources;
    }

    private List<SwaggerResource> swaggerResource(RouteDefinition route) {

        try {
            String sourceUrl = String.format(SOURCE_URI, route.getUri().getHost());
            ResponseEntity<String> content = restTemplate.getForEntity(sourceUrl, String.class);
            List<SwaggerResource> swaggerResources = JSONUtil.toList(new JSONArray(content.getBody()), SwaggerResource.class);

            swaggerResources.stream().forEach(swaggerResource -> {
                swaggerResource.setName(route.getUri().getHost().toLowerCase() + "-" + swaggerResource.getName());
                swaggerResource.setUrl("/" + route.getUri().getHost().toLowerCase() + swaggerResource.getUrl());
            });

            return swaggerResources;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
