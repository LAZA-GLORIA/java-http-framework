package com.framework.core;

import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String statusMessage;
    private final String body;
    private final Map<String, String> headers;

    public HttpResponse(int statusCode, String statusMessage, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body;
        this.headers = new LinkedHashMap<>();
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public byte[] toBytes() {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "text/plain; charset=UTF-8");
        }

        headers.put("Content-Length", String.valueOf(bodyBytes.length));

        StringBuilder responseBuilder = new StringBuilder();

        // ligne de statut
        responseBuilder.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(statusMessage)
                .append("\r\n");

        // headers
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\r\n");
        }

        // Ligne vide
        responseBuilder.append("\r\n");

        byte[] headerBytes = responseBuilder.toString().getBytes(StandardCharsets.UTF_8);

        byte[] fullResponse = new byte[headerBytes.length + bodyBytes.length];

        System.arraycopy(headerBytes, 0, fullResponse, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, fullResponse, headerBytes.length, bodyBytes.length);

        return fullResponse;
    }

    public static HttpResponse ok(String body) {
        HttpResponse response = new HttpResponse(200, "OK", body);
        response.addHeader("Content-Type", "text/plain; charset=UTF-8");
        return response;
    }

    public static HttpResponse notFound(String body) {
        HttpResponse response = new HttpResponse(404, "Not Found", body);
        response.addHeader("Content-Type", "text/plain; charset=UTF-8");
        return response;
    }
}

