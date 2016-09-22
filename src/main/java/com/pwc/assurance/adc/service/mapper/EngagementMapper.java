package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.EngagementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Engagement and its DTO EngagementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EngagementMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "workflow.id", target = "workflowId")
    @Mapping(source = "workflow.name", target = "workflowName")
    EngagementDTO engagementToEngagementDTO(Engagement engagement);

    List<EngagementDTO> engagementsToEngagementDTOs(List<Engagement> engagements);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "checklistTemplates", ignore = true)
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "workflowId", target = "workflow")
    Engagement engagementDTOToEngagement(EngagementDTO engagementDTO);

    List<Engagement> engagementDTOsToEngagements(List<EngagementDTO> engagementDTOs);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }

    default Workflow workflowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Workflow workflow = new Workflow();
        workflow.setId(id);
        return workflow;
    }
}
