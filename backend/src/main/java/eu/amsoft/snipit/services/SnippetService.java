package eu.amsoft.snipit.services;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.mappers.SnippetMapper;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.repositories.SnippetRepository;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SnippetService {

    private final SnippetRepository snippetRepository;

    public Page<SnippetDto> getAll(final Pageable pageable) {
        final Page<SnippetModel> page = snippetRepository.findAll(pageable);
        return page.map(SnippetMapper.INSTANCE::toSnippetDto);
    }

    public SnippetDto getById(final String id) throws EntityNotFoundException {
        final SnippetModel snippetModel = snippetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SnippetModel.class, id));
        return SnippetMapper.INSTANCE.toSnippetDto(snippetModel);
    }
}
