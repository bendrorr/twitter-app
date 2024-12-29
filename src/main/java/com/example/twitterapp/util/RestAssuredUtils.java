package com.example.twitterapp.util;

import com.example.twitterapp.Exception.RestAssuredError;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestAssuredUtils {

    private RequestSpecification buildRequestSpecification(String path, Map<String, ?> queryParams, Map<String, ?> formParams, Map<String, ?> headers, Object body) {
        RequestSpecification requestSpecification = RestAssured.given().baseUri(path);

        if (queryParams != null && !queryParams.isEmpty()) {
            requestSpecification.queryParams(queryParams);
        }

        if (formParams != null && !formParams.isEmpty()) {
            requestSpecification.formParams(formParams);
        }

        if (headers != null && !headers.isEmpty()) {
            requestSpecification.headers(headers);
        }

        if (body != null) {
            requestSpecification.body(body);
        }

        return requestSpecification.log().all();
    }

    private Response executeRequest(String method, String path, Map<String, ?> queryParams, Map<String, ?> formParams, Map<String, ?> headers, Object body) {
        try {
            RequestSpecification specification = buildRequestSpecification(path, queryParams, formParams, headers, body);
            return (switch (method.toUpperCase()) {
                case "GET" -> specification.get();
                case "POST" -> specification.post();
                case "PUT" -> specification.put();
                case "PATCH" -> specification.patch();
                case "DELETE" -> specification.delete();
                default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
            }).prettyPeek();
        } catch (Exception e) {
            throw new RestAssuredError(method + " call fails", e);
        }
    }

    public Response get(String path, Map<String, ?> queryParams, Map<String, ?> headers) {
        return executeRequest("GET", path, queryParams, null, headers, null);
    }

    public Response post(String path, Map<String, ?> queryParams, Map<String, ?> formParams, Map<String, ?> headers, Object body) {
        return executeRequest("POST", path, queryParams, formParams, headers, body);
    }

    public Response put(String path, Map<String, ?> queryParams, Map<String, ?> formParams, Map<String, ?> headers, Object body) {
        return executeRequest("PUT", path, queryParams, formParams, headers, body);
    }

    public Response patch(String path, Map<String, ?> queryParams, Map<String, ?> formParams, Map<String, ?> headers, Object body) {
        return executeRequest("PATCH", path, queryParams, formParams, headers, body);
    }

    public Response delete(String path, Map<String, ?> queryParams, Map<String, ?> headers) {
        return executeRequest("DELETE", path, queryParams, null, headers, null);
    }
}
