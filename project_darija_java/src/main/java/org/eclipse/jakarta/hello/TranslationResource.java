package org.eclipse.jakarta.hello;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

@Path("translate")
public class TranslationResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON) // Indique que la réponse est en JSON
    public Response translate(@QueryParam("inputText") String inputText) {
        // Vérifier si le paramètre est nul ou vide
        if (inputText == null || inputText.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"Input text is required.\"}")
                           .build();
        }

        System.out.println("\nReceived text from body: " + inputText + "\n");

        // Appel à la classe Translator pour effectuer la traduction
        Translator translator = new Translator();
        String translatedText;

        try {
            translatedText = translator.translate(inputText);
        } catch (Exception e) {
            System.err.println("Function failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\": \"Translation failed: " + e.getMessage() + "\"}")
                           .build();
        }

        // Préparer la réponse JSON
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("translatedText", translatedText);

        return Response.ok(jsonResponse.toString(), MediaType.APPLICATION_JSON).build();
    }
}
