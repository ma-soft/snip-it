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
        return snippetRepository.findAll(pageable)
                .map(SnippetMapper.INSTANCE::toSnippetDto);
    }

    public SnippetDto getById(final String id) throws EntityNotFoundException {
        return SnippetMapper.INSTANCE.toSnippetDto(
                // ne peut jamais être null car si n'existe pas, alors envoi d'exception
                getByIdOrElseThrow(id)
        );
    }

    public SnippetDto createSnippet(final SnippetDto dto) {
        final SnippetModel model = SnippetMapper.INSTANCE.toSnippetModel(dto);

        return SnippetMapper.INSTANCE.toSnippetDto(
                snippetRepository.save(model)
        );
    }

    public void deleteById(final String id) throws EntityNotFoundException {
        snippetRepository.deleteById(
                // ne peut jamais être null car si n'existe pas, alors envoi d'exception
                getByIdOrElseThrow(id).getId()
        );
    }

    public SnippetDto updateSnippet(final SnippetDto dto) throws EntityNotFoundException {
        final String id = dto.getId();
        if (id == null || !snippetRepository.existsById(id)) {
            throw new EntityNotFoundException(SnippetModel.class, id);
        }

        return SnippetMapper.INSTANCE.toSnippetDto(
                snippetRepository.save(SnippetMapper.INSTANCE.toSnippetModel(dto))
        );
    }

    private SnippetModel getByIdOrElseThrow(final String id) throws EntityNotFoundException {
        return snippetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SnippetModel.class, id));
    }
}
