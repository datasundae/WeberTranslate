import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FormattedPDFTranslator extends PDFTextStripper {

    private StringBuilder htmlContent = new StringBuilder();

    public FormattedPDFTranslator() throws IOException {
        // Start HTML document structure
        htmlContent.append("<html><head><title>Translated Document</title></head><body>");
    }

    // Override to capture text with font details for bold and italic
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            // Check if text is bold or italic based on font name
            boolean isBold = text.getFont().getName().toLowerCase().contains("bold");
            boolean isItalic = text.getFont().getName().toLowerCase().contains("italic");

            // Add formatting tags based on detected style
            if (isBold) htmlContent.append("<b>");
            if (isItalic) htmlContent.append("<i>");

            htmlContent.append(text.getUnicode()); // Add the actual text

            // Close tags in reverse order
            if (isItalic) htmlContent.append("</i>");
            if (isBold) htmlContent.append("</b>");
        }
        htmlContent.append("<br>"); // Line break after each text block
    }

    // Function to translate German text to English using Google Translate API
    public static String translateToEnglish(String germanText) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Translation translation = translate.translate(
                germanText,
                Translate.TranslateOption.sourceLanguage("de"),
                Translate.TranslateOption.targetLanguage("en")
        );

        return translation.getTranslatedText();
    }

    public static void main(String[] args) {
        String pdfPath = "/Users/datasundae/Documents/Wirtschaft16.pdf";
        String outputHtmlPath = "/Users/datasundae/Documents/WirtschaftTranslated.html";

        try (PDDocument document = PDDocument.load(new File(pdfPath));
             FileWriter htmlWriter = new FileWriter(outputHtmlPath)) {

            FormattedPDFTranslator pdfStripper = new FormattedPDFTranslator();
            pdfStripper.setSortByPosition(true);

            int pageCount = document.getNumberOfPages();
            for (int page = 1; page <= pageCount; page++) {
                pdfStripper.setStartPage(page);
                pdfStripper.setEndPage(page);

                System.out.println("Processing Page " + page);
                String germanText = pdfStripper.getText(document);

                // Add German text to HTML
                pdfStripper.htmlContent.append("<h2>Page ").append(page).append(" - German Text</h2>");
                pdfStripper.htmlContent.append(germanText.replaceAll("\n", "<br>"));

                // Translate and add English text to HTML
                String englishText = translateToEnglish(germanText);
                pdfStripper.htmlContent.append("<h2>Page ").append(page).append(" - English Translation</h2>");
                pdfStripper.htmlContent.append(englishText.replaceAll("\n", "<br>"));
                pdfStripper.htmlContent.append("<hr>"); // Add a separator between pages
            }

            // Close HTML structure
            pdfStripper.htmlContent.append("</body></html>");

            // Write the HTML content to a file
            htmlWriter.write(pdfStripper.htmlContent.toString());
            System.out.println("HTML file created at: " + outputHtmlPath);

        } catch (IOException e) {
            System.err.println("Error processing PDF file: " + e.getMessage());
        }
    }
}