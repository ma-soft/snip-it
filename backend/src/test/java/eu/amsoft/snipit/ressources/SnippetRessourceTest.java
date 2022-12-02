package eu.amsoft.snipit.ressources;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import eu.amsoft.snipit.services.SnippetService;
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

import static eu.amsoft.snipit.utils.TestUtils.getRandomSnippetDtoList;
import static eu.amsoft.snipit.utils.Uris.SNIPPET_RESSOURCE_BASE_URI;
import static java.lang.String.format;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@WebMvcTest(SnippetRessource.class)
class SnippetRessourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SnippetService snippetService;

    @Nested
    class GetAllSnippetsTest {
        @Test
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
                    .andExpect(jsonPath("$.numberOfElements", is(expectedResults.size())))
                    .andReturn().getResponse();

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
    class GetSnippetByIdTest {
        @Test
        void should_get_snippet_with_id_when_exists() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            given(snippetService.getById(anyString())).willReturn(SnippetDto.builder().id(id).build());

            // when
            final ResultActions response = mvc.perform(get(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id)));

            // then
            response
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", is(id)))
                    .andReturn();
        }

        @Test
        void should_get_404_when_id_not_exists() throws Exception {
            // given
            final String id = randomAlphanumeric(10);
            final int expectedHttpStatusCode = NOT_FOUND.value();
            final String expectedErrorMessage = format("%s {id: %s} not found.", SnippetModel.class.getName(), id);
            given(snippetService.getById(anyString())).willThrow(new EntityNotFoundException(SnippetModel.class, id));

            // when
            final ResultActions response = mvc.perform(get(format("%s/%s", SNIPPET_RESSOURCE_BASE_URI, id)));

            // then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.errorCode", is(expectedHttpStatusCode)))
                    .andExpect(jsonPath("$.errorMessage", is(expectedErrorMessage)))
                    .andReturn();
        }
    }
}