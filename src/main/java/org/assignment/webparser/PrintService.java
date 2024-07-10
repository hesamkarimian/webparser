package org.assignment.webparser;

public class PrintService {

    private final PersistenceService persistenceService;

    public PrintService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void printUrls() {
        persistenceService.findAll().forEach(link -> {
            System.out.println("label: " + link.getLabel() + " | url: " + link.getUrl());
        });
        System.out.println("Total Number of Urls found:" + persistenceService.linksSize());
    }
}
