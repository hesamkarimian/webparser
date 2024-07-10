package org.assignment.webparser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class PageReader {

    public String getPageContent(String urlString) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString);
        return getPageContext(connection);
    }

    private static String getPageContext(HttpURLConnection connection) throws IOException {
        StringBuilder htmlContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            htmlContent.append(line).append("\n");
        }
        reader.close();
        return htmlContent.toString();
    }

    private static HttpURLConnection getHttpURLConnection(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection;
        if (urlString.startsWith("https")) {
            connection = (HttpsURLConnection) url.openConnection();
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestMethod("GET");
        return connection;
    }

}
