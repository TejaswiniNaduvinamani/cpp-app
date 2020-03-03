package com.gfs.cpp.web.controller.item;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.component.item.ItemService;
import com.gfs.cpp.web.controller.item.ItemController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemControllerTest extends BaseTestClass {

    public static final String FIND_ITEM_INFORMATION_URL = "/item/findItemInformation?itemId=10001&cmgCustomerId=1&cmgCustomerTypeCode=31&contractPriceProfileSeq=1";

    @InjectMocks
    private ItemController target;

    @Mock
    private ItemService itemService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void shouldFindItemInformation() throws Exception {
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setIsValid(true);
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setItemNo("10001");
        itemInformationDTO.setStockingCode("3");
        itemInformationDTO.setItemStatusCode("AC");
        doReturn(itemInformationDTO).when(itemService).findItemInformation("10001", "1", 31, 1);

        ResultActions result = mockMvc.perform(get(FIND_ITEM_INFORMATION_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        ItemInformationDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), ItemInformationDTO.class);
        assertThat(resultsobj.getIsActive(), equalTo(true));
        assertThat(resultsobj.getIsValid(), equalTo(true));
        assertThat(resultsobj.getStockingCode(), equalTo("3"));
        assertThat(resultsobj.getItemNo(), equalTo("10001"));
        assertThat(resultsobj.getItemStatusCode(), equalTo("AC"));

        verify(itemService).findItemInformation("10001", "1", 31, 1);
    }

}
