package eu.amsoft.snipit.repositories;

import eu.amsoft.snipit.models.SnippetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SnippetRepository extends MongoRepository<SnippetModel, String> {

    @Override
    Page<SnippetModel> findAll(Pageable pageRequest);

}
