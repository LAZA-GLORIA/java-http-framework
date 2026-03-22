package com.framework.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers; // Host: localhost, Accept: text/html...

    public HttpRequest(BufferedReader reader) throws IOException {

        // On lit la première ligne : "GET /users HTTP/1.1"
        String requestLine = reader.readLine();
        System.out.println("Requête brute : " + requestLine);

        // Séparer par espace
        String[] parts = requestLine.split(" ");
        this.method = parts[0]; // GET
        this.path = parts[1]; // /users

        // Lire les headers ligne par ligne jusqu'à la ligne vide
        this.headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }

    }
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}
