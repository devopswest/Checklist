package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.AuditProfileDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AuditProfile and its DTO AuditProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {AuditQuestionResponseMapper.class, })
public interface AuditProfileMapper {

    @Mapping(source = "engagement.id", target = "engagementId")
    @Mapping(source = "engagement.description", target = "engagementDescription")
    @Mapping(target = "responseFileId", ignore = true)
    AuditProfileDTO auditProfileToAuditProfileDTO(AuditProfile auditProfile);

    List<AuditProfileDTO> auditProfilesToAuditProfileDTOs(List<AuditProfile> auditProfiles);

    @Mapping(target = "logs", ignore = true)
    @Mapping(source = "engagementId", target = "engagement")
    AuditProfile auditProfileDTOToAuditProfile(AuditProfileDTO auditProfileDTO);

    List<AuditProfile> auditProfileDTOsToAuditProfiles(List<AuditProfileDTO> auditProfileDTOs);

    default Engagement engagementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Engagement engagement = new Engagement();
        engagement.setId(id);
        return engagement;
    }

    default AuditQuestionResponse auditQuestionResponseFromId(Long id) {
        if (id == null) {
            return null;
        }
        AuditQuestionResponse auditQuestionResponse = new AuditQuestionResponse();
        auditQuestionResponse.setId(id);
        return auditQuestionResponse;
    }
}
