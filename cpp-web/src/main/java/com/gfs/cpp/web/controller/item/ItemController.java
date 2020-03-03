package com.gfs.cpp.web.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.component.item.ItemService;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    @Qualifier("itemService")
    ItemService itemService;

    @GetMapping(value = "/findItemInformation", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ItemInformationDTO> findItemInformation(@RequestParam String itemId, @RequestParam String cmgCustomerId,
            @RequestParam int cmgCustomerTypeCode, @RequestParam int contractPriceProfileSeq) {
        ItemInformationDTO itemInformationDTO = itemService.findItemInformation(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);
        return new ResponseEntity<>(itemInformationDTO, HttpStatus.OK);
    }

}
