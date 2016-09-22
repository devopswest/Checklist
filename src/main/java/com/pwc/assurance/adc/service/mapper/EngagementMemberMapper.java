package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.EngagementMemberDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EngagementMember and its DTO EngagementMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface EngagementMemberMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "engagement.id", target = "engagementId")
    @Mapping(source = "engagement.description", target = "engagementDescription")
    EngagementMemberDTO engagementMemberToEngagementMemberDTO(EngagementMember engagementMember);

    List<EngagementMemberDTO> engagementMembersToEngagementMemberDTOs(List<EngagementMember> engagementMembers);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "engagementId", target = "engagement")
    EngagementMember engagementMemberDTOToEngagementMember(EngagementMemberDTO engagementMemberDTO);

    List<EngagementMember> engagementMemberDTOsToEngagementMembers(List<EngagementMemberDTO> engagementMemberDTOs);

    default Engagement engagementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Engagement engagement = new Engagement();
        engagement.setId(id);
        return engagement;
    }
}
