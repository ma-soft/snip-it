package eu.amsoft.snipit.services;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.repositories.SnippetRepository;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static eu.amsoft.snipit.utils.TestUtils.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@ExtendWith(MockitoExtension.class)
@DisplayName("SnippetService")
class SnippetServiceTest {

    @Mock
    SnippetRepository snippetRepository;

    @InjectMocks
    SnippetService snippetService;

    @Nested
    @DisplayName("SnippetService#getAllSnippets")
    class GetAllSnippetTest {
        @Test
        @DisplayName("Une paginée de snippets doit être renvoyée")
        void should_list_all_snippets() {
            // given
            final Pageable pageable = PageRequest.of(0, 10);
            final List<SnippetModel> models = getRandomSnippetModelList(10);
            given(snippetRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(models));

            // when
            final Page<SnippetDto> result = snippetService.getAll(pageable);

            // then
            assertThat(result, notNullValue());
            assertThat(result.getSize(), is(models.size()));
        }
    }

    @Nested
    @DisplayName("SnippetService#getSnippetById")
    class GetSnippetByIdTest {

        @Test
        @DisplayName("Un snippet doit être retourné quand son identifiant existe")
        void should_get_snippet_when_id_exists() throws EntityNotFoundException {
            // given
            final SnippetModel model = getRandomSnippetModel();
            given(snippetRepository.findById(anyString())).willReturn(of(model));

            // when
            final SnippetDto dto = snippetService.getById(model.getId());

            // then
            assertThat(dto, notNullValue());
            assertThat(dto.getId(), is(model.getId()));
            assertThat(dto.getTitle(), is(model.getTitle()));
            assertThat(dto.getContent(), is(model.getContent()));
        }

        @Test
        @DisplayName("Une exception doit être lancée quand on recherche un identifiant qui n'existe pas")
        void should_throw_exception_when_snippet_not_found() {
            // given
            final String id = randomAlphanumeric(10);
            final EntityNotFoundException expectedException = new EntityNotFoundException(SnippetModel.class, id);
            given(snippetRepository.findById(id)).willReturn(empty());

            // when
            final EntityNotFoundException capturedException = assertThrows(EntityNotFoundException.class, () -> snippetService.getById(id));
            assertThat(capturedException.getMessage(), is(expectedException.getMessage()));
            assertThat(capturedException, isA(EntityNotFoundException.class));
        }
    }

    @Nested
    @DisplayName("SnippetService#createSnippet")
    class CreateSnippetTest {
        @Test
        @DisplayName("Un snippet doit être créé")
        void should_create_snippet() {
            // given
            final SnippetDto toSave = getRandomSnippetDto(false);
            final SnippetModel model = SnippetModel.builder()
                    .id(randomAlphanumeric(10))
                    .title(toSave.getTitle())
                    .content(toSave.getContent())
                    .build();
            given(snippetRepository.save(any(SnippetModel.class))).willReturn(model);

            // when
            final SnippetDto snippet = snippetService.createSnippet(toSave);

            // then
            assertThat(snippet, notNullValue());
            assertThat(snippet.getId(), notNullValue());
            assertThat(snippet.getTitle(), is(toSave.getTitle()));
            assertThat(snippet.getContent(), is(toSave.getContent()));
        }
    }

    @Nested
    @DisplayName("SnippetService#deleteSnippet")
    class DeleteSnippetTest {
        @Test
        @DisplayName("Un snippet doit être supprimé quand son identifiant existe")
        void should_delete_snippet_when_exists() throws EntityNotFoundException {
            // given
            final SnippetModel snippetModel = getRandomSnippetModel();
            given(snippetRepository.findById(anyString())).willReturn(of(snippetModel));

            // when
            snippetService.deleteById(snippetModel.getId());

            // then
            verify(snippetRepository, times(1)).deleteById(snippetModel.getId());
        }

        @Test
        @DisplayName("Une exception doit être lancée quand on tente de supprimer un snippet qui n'existe pas")
        void should_throw_exception_when_not_exists() {
            // given
            final String id = randomAlphanumeric(10);
            final EntityNotFoundException expectedException = new EntityNotFoundException(SnippetModel.class, id);
            given(snippetRepository.findById(anyString())).willReturn(empty());

            // when
            final EntityNotFoundException capturedException = assertThrows(EntityNotFoundException.class, () -> snippetService.getById(id));

            // then
            assertThat(capturedException.getMessage(), is(expectedException.getMessage()));
            assertThat(capturedException, isA(EntityNotFoundException.class));
        }
    }

    @Nested
    @DisplayName("SnippetService#updateSnippet")
    class UpdateSnippetTest {
        @Test
        @DisplayName("Un snippet xdto correspondant à celui en paramètres doit être retourné si le snippet existe")
        void should_return_same_dto_when_exists() throws EntityNotFoundException {
            // given
            final SnippetDto initialDto = getRandomSnippetDto();
            final SnippetModel model = SnippetModel.builder()
                    .id(initialDto.getId())
                    .content(initialDto.getContent())
                    .title(initialDto.getTitle())
                    .build();
            given(snippetRepository.existsById(anyString())).willReturn(true);
            given(snippetRepository.save(any(SnippetModel.class))).willReturn(model);

            // when
            final SnippetDto resultDto = snippetService.updateSnippet(initialDto);

            // then
            assertThat(resultDto, notNullValue());
            assertThat(resultDto.getId(), is(initialDto.getId()));
            assertThat(resultDto.getContent(), is(initialDto.getContent()));
            assertThat(resultDto.getTitle(), is(initialDto.getTitle()));

            verify(snippetRepository, times(1)).save(any(SnippetModel.class));
        }

        @Test
        @DisplayName("Une exception doit être lancée quand on tente de modifier un snippet qui n'existe pas")
        void should_throw_exception_when_snippet_not_exists() throws EntityNotFoundException {
            // given
            final SnippetDto dto = getRandomSnippetDto();
            final EntityNotFoundException expectedException = new EntityNotFoundException(SnippetModel.class, dto.getId());
            given(snippetRepository.existsById(anyString())).willReturn(false);

            // when
            final EntityNotFoundException capturedException = assertThrows(EntityNotFoundException.class, () -> snippetService.updateSnippet(dto));

            // then
            assertThat(capturedException.getMessage(), is(expectedException.getMessage()));
            assertThat(capturedException, isA(EntityNotFoundException.class));

        }

        @Test
        @DisplayName("Une exception doit être lancée quand on tente de modifier un snippet à l'aide d'un Id null")
        void should_throw_exception_when_id_is_null() throws EntityNotFoundException {
            // given
            final SnippetDto dto = getRandomSnippetDto(false);
            final EntityNotFoundException expectedException = new EntityNotFoundException(SnippetModel.class, dto.getId());

            // when
            final EntityNotFoundException capturedException = assertThrows(EntityNotFoundException.class, () -> snippetService.updateSnippet(dto));

            // then
            assertThat(capturedException.getMessage(), is(expectedException.getMessage()));
            assertThat(capturedException, isA(EntityNotFoundException.class));
        }
    }
}