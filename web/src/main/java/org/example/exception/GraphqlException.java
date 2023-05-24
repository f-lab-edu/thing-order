package org.example.exception;

import java.util.Map;

public class GraphqlException extends AbstractGraphQLException {
    public GraphqlException(String message) {
        super(message);
    }

    public GraphqlException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
