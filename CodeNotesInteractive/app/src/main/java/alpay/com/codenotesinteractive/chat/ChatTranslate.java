package alpay.com.codenotesinteractive.chat;

import android.os.AsyncTask;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * A snippet for Google Translation showing how to detect the language of some text and translate
 * some other text.
 */

public class ChatTranslate extends AsyncTask<String, String, String> {

    String reply = "";


    private static Translate translate;

    public String doInBackground(String... args) {
        translate = TranslateOptions.newBuilder().setApiKey("AIzaSyCz0uVFiGRbvTe8HA_J1lWBzLzKO83sI9o").
                build().
                getService();

        if (args[0].contains("detect")) {
            reply = detectLanguage(args[1]);
        }
        if (args[0].contains("tr")) {
            reply = translateFromTurkish(args[1]);
        }
        if (args[0].contains("en")) {
            reply = translateFromEnglish(args[1]);
        }

        return reply;
    }

    protected String onPostExecute() {
        return reply;
    }

    public static String detectLanguage(String mysteriousText) {
        // Detect the language of the mysterious text
        Detection detection = translate.detect(mysteriousText);
        String detectedLanguage = detection.getLanguage();
        return detectedLanguage;
    }

    public static String translateFromTurkish(String mysteriousText) {
        // Create a service object
        //
        // If no explicit credentials or API key are set, requests are authenticated using Application
        // Default Credentials if available; otherwise, using an API key from the GOOGLE_API_KEY
        // environment variable
        translate = TranslateOptions.getDefaultInstance().getService();
        // Translate the mysterious text to English
        Translation translation = translate.translate(
                mysteriousText,
                TranslateOption.sourceLanguage("tr"),
                TranslateOption.targetLanguage("en"));

        return translation.getTranslatedText();
    }

    public static String translateFromEnglish(String mysteriousText) {
        // Create a service object
        // If no explicit credentials or API key are set, requests are authenticated using Application
        // Default Credentials if available; otherwise, using an API key from the GOOGLE_API_KEY
        // environment variable
        translate = TranslateOptions.getDefaultInstance().getService();
        // Translate the mysterious text to English
        Translation translation = translate.translate(
                mysteriousText,
                TranslateOption.sourceLanguage("tr"),
                TranslateOption.targetLanguage("en"));

        return translation.getTranslatedText();
    }

}