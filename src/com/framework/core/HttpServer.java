package com.framework.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        // J'ouvre le port 8080 pour le serveur
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Serveur démarré sur http://localhost:8080");

        // Le serveur tourne
        while(true) {
            // On accepte la connexion au port
            Socket clientSocket = serverSocket.accept();
            System.out.println("-----------> Nouvelle connexion ");

            // On lit ligne par ligne ce qu'envoie le client et on parse les octets
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            // On lit la première ligne de la requete
            // String requestLine = reader.readLine();
            // System.out.println("Requête reçue : " + requestLine);

            // On ne lit plus la première ligne de la requete --> On délègue le parsing à HttpRequest
            HttpRequest request = new HttpRequest(reader);
            System.out.println("Méthode : " + request.getMethod());
            System.out.println("Path : " + request.getPath());
            System.out.println("Headers : " + request.getHeaders());

            // On construit la réponse HTTP :
            //    - ligne de statut (HTTP/1.1 200 OK)
            //    - headers (Content-Type, etc.)
            //    - ligne vide (\r\n)
            //    - body (le contenu)

            String body = "Hello I'm a framework!";

            String response =
                    "HTTP/1.1 200 OK\r\n" +                    // statut
                            "Content-Type: text/plain\r\n" +            // type de contenu
                            "Content-Length: " + body.length() + "\r\n" + // taille du body
                            "\r\n" +                                     // ligne vide = fin des headers
                            body;

            // On envoie la réponse au client
            // OutputStream est le tuyau de sortie vers le navigateur
            OutputStream output = clientSocket.getOutputStream();
            output.write(response.getBytes());
            output.flush();

            // On ferme la connexion avec ce client, la réponse reçue
            clientSocket.close();
        }

    }
    }
