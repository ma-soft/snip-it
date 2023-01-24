package eu.amsoft.snipit.mappers;

import eu.amsoft.snipit.models.SnippetModel;
import eu.amsoft.snipit.ressources.dto.SnippetDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface SnippetMapper {

    SnippetMapper INSTANCE = Mappers.getMapper(SnippetMapper.class);

    SnippetDto toSnippetDto(final SnippetModel model);

    SnippetModel toSnippetModel(final SnippetDto dto);
}
