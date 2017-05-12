package com.learning.estimator.persistenceservice.utils;

import org.springframework.hateoas.Link;

/**
 * Workaround for brackets and slash encoding within HATEOAS Links
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class BracketsLink extends Link {
    public BracketsLink(Link link) {
        super("http://" + link.getHref().replaceAll("%7B", "{").replaceAll("%7D", "}").replaceAll("\\?", "/\\?").replaceAll("//", "/"), link.getRel());
    }
}
