package org.assignment.webparser;

import java.util.Comparator;
import java.util.List;

public class PrintService {

    private final PersistenceService persistenceService;

    public PrintService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void printUrls() {
        List<Link> sortedLinks = persistenceService.findAll().stream()
                .sorted(Comparator.comparing(Link::label))
                .toList();

        sortedLinks.forEach(link -> System.out.println("label: " + link.label() + " | url: " + link.url()));
        System.out.println("Total Number of Urls found:" + persistenceService.linksSize());
    }
}
