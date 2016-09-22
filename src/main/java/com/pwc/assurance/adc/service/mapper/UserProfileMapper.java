package com.pwc.assurance.adc.service.mapper;

import com.pwc.assurance.adc.domain.*;
import com.pwc.assurance.adc.service.dto.UserProfileDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UserProfile and its DTO UserProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface UserProfileMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    UserProfileDTO userProfileToUserProfileDTO(UserProfile userProfile);

    List<UserProfileDTO> userProfilesToUserProfileDTOs(List<UserProfile> userProfiles);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "clientId", target = "client")
    UserProfile userProfileDTOToUserProfile(UserProfileDTO userProfileDTO);

    List<UserProfile> userProfileDTOsToUserProfiles(List<UserProfileDTO> userProfileDTOs);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
