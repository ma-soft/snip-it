package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static eu.amsoft.snipit.utils.Uris.GET_SNIPPET_BY_ID;
import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;
import static java.lang.String.format;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RestController
@RequiredArgsConstructor
@RequestMapping(SNIPPET_RESSOURCE_BASE_URI)
public class SnippetRessource {

    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    @GetMapping(GET_SNIPPET_BY_ID)
    public ResponseEntity<SnippetDto> getSnippetById(@PathVariable final String id) throws EntityNotFoundException {
        return ResponseEntity.ok(snippetService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SnippetDto> createSnippet(@RequestBody final SnippetDto snippetDto) {
        final SnippetDto result = snippetService.createSnippet(snippetDto);
        final String itemUri = contextPath + format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, result.getId());
        final URI location = fromPath(itemUri).buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }
}
