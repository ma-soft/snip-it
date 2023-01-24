package eu.amsoft.snipit.services;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.mappers.SnippetMapper;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.repositories.SnippetRepository;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.ressources.dto.SnippetRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class SnippetService {

    private final SnippetRepository snippetRepository;

    public Page<SnippetDto> getAll(final Pageable pageable) {
        return snippetRepository.findAll(pageable)
                .map(SnippetMapper.INSTANCE::toSnippetDto);
    }

    public SnippetDto getById(final String id) throws EntityNotFoundException {
        return SnippetMapper.INSTANCE.toSnippetDto(
                // ne peut jamais être null car si n'existe pas, alors envoi d'exception
                getByIdOrElseThrow(id)
        );
    }

    public SnippetDto createSnippet(final SnippetRequest request) {
        final SnippetModel model = SnippetModel.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return SnippetMapper.INSTANCE.toSnippetDto(
                snippetRepository.save(model)
        );
    }

    public void deleteById(final String id) throws EntityNotFoundException {
        snippetRepository.deleteById(
                // ne peut jamais être null car si n'existe pas ou id null, alors envoi d'exception
                getByIdOrElseThrow(id).getId()
        );
    }

    public SnippetDto updateSnippet(final SnippetDto dto) throws EntityNotFoundException {
        final SnippetModel model = getByIdOrElseThrow(dto.getId());

        model.setTitle(dto.getTitle());
        model.setContent(dto.getContent());
        model.setUpdatedAt(now());

        return SnippetMapper.INSTANCE.toSnippetDto(
                snippetRepository.save(model)
        );
    }

    private SnippetModel getByIdOrElseThrow(final String id) throws EntityNotFoundException {
        return snippetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SnippetModel.class, id));
    }
}
