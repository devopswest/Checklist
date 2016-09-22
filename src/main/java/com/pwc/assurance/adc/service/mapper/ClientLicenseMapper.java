package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.ClientLicenseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ClientLicense and its DTO ClientLicenseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientLicenseMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "clientLicenseType.id", target = "clientLicenseTypeId")
    @Mapping(source = "clientLicenseType.label", target = "clientLicenseTypeLabel")
    ClientLicenseDTO clientLicenseToClientLicenseDTO(ClientLicense clientLicense);

    List<ClientLicenseDTO> clientLicensesToClientLicenseDTOs(List<ClientLicense> clientLicenses);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "clientLicenseTypeId", target = "clientLicenseType")
    ClientLicense clientLicenseDTOToClientLicense(ClientLicenseDTO clientLicenseDTO);

    List<ClientLicense> clientLicenseDTOsToClientLicenses(List<ClientLicenseDTO> clientLicenseDTOs);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }

    default Taxonomy taxonomyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Taxonomy taxonomy = new Taxonomy();
        taxonomy.setId(id);
        return taxonomy;
    }
}
