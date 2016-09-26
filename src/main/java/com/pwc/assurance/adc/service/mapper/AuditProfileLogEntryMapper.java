package com.pwc.assurance.adc.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pwc.assurance.adc.domain.AuditProfile;
import com.pwc.assurance.adc.domain.AuditProfileLogEntry;
import com.pwc.assurance.adc.service.dto.AuditProfileLogEntryDTO;

/**
 * Mapper for the entity AuditProfileLogEntry and its DTO AuditProfileLogEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuditProfileLogEntryMapper {

   
	
    @Mapping(source = "auditProfileId", target = "auditProfile")
	@Mapping(target = "happened", ignore=true)
    @Mapping(target = "who", ignore= true)
    AuditProfileLogEntry auditProfileLongEntryDTOToAuditProfileLogEntry(AuditProfileLogEntryDTO auditProfileLogEntryDTO);

    List<AuditProfileLogEntry> auditProfileLongEntryDTOToAuditProfileLogEntrys(List<AuditProfileLogEntryDTO> auditProfileLogEntryDTOs);
    
    
    
    default AuditProfile auditProfileFromId(Long id) {
        if (id == null) {
            return null;
        }
        AuditProfile auditProfile = new AuditProfile();
        auditProfile.setId(id);
        return auditProfile;
    }

}
