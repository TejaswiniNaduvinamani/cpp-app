package com.gfs.cpp.component.markup;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;

@Component
public class SubgroupResponseDTOBuilder {

    public SubgroupResponseDTO buildSubgroupResponseDTO(String subgroupId, String subgroupDescription, int validationCode, String validationMessage) {
        SubgroupResponseDTO subgroupResponseDTO = new SubgroupResponseDTO();
        subgroupResponseDTO.setSubgroupId(subgroupId);
        subgroupResponseDTO.setSubgroupDesc(subgroupDescription);
        subgroupResponseDTO.setValidationCode(validationCode);
        subgroupResponseDTO.setValidationMessage(validationMessage);
        return subgroupResponseDTO;
    }

}
