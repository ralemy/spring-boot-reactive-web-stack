package com.curisprofound.spring.reactivestackdemo.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book,String> {
}
