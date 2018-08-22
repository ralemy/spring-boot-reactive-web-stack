package com.curisprofound.spring.reactivestackdemo.cucumber;

import com.curisprofound.spring.reactivestackdemo.StepsBase;
import com.curisprofound.spring.reactivestackdemo.db.Book;
import com.curisprofound.spring.reactivestackdemo.db.BookRepository;
import com.curisprofound.spring.reactivestackdemo.db.PublisherRepository;
import com.curisprofound.spring.reactivestackdemo.fixtures.MongoFixture;
import com.curisprofound.test.assertions.*;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MongoDBSteps extends StepsBase {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private PublisherRepository publisherRepository;

    private static boolean databaseFilled = false;

    @After("@ReactiveMongo")
    public void afterReactiveMongo(){
        bookRepository.deleteAll().block();
    }

    @Given("^There exists a class named \"([^\"]*)\" in \"([^\"]*)\" package$")
    public void thereExistsAClassNamedInPackage(String className, String packageName) throws Throwable {
       AddClassAssertions(
               AssertOnClass
                       .For(className,packageName)
                       .exists());
    }

    @And("^The class has the following properties: \"([^\"]*)\"$")
    public void theClassHasTheFollowingProperties(String propertyList) throws Throwable {
        ClassAssertions target = GetClassAssertions();
        Arrays.stream(propertyList.split(","))
                .map(String::trim)
                .forEach(f -> target.Field(f).exists());
    }

    @Then("^The \"([^\"]*)\" field is annotated as \"([^\"]*)\"$")
    public void theFieldIsAnnotatedAs(String fieldName, String annotation) throws Throwable {
        GetClassAssertions().Field(fieldName).Annotation(annotation).exists();
    }

    @And("^The \"([^\"]*)\" field is of type \"([^\"]*)\"$")
    public void theFieldIsOfType(String fieldName, String typeName) throws Throwable {
        GetClassAssertions().Field(fieldName).isOfType(TypeDef.parse(typeName).get(0));
    }

    @And("^the \"([^\"]*)\" annotation exists in the class annotations$")
    public void theAnnotationExistsInTheClassAnnotations(String annotation) throws Throwable {
        GetClassAssertions().getAnnotation(annotation).exists();
    }

    @Then("^The class is an interface$")
    public void theClassIsAndInterface() throws Throwable {
        GetClassAssertions().isInterface();
    }

    @And("^The class implements the \"([^\"]*)\" interface$")
    public void theClassImplementsTheInterface(String signature) throws Throwable {
        GetClassAssertions().implementsInterface(TypeDef.parse(signature).get(0));
    }

    @And("^the class is autowired$")
    public void theClassIsAutowired() throws Throwable {
        Optional<FieldAssertions> target = AssertOnClass.For(this.getClass()).getFields()
                .filter(f -> f.isSubtypeOf(GetClassAssertions().classType()))
                .findAny();
        assertTrue("No field of type " + GetClassAssertions().classType(), target.isPresent());
        assertNotNull(" field " + target.get().fieldName() + " is null", target.get().getValue(this));
    }

    @Given("^I have instantiated book objects as:$")
    public void iHaveInstantiatedBookObjectsAs(DataTable table) throws Throwable {
        Add(List.class, MongoFixture.tabletoBooks(table),"bookList");
    }

    @Then("^It will be found in the database$")
    public void itWillBeFoundInTheDatabase() throws Throwable {
        List<?> books = AssertOnDb
                .ForMongo(mongoTemplate)
                .collectionExists(Book.class)
                .getAll()
                .buffer()
                .blockLast();

        assertEquals(
                Get(List.class, "bookList"),
                books
        );
        databaseFilled = true;
    }

    @When("^I save the book with the bookRepository$")
    public void iSaveTheBookWithTheBookRepository() throws Throwable {
        List<Book> books = Get(List.class, "bookList");
        MongoFixture.saveBooks(books, bookRepository, publisherRepository);
    }

    @Given("^I have saved book objects as:$")
    public void iHaveSavedBookObjectsAs(DataTable table) throws Throwable {
        MongoFixture.saveBooks(table, bookRepository, publisherRepository);
    }

    @When("^I update the book by id \"([^\"]*)\" title to \"([^\"]*)\"$")
    public void iUpdateTheBookByIdTitleTo(String bookId, String newTitle) throws Throwable {
        bookRepository.findById(bookId)
                .doOnNext(b-> b.setTitle(newTitle))
                .flatMap(b-> bookRepository.save(b))
                .block();
    }

    @Then("^the book by id \"([^\"]*)\" will have title \"([^\"]*)\"$")
    public void theBookByIdWillHaveTitle(String bookId, String expected) throws Throwable {
        bookRepository.findById(bookId)
                .doOnNext(b-> assertEquals(expected, b.getTitle()))
                .block();
    }

    @And("^the book by id \"([^\"]*)\" exists in the repository$")
    public void theBookByIdExistsInTheRepository(String bookId) throws Throwable {
        Book b = bookRepository.findById(bookId).block();
        assertNotNull(
                "book by id " + bookId + " not found in repo",
                b
        );
    }

    @When("^I delete book by id \"([^\"]*)\"$")
    public void iDeleteBookById(String bookId) throws Throwable {
        bookRepository.deleteById(bookId).block();
    }

    @Then("^the book by id \"([^\"]*)\" does not exist in the repository$")
    public void theBookByIdDoesNotExistInTheRepository(String bookId) throws Throwable {
        Book b = bookRepository.findById(bookId).block();
        assertNull(
                "book by id " + bookId + " was found in repo",
                b
        );
    }

    @Given("^previous test has run$")
    public void previousTestHasRun() throws Throwable {
        assertTrue(
                "previous scenario did not pass",
                databaseFilled
        );
    }

    @Then("^The database should not have a collection$")
    public void theDatabaseShouldNotHaveACollection() throws Throwable {
        List<?> books = AssertOnDb
                .ForMongo(mongoTemplate)
                .collectionExists(Book.class)
                .getAll()
                .buffer()
                .blockLast();
        assertNull(
                " There are books in database",
                books
        );
    }

    @Then("^the book by Id \"([^\"]*)\" has an author embedded by name of \"([^\"]*)\"$")
    public void theBookByIdHasAnAuthorEmbeddedByNameOf(String bookId, String authorName) throws Throwable {
        Book b = bookRepository.findById(bookId).block();
        assertEquals(
                authorName,
                b.getAuthor().getName()
        );
    }

    @Then("^the book by Id \"([^\"]*)\" has a publisher by postalCode of \"([^\"]*)\"$")
    public void theBookByIdHasAPublisherByPostalCodeOf(String bookId, String expected) throws Throwable {
        Book b = bookRepository.findById(bookId).block();
        assertEquals(
                expected,
                b.getPublisher().getPostalCode()
        );
    }
}
