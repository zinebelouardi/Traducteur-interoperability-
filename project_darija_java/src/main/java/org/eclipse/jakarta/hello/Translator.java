package org.eclipse.jakarta.hello;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Translator {
    private static final String GEMINI_API_KEY = "AIzaSyDf2Ur7WZK8BF4Z0ukhbu0QcJqHYuzpP54"; // Remplacez par votre clé API
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";

    public String translate(String text) {
        try {
            if (text == null || text.isEmpty()) {
                return "Erreur : Le texte à traduire est vide.";
            }

            // Préparer le corps de la requête
            JSONObject requestBody = new JSONObject();
            JSONArray partsArray = new JSONArray();
            JSONObject textObject = new JSONObject();
            textObject.put("text", "Traduire le texte suivant en darija : " + text);
            partsArray.put(textObject);

            JSONArray contentsArray = new JSONArray();
            JSONObject partsWrapper = new JSONObject();
            partsWrapper.put("parts", partsArray);
            contentsArray.put(partsWrapper);

            requestBody.put("contents", contentsArray);

            // Envoyer la requête à l'API
            URL url = new URL(GEMINI_API_URL + "?key=" + GEMINI_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.toString().getBytes("utf-8"));
            }

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return "Erreur : Impossible de traduire le texte (code HTTP " + statusCode + ").";
            }

            // Lire la réponse de l'API
            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            // Analyser la réponse JSON pour extraire uniquement la traduction
            JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
            JSONArray candidates = jsonResponse.optJSONArray("candidates");

            if (candidates != null && candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
                if (content != null) {
                    JSONArray parts = content.optJSONArray("parts");
                    if (parts != null && parts.length() > 0) {
                        // Extraire le texte de la traduction
                        String translatedText = parts.getJSONObject(0).optString("text", "Traduction introuvable.").trim();
                        return translatedText; // Retourner uniquement la traduction en texte brut
                    }
                }
            }

            return "Erreur : La traduction n'a pas pu être extraite de la réponse.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur : Une exception s'est produite lors de la traduction.";
        }
    }
}
