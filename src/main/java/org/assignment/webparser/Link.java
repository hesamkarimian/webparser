package org.assignment.webparser;

import java.util.Objects;

public record Link(String label, String url) {

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Link link = (Link) object;
        return Objects.equals(label.toLowerCase(), link.label.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(label.toLowerCase());
    }
}
