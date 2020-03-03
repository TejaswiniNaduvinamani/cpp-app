package com.gfs.cpp.component.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.assignment.ItemAssignmentDuplicateValidator;
import com.gfs.cpp.proxy.ItemQueryProxy;

@Service
public class ItemService {

    @Autowired
    private ItemQueryProxy itemQueryProxy;

    @Autowired
    private ItemAssignmentDuplicateValidator itemAssignmentDuplicateValidator;

    public ItemInformationDTO findItemInformation(String itemId) {
        return itemQueryProxy.findItemInformation(itemId);
    }

    public ItemInformationDTO findItemInformation(String itemId, String cmgCustomerId, int cmgCustomerTypeCode, int contractPriceProfileSeq) {
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        try {
            itemAssignmentDuplicateValidator.validateOnFindItemInformation(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);
            itemInformationDTO = itemQueryProxy.findItemInformation(itemId);
        } catch (CPPRuntimeException cpp) {
            if (cpp.getType().equals(CPPExceptionType.ITEM_ALREADY_EXIST))
                itemInformationDTO.setIsItemAlreadyExist(true);
            else
                throw cpp;
        }
        return itemInformationDTO;
    }

}
