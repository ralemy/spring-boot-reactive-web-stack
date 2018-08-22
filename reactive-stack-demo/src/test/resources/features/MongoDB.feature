# Feature File for Cucumber Testing
# Created 8/7/18 8:19 PM by rezaalemy

Feature: I need reactive MongoDb tools
  To implement use cases that require streaming and non-blocking requests

  @ReactiveMongo
  Scenario: Should have a Book object marked as Document
    Given There exists a class named "Book" in "com.curisprofound.spring.reactivestackdemo.db" package
    And   The class has the following properties: "id, title, author, publisher"
    Then  The "id" field is annotated as "Id"
    And   The "id" field is of type "String"
    And   the "Document" annotation exists in the class annotations

  @ReactiveMongo
  Scenario: Should have a BookRepository Interface
    Given There exists a class named "BookRepository" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then  The class is an interface
    And   The class implements the "ReactiveMongoRepository<Book,String>" interface
    And   the class is autowired

  @ReactiveMongo
  Scenario: Should have MongoTemplate object autowired
    Given There exists a class named "ReactiveMongoTemplate" in "org.springframework.data.mongodb.core" package
    Then   the class is autowired

  @ReactiveMongo
  Scenario: Should be able to use repository to save a book
    Given I have instantiated book objects as:
      |id|title|author|publisher|
      |idOne|titleone|authorone|publisherone|
    When  I save the book with the bookRepository
    Then  It will be found in the database

  @ReactiveMongo
  Scenario: Should have cleared the database between test calls
    Given previous test has run
    Then  The database should not have a collection

  @ReactiveMongo
  Scenario: Should be able to update a book in repository
    Given  I have saved book objects as:
      |id|title|author|publisher|
      |idOne|titleone|authorone,phoneOne|publisherone|
    When I update the book by id "idOne" title to "titleOneUpdated"
    Then the book by id "idOne" will have title "titleOneUpdated"

  @ReactiveMongo
  Scenario: Should be able to delete a book from repository
    Given  I have saved book objects as:
      |id|title|author|publisher|
      |idOne|titleone|authorone,phoneOne|publisherone|
    And   the book by id "idOne" exists in the repository
    When  I delete book by id "idOne"
    Then  the book by id "idOne" does not exist in the repository

  @ReactiveMongo
  Scenario: Should hava author class
    Given There exists a class named "Author" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then  The class has the following properties: "name, phone"

  @ReactiveMongo
  Scenario: The Author class should be embedded in the Book Class
    Given There exists a class named "Book" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then   The "author" field is of type "Author"

  @ReactiveMongo
  Scenario: Should find author class inside book class
    Given I have saved book objects as:
      |id|title|author|publisher|
      |idOne|titleone|authorone,phoneOne|publisherone|
    Then  the book by Id "idOne" has an author embedded by name of "authorone"

  @ReactiveMongo
  Scenario: Should have a publisher class which is stored as document
    Given There exists a class named "Publisher" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then  The class has the following properties: "id, name, postalCode, bookIds"
    Then  The "id" field is annotated as "Id"
    And   The "id" field is of type "String"
    And   The "bookIds" field is of type "List<String>"
    And   the "Document" annotation exists in the class annotations

  @ReactiveMongo
  Scenario: Should have a PublisherRepository Interface
    Given There exists a class named "PublisherRepository" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then  The class is an interface
    And   The class implements the "ReactiveMongoRepository<Publisher,String>" interface
    And   the class is autowired

  @ReactiveMongo
  Scenario: Should have a reference to publisher in the book class
    Given There exists a class named "Book" in "com.curisprofound.spring.reactivestackdemo.db" package
    Then  The "publisher" field is annotated as "DBRef"
    And   The "publisher" field is of type "Publisher"

  @ReactiveMongo
  Scenario: Should access publisher from book
    Given I have saved book objects as:
      |id|title|author|publisher|
      |idOne|titleone|authorone,phoneOne|publisherone,postalCode1|
    Then  the book by Id "idOne" has a publisher by postalCode of "postalCode1"
