package org.assignment.webparser;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class LinkExtractorCallback extends HTMLEditorKit.ParserCallback {

    private final PersistenceService persistenceService;

    private final StringBuilder currentText;
    private String currentHref;

    public LinkExtractorCallback(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.currentText = new StringBuilder();
    }

    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attrs, int pos) {
        if (tag == HTML.Tag.A) {
            currentHref = (String) attrs.getAttribute(HTML.Attribute.HREF);
            currentText.setLength(0);
        }
    }

    @Override
    public void handleText(char[] data, int pos) {
        if (currentHref != null) {
            currentText.append(data);
        }
    }

    @Override
    public void handleEndTag(HTML.Tag tag, int pos) {
        if (tag == HTML.Tag.A && currentHref != null) {
            if (currentHref.contains("http") || currentHref.contains("www")) {
                persistenceService.save(currentText.toString(), currentHref);
            }
            currentHref = null;
            currentText.setLength(0);
        }
    }

}
