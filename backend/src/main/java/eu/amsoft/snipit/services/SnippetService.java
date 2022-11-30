package eu.amsoft.snipit.services;

import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import static java.util.List.of;

@Service
public class SnippetService {

    public Page<SnippetDto> getAll() {
        return new PageImpl<>(
                of(SnippetDto.builder().build(), SnippetDto.builder().build())
        );
    }

}
