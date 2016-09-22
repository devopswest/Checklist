package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ClientTagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientTag and its DTO ClientTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientTagMapper {

    @Mapping(source = "tagProperty.id", target = "tagPropertyId")
    @Mapping(source = "tagProperty.label", target = "tagPropertyLabel")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    ClientTagDTO clientTagToClientTagDTO(ClientTag clientTag);

    List<ClientTagDTO> clientTagsToClientTagDTOs(List<ClientTag> clientTags);

    @Mapping(source = "tagPropertyId", target = "tagProperty")
    @Mapping(source = "clientId", target = "client")
    ClientTag clientTagDTOToClientTag(ClientTagDTO clientTagDTO);

    List<ClientTag> clientTagDTOsToClientTags(List<ClientTagDTO> clientTagDTOs);

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
    }

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
