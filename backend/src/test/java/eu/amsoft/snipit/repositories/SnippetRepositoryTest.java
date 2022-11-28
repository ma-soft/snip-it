package eu.amsoft.snipit.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@Testcontainers
class SnippetRepositoryTest {

    @Container
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:3.6.0");

    @Autowired
    private SnippetRepository repository;

    @DynamicPropertySource
    static void mongoProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getConnectionString);
    }

    @Test
    void test() {
        assertThat(repository.findAll(), is(empty()));
    }
}