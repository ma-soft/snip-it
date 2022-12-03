package eu.amsoft.snipit.mappers;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static eu.amsoft.snipit.utils.TestUtils.getRandomSnippetDto;
import static eu.amsoft.snipit.utils.TestUtils.getRandomSnippetModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SnippetMapperTest {

    @Nested
    class ToSnippetDto {
        @Test
        void should_map_to_dto() {
            // given
            final SnippetModel model = getRandomSnippetModel();

            //when
            final SnippetDto dto = SnippetMapper.INSTANCE.toSnippetDto(model);

            // then
            assertThat(dto, notNullValue());
            assertThat(dto.getId(), is(model.getId()));
            assertThat(dto.getTitle(), is(model.getTitle()));
            assertThat(dto.getContent(), is(model.getContent()));
        }

        @Test
        void should_get_null_when_dto_is_null() {
            //when
            final SnippetDto dto = SnippetMapper.INSTANCE.toSnippetDto(null);

            // then
            assertThat(dto, nullValue());
        }
    }

    @Nested
    class ToSnippetModel {
        @Test
        void should_map_to_model() {
            // given
            final SnippetDto dto = getRandomSnippetDto();

            // when
            final SnippetModel model = SnippetMapper.INSTANCE.toSnippetModel(dto);

            // then
            assertThat(model, notNullValue());
            assertThat(model.getId(), is(dto.getId()));
            assertThat(model.getTitle(), is(dto.getTitle()));
            assertThat(model.getContent(), is(dto.getContent()));
        }

        @Test
        void should_get_null_when_model_is_null() {
            // when
            final SnippetModel model = SnippetMapper.INSTANCE.toSnippetModel(null);

            // then
            assertThat(model, nullValue());
        }
    }
}