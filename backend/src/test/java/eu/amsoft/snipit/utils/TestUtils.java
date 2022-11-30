package eu.amsoft.snipit.utils;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static List<SnippetDto> getRandomSnippetDtoList(final int size) {
        final List<SnippetDto> dtos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            dtos.add(
                    SnippetDto
                            .builder()
                            .id(UUID.randomUUID().toString())
                            .title(RandomStringUtils.randomAlphanumeric(10))
                            .content(RandomStringUtils.randomAlphanumeric(100))
                            .build()
            );
        }
        
        return dtos;
    }

    public static List<SnippetModel> getRandomSnippetModelList(final int size) {
        final List<SnippetModel> models = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            models.add(
                    SnippetModel
                            .builder()
                            .id(UUID.randomUUID().toString())
                            .title(RandomStringUtils.randomAlphanumeric(10))
                            .content(RandomStringUtils.randomAlphanumeric(100))
                            .build()
            );
        }

        return models;
    }

}
