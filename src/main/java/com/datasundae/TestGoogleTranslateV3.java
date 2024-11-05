package com.datasundae;

import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;

public class TestGoogleTranslateV3 {
    public static void main(String[] args) {
        String projectId = "112722748338234555475"; // replace with your Google Cloud Project ID
        String location = "global";
        String text = "Hallo, wie geht es Ihnen?";

        try {
            // Initialize the translation client with project settings
            TranslationServiceSettings settings = TranslationServiceSettings.newBuilder()
                    .setEndpoint(location + "-translation.googleapis.com:443")
                    .build();
            TranslationServiceClient client = TranslationServiceClient.create(settings);

            // Prepare translation request
            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent("projects/" + projectId + "/locations/" + location)
                    .setMimeType("text/plain")
                    .setTargetLanguageCode("en")
                    .addContents(text)
                    .build();

            // Perform translation
            TranslateTextResponse response = client.translateText(request);
            response.getTranslationsList().forEach(translation ->
                    System.out.println("Translated Text: " + translation.getTranslatedText())
            );
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}