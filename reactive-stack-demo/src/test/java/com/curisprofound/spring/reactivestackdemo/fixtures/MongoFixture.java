package com.curisprofound.spring.reactivestackdemo.fixtures;

import com.curisprofound.spring.reactivestackdemo.db.*;
import cucumber.api.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MongoFixture {
    public static List<Book> tabletoBooks(DataTable table) {
        return table.asMaps(String.class, String.class)
                .stream()
                .map(MongoFixture::newBook)
                .collect(Collectors.toList());
    }

    public static Book newBook(Map<String, String> map) {
        Book b = new Book();
        b.setId(map.getOrDefault("id", ""));
        b.setTitle(map.getOrDefault("title", ""));
        b.setAuthor(newAuthor(map.getOrDefault("author", "")));
        b.setPublisher(newPublisher(b, map.getOrDefault("publisher", "")));
        return b;
    }

    private static Publisher newPublisher(Book b, String publisher) {
        String[] params = publisher.split(",");
        Publisher p = new Publisher();
        p.setId(b.getId() + "_publisher");
        if(params.length > 1)
            p.setPostalCode(params[1].trim());
        if(params.length>0)
            p.setName(params[0].trim());
        p.getBookIds().add(b.getId());
        return p;
    }

    private static Author newAuthor(String author) {
        String[] params = author.split(",");
        Author a = new Author();
        a.setName(params[0].trim());
        a.setPhone(params.length > 1 ? params[1].trim():"" );
        return a;
    }

    public static List<Book> saveBooks(List<Book> books, BookRepository repository, PublisherRepository publisherRepository) {
        return books.stream()
                .map(repository::save)
                .map(Mono::block)
                .peek(book -> publisherRepository.save(book.getPublisher()).then().block())
                .collect(Collectors.toList());
    }

    public static List<Book> saveBooks(DataTable table, BookRepository repository, PublisherRepository publisherRepository) {
        return saveBooks(tabletoBooks(table), repository, publisherRepository);
    }
}

