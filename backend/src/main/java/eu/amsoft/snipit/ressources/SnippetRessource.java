package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;

@RestController
@AllArgsConstructor
@RequestMapping(SNIPPET_RESSOURCE_BASE_URI)
public class SnippetRessource {

    private final SnippetService snippetService;

    @GetMapping
    public ResponseEntity<Page<SnippetDto>> getAllSnippets(
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "rows", defaultValue = "10") final int rows,
            @RequestParam(value = "sortBy", defaultValue = "id") final String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") final String sortOrder
    ) {
        final Pageable pageable = PageRequest.of(page, rows, Sort.by(Sort.Direction.valueOf(sortOrder), sortBy));
        return ResponseEntity.ok(snippetService.getAll(pageable));
    }
}
