package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static eu.amsoft.snipit.utils.Uris.GET_SNIPPET_BY_ID;
import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;
import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RestController
@RequiredArgsConstructor
@RequestMapping(SNIPPET_RESSOURCE_BASE_URI)
public class SnippetRessource {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final SnippetService snippetService;

    @GetMapping
    @ApiOperation(value = "Récupère la collection de tous les snippets paginée")
    public ResponseEntity<Page<SnippetDto>> getAllSnippets(
            @ApiParam("Numéro de la page à laquelle on souhaite accéder")
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @ApiParam("Nombre de résultats par page")
            @RequestParam(value = "rows", defaultValue = "10") final int rows,
            @ApiParam("Nom du champs de l'objet utilisé pour le tri")
            @RequestParam(value = "sortBy", defaultValue = "id") final String sortBy,
            @ApiParam("Ordre de tri (ASC ou ascendant ou DESC pour descendant)")
            @RequestParam(value = "sortOrder", defaultValue = "ASC") final String sortOrder
    ) {
        final Pageable pageable = PageRequest.of(page, rows, by(Direction.valueOf(sortOrder), sortBy));
        return ResponseEntity.ok(snippetService.getAll(pageable));
    }

    @GetMapping(GET_SNIPPET_BY_ID)
    @ApiOperation(value = "Récupère un snippet grâce à son id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Aucun snippet n'a été trouvé portant l'identifiant fourni")
    })
    public ResponseEntity<SnippetDto> getSnippetById(
            @Parameter(name = "id", description = "Identifiant du snippet à rechercher")
            @PathVariable final String id
    ) throws EntityNotFoundException {
        return ResponseEntity.ok(snippetService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "Enregistre un snippet")
    public ResponseEntity<SnippetDto> createSnippet(@RequestBody final SnippetDto snippetDto) {
        final SnippetDto result = snippetService.createSnippet(snippetDto);
        final String itemUri = contextPath + format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, result.getId());
        final URI location = fromPath(itemUri).buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping(GET_SNIPPET_BY_ID)
    public ResponseEntity<?> deleteSnippet(@PathVariable final String id) throws EntityNotFoundException {
        snippetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
