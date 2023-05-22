package org.example.interceptor;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RequestExceptionInterceptor implements WebGraphQlInterceptor {
    private static Map<String, Object> getCommonResponseExceptionMap(String path, Map<String, Object> dataFields) {
        Map<String, Object> result = new HashMap<>();
        result.put(path, dataFields);
        return result;
    }

    private static Map<String, Object> getCommonMapForDataMapWhenExceptionInvoked() {
        Map<String, Object> dataFields = new HashMap<>();
        dataFields.put("ok", false);
        dataFields.put("error", null);
        dataFields.put("results", null);
        return dataFields;
    }

    private static String getPathInResponse(WebGraphQlResponse response) {
        return (String) response.getExecutionResult().getErrors().get(0).getPath().get(0);
    }

    @Override
    @NonNull
    public Mono<WebGraphQlResponse> intercept(@NonNull WebGraphQlRequest request, Chain chain) {
        return chain.next(request).map(response -> {
            if (response.isValid()) {
                return response;
            }

            return response.transform(builder -> builder.data(createErrorData(response)).build());
        });
    }

    private Map<String, Object> createErrorData(WebGraphQlResponse response) {
        String path = getPathInResponse(response);
        Map<String, Object> commonResponseDataFieldsWhenExceptionInvoked = getCommonMapForDataMapWhenExceptionInvoked();
        return getCommonResponseExceptionMap(path, commonResponseDataFieldsWhenExceptionInvoked);
    }
}
