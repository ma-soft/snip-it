package eu.amsoft.snipit.utils;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class TestUtils {

    private TestUtils() {
        // empêche l'instanciation de la classe
    }

    public static SnippetDto getRandomSnippetDto() {
        return getRandomSnippetDto(true);
    }

    public static SnippetDto getRandomSnippetDto(final boolean withId) {
        return SnippetDto
                .builder()
                .id(withId ? randomAlphanumeric(10) : null)
                .title(randomAlphanumeric(20))
                .content(randomAlphanumeric(100))
                .build();
    }

    public static List<SnippetDto> getRandomSnippetDtoList(final int size) {
        final List<SnippetDto> dtos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            dtos.add(getRandomSnippetDto());
        }

        return dtos;
    }

    public static SnippetModel getRandomSnippetModel() {
        return getRandomSnippetModel(true);
    }

    public static SnippetModel getRandomSnippetModel(final boolean withId) {
        return SnippetModel
                .builder()
                .id(withId ? randomAlphanumeric(10) : null)
                .title(randomAlphanumeric(20))
                .content(randomAlphanumeric(100))
                .build();
    }

    public static List<SnippetModel> getRandomSnippetModelList(final int size) {
        final List<SnippetModel> models = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            models.add(getRandomSnippetModel());
        }

        return models;
    }

}
