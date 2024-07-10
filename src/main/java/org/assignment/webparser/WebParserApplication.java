package org.assignment.webparser;

import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class WebParserApplication {

    private static final AtomicInteger activeTasks = new AtomicInteger(0);

    public void parseUrl(String homeUrl) {

        PersistenceService persistenceService = new PersistenceService(homeUrl);
        PrintService printService = new PrintService(persistenceService);

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            while (true) {
                System.out.println("Number of items in Queue waiting to be parsed: " + persistenceService.queueSize());
                System.out.println("Number of url found by now: " + persistenceService.linksSize());
                System.out.println("-------");

                List<Callable<Void>> tasks = generateCallableTasks(homeUrl, persistenceService);

                if (!tasks.isEmpty()) {
                    activeTasks.addAndGet(tasks.size());
                    executorService.invokeAll(tasks);
                } else if (activeTasks.get() == 0 && persistenceService.isThereWaitingItems()) {
                    break;
                }
            }
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                System.out.println("System exited due to timeout");
            }

            printService.printUrls();

        } catch (InterruptedException e) {
            System.out.println("Failed to continue due to :" + e.getMessage());
        }
    }

    private List<Callable<Void>> generateCallableTasks(String homeUrl, PersistenceService persistenceService) {
        List<Callable<Void>> tasks = new ArrayList<>();

        Link link;
        while ((link = persistenceService.getItemFromQueue()) != null && tasks.size() < 10) {
            if (isUrlNotOnSameHost(link, homeUrl)) continue;
            final Link finalLink = link;
            tasks.add(() -> {
                try {
                    extractLinks(persistenceService, finalLink);
                } finally {
                    activeTasks.decrementAndGet();
                }
                return null;
            });
        }
        return tasks;
    }

    private void extractLinks(PersistenceService persistenceService, Link link) {
        HTMLEditorKit.ParserCallback callback = new LinkExtractorCallback(persistenceService);
        LinkService linkService = new LinkService(callback);
        try {
            linkService.extractAndStoreLinks(link.getUrl());
        } catch (IOException e) {
            System.out.println("Not possible to parse " + link.getUrl());
        }
    }

    private static boolean isUrlNotOnSameHost(Link link, String homeUrl) {
        return link.getUrl() == null || !link.getUrl().toLowerCase().contains(homeUrl);
    }

    public static void main(String[] args) {
        WebParserApplication parserApplication = new WebParserApplication();
        parserApplication.parseUrl("https://ecosio.com/");
    }

}
