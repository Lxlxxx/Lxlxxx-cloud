package com.example.gateway.handler;

import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @Author lixianglong
 * @create 2022/8/9 上午11:58
 */
public class SentinelFallbackHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        if (serverWebExchange.getResponse().isCommitted()){
            return Mono.error(throwable);
        }
        if (!BlockException.isBlockException(throwable)){
            return Mono.error(throwable);
        }

        return handleBlockedRequest(serverWebExchange,throwable).flatMap(serverResponse ->writeResponse(serverResponse,serverWebExchange) );
    }

    private Mono<? extends Void> writeResponse(ServerResponse response, ServerWebExchange serverWebExchange) {
        ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] datas = "{\"status\":429,\"message\":\"请求超过最大数，请稍后再试\"}".getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(datas);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange,Throwable throwable){

        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }


}
