package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.ressources.dto.SnippetRequest;
import eu.amsoft.snipit.services.SnippetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static eu.amsoft.snipit.utils.JsonMapper.toJson;
import static eu.amsoft.snipit.utils.TestUtils.*;
import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebMvcTest(SnippetRessource.class)
@DisplayName("SnippetRessource")
class SnippetRessourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SnippetService snippetService;

    @Nested
    @DisplayName("SnippetRessource#getAllSnippets")
    class GetAllSnippetsTest {
        @Test
        @DisplayName("Une liste paginée doit être transmise, avec un code HTTP 200")
        void should_get_pagination_of_snippets() throws Exception {
            // given
            final List<SnippetDto> expectedResults = getRandomSnippetDtoList(50);
            given(snippetService.getAll(any(Pageable.class))).willReturn(new PageImpl<>(expectedResults));

            // when
            final ResultActions response = mvc.perform(get(SNIPPET_RESSOURCE_BASE_URI));

            // then
            response
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.numberOfElements", is(expectedResults.size())));

            for (int i = 0; i < expectedResults.size(); i++) {
                final String contentPath = format("$.content[%d]", i);
                // on vérifie, champs par champs, que les objets reçus sont identiques à ceux attendus
                response
                        .andExpect(jsonPath(contentPath + ".id", is(expectedResults.get(i).getId())))
                        .andExpect(jsonPath(contentPath + ".title", is(expectedResults.get(i).getTitle())))
                        .andExpect(jsonPath(contentPath + ".content", is(expectedResults.get(i).getContent())));
            }

            verify(snippetService, times(1)).getAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Une liste vide paginée doit être transmise si aucun snippet n'est encore stocké, avec un code HTTP 200")
        void should_get_empty_when_no_snippet() throws Exception {
            // given
            given(snippetService.getAll(any(Pageable.class))).willReturn(Page.empty());

            // when
            final ResultActions response = mvc.perform(get(SNIPPET_RESSOURCE_BASE_URI));

            // then
            response
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.numberOfElements", is(0)))
                    .andExpect(jsonPath("$.content", emptyCollectionOf(SnippetDto.class)))
                    .andReturn();

            verify(snippetService, times(1)).getAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("SnippetRessource#getSnippetById")
    class GetSnippetByIdTest {
        @Test
        @DisplayName("Un snippet doit être transmis s'il existe, avec un code HTTP 200")
        void should_get_snippet_with_id_when_exists() throws Exception {
            // given
            final SnippetDto expectedSnippetDto = getRandomSnippetDto();
            given(snippetService.getById(anyString())).willReturn(expectedSnippetDto);

            // when
            final ResultActions response = mvc.perform(
                    get(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, expectedSnippetDto.getId()))
            );

            // then
            response
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", is(expectedSnippetDto.getId())))
                    .andExpect(jsonPath("$.title", is(expectedSnippetDto.getTitle())))
                    .andExpect(jsonPath("$.content", is(expectedSnippetDto.getContent())));

            verify(snippetService, times(1)).getById(anyString());
        }

        @Test
        @DisplayName("Une ExceptionResponse doit être transmise, avec un code retour 404")
        void should_get_404_when_id_not_exists() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            final EntityNotFoundException entityNotFoundException = new EntityNotFoundException(SnippetModel.class, id);
            given(snippetService.getById(anyString())).willThrow(entityNotFoundException);

            // when
            final ResultActions response = mvc.perform(get(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id)));

            // then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.errorCode", is(NOT_FOUND.value())))
                    .andExpect(jsonPath("$.errorMessage", is(entityNotFoundException.getMessage())))
                    .andReturn();

            verify(snippetService, times(1)).getById(anyString());
        }
    }

    @Nested
    @DisplayName("SnippetRessource#createSnippet")
    class CreateSnippetTest {
        @Test
        @DisplayName("Un snippet doit être transmis, avec un code retour 200")
        void should_get_created_snippet_with_id() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            final String locationExpected = format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id);
            final SnippetRequest request = getRandomSnippetRequest();
            final SnippetDto snippetDto = SnippetDto.builder()
                    .id(id)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .createdAt(now())
                    .build();
            given(snippetService.createSnippet(any(SnippetRequest.class))).willReturn(snippetDto);

            // when
            final ResultActions response = mvc.perform(
                    post(SNIPPET_RESSOURCE_BASE_URI).content(toJson(request)).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
            );

            // then
            response
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.title", equalTo(request.getTitle())))
                    .andExpect(jsonPath("$.createdAt", equalTo(snippetDto.getCreatedAt().toString())))
                    .andExpect(jsonPath("$.updatedAt", nullValue()))
                    .andExpect(jsonPath("$.content", equalTo(request.getContent())))
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", equalTo(locationExpected)));

            verify(snippetService, times(1)).createSnippet(any(SnippetRequest.class));
        }
    }

    @Nested
    @DisplayName("SnippetRessource#deleteSnippet")
    class DeleteSnippetTest {
        @Test
        @DisplayName("Un code retour 204 doit être transmis si le snippet existe")
        void should_delete_snippet_when_exists() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            willDoNothing().given(snippetService).deleteById(id);

            // when
            final ResultActions response = mvc.perform(
                    delete(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id))
            );

            // then
            response.andExpect(status().isNoContent());

            verify(snippetService, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Une ExceptionResponse doit être transmise avec un code 404 si le snippet n'existe pas")
        void should_get_404_when_not_exists() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            final EntityNotFoundException entityNotFoundException = new EntityNotFoundException(SnippetModel.class, id);
            willThrow(entityNotFoundException).given(snippetService).deleteById(anyString());

            // when
            final ResultActions response = mvc.perform(
                    delete(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id))
            );

            // then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.errorCode", is(NOT_FOUND.value())))
                    .andExpect(jsonPath("$.errorMessage", is(entityNotFoundException.getMessage())))
                    .andReturn();

            verify(snippetService, times(1)).deleteById(id);
        }
    }

    @Nested
    @DisplayName("SnippetRessource#updateSnippet")
    class UpdateSnippetTest {
        @Test
        @DisplayName("Un snippet équivalent à celui envoyé doit être transmis, avec un code HTTP 200")
        void should_update_snippet_if_exists() throws Exception {
            // given
            final SnippetDto dto = getRandomSnippetDto();
            given(snippetService.updateSnippet(any(SnippetDto.class))).willReturn(dto);

            // when
            final ResultActions response = mvc.perform(
                    put(SNIPPET_RESSOURCE_BASE_URI).content(toJson(dto)).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
            );

            // then
            response
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(dto.getId())))
                    .andExpect(jsonPath("$.title", equalTo(dto.getTitle())))
                    .andExpect(jsonPath("$.content", equalTo(dto.getContent())));

            verify(snippetService, times(1)).updateSnippet(any(SnippetDto.class));
        }

        @Test
        @DisplayName("Une ExceptionResponse doit être transmise, avec un code retour 404 si le snippet n'existe pas")
        void should_get_404_when_snippet_not_exists() throws Exception {
            // given
            final SnippetDto dto = getRandomSnippetDto();
            final EntityNotFoundException entityNotFoundException = new EntityNotFoundException(SnippetModel.class, dto.getId());
            given(snippetService.updateSnippet(any(SnippetDto.class))).willThrow(entityNotFoundException);

            // when
            final ResultActions response = mvc.perform(
                    put(SNIPPET_RESSOURCE_BASE_URI).content(toJson(dto)).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
            );

            // then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.errorCode", is(NOT_FOUND.value())))
                    .andExpect(jsonPath("$.errorMessage", is(entityNotFoundException.getMessage())))
                    .andReturn();

            verify(snippetService, times(1)).updateSnippet(any(SnippetDto.class));
        }
    }
}