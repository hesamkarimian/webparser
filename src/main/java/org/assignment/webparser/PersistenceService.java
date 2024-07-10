package org.assignment.webparser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PersistenceService {

    private final Set<Link> links;
    private final ConcurrentLinkedQueue<Link> linkQueue;

    public PersistenceService(String homeUrl) {
        this.links = ConcurrentHashMap.newKeySet();
        this.linkQueue = new ConcurrentLinkedQueue<>();
        save("Home", homeUrl);
    }

    public synchronized void save(String label, String url) {
        Link newLink = new Link(label, url);
        if (links.add(newLink)) {
            linkQueue.add(newLink);
        }
    }

    public Link getItemFromQueue() {
        return linkQueue.poll();
    }

    public int queueSize() {
        return linkQueue.size();
    }

    public int linksSize() {
        return links.size();
    }

    public boolean isThereWaitingItems() {
        return linkQueue.isEmpty();
    }

    public List<Link> findAll() {
        return new ArrayList<>(links);
    }

}
