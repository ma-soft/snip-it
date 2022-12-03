package eu.amsoft.snipit.repositories;

import eu.amsoft.snipit.models.SnippetModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SnippetRepository extends MongoRepository<SnippetModel, String> {
}
