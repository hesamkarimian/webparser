package org.assignment.webparser;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class LinkService {

    HTMLEditorKit.ParserCallback callback;

    public LinkService(HTMLEditorKit.ParserCallback callback) {
        this.callback = callback;
    }

    public void extractAndStoreLinks(String urlString) throws IOException {
        String htmlContent = getPageContent(urlString);
        Reader reader = new StringReader(htmlContent);
        ParserDelegator parserDelegator = new ParserDelegator();
        parserDelegator.parse(reader, callback, true);
    }

    private static String getPageContent(String urlString) throws IOException {
        PageReader pageReader = new PageReader();
        return pageReader.getPageContent(urlString);
    }

}
