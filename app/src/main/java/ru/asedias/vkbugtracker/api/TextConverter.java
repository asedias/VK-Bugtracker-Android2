package ru.asedias.vkbugtracker.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import pl.droidsonroids.jspoon.ElementConverter;
import pl.droidsonroids.jspoon.annotation.Selector;

/**
 * Created by rorom on 15.12.2018.
 */

public class TextConverter implements ElementConverter<String> {
    @Override
    public String convert(Element node, Selector selector) {
        if(node != null)
        return Jsoup.clean(node.html(), "https://vk.com", new Whitelist().addTags("br").addAttributes("a","href"));
        return "123";
    }
}
