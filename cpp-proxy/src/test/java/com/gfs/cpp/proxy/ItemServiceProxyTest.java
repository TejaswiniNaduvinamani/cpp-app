package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceProxyTest {

    private static final String BASE_URL = "baseUrl/";
    private static final String PRODUCT_TYPE_URL = "api/itemConfiguration/offeringCategory?languageTypeCode=en";

    @InjectMocks
    @Spy
    private ItemServiceProxy target;

    @Mock
    private CppRestTemplate cppRestTemplate;

    @Mock
    Environment environment;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "itemServiceUrl", BASE_URL);
    }

    @Test
    public void fetchProductItemsTest() throws Exception {
        Map<Integer, ProductTypeDTO> expectedResponse = new HashMap<>();
        expectedResponse.put(1, new ProductTypeDTO());
        doReturn(expectedResponse).when(cppRestTemplate).postForObject(eq(BASE_URL + PRODUCT_TYPE_URL), eq(null), eq(Map.class));
        assertThat(target.fetchProductItems(), equalTo(expectedResponse));
        verify(cppRestTemplate).postForObject(eq(BASE_URL + PRODUCT_TYPE_URL), eq(null), eq(Map.class));

    }

    @Before
    public void before() throws IOException {
        when(environment.getProperty(CPPConstants.PRODUCTS_TO_EXCLUDE)).thenReturn("12");
    }

    @Test
    public void fetchProductType() {
        Map<Integer, ProductTypeDTO> expectedResponse = new HashMap<>();

        ProductTypeDTO prod = new ProductTypeDTO();
        prod.setOfferingTypeId(4);
        prod.setOfferingCategoryId(5);
        prod.setOfferingCategoryDescription("Test");
        List<ProductTypeDTO> prodList = new ArrayList<>();
        prodList.add(prod);
        expectedResponse.put(1, prod);

        doReturn(expectedResponse).when(target).fetchProductItems();

        assertThat(target.fetchProductType(), equalTo(prodList));

        verify(target).fetchProductItems();

    }

    @Test
    public void fetchProductTypeException() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(target).fetchProductItems();
        target.fetchProductType();
    }

    @Test
    public void fetchProductNameById() {
        Map<Integer, ProductTypeDTO> expectedResponse = new HashMap<>();

        ProductTypeDTO prod = new ProductTypeDTO();
        prod.setOfferingTypeId(1);
        prod.setOfferingCategoryId(1);
        prod.setOfferingCategoryDescription("Test");
        List<ProductTypeDTO> prodList = new ArrayList<>();
        prodList.add(prod);
        expectedResponse.put(1, prod);

        doReturn(expectedResponse).when(target).fetchProductItems();
        Map<Integer, String> offeringMap = new HashMap<>();
        offeringMap.put(1, "Test");

        assertThat(target.getAllProductTypesById(), equalTo(offeringMap));
        verify(target).fetchProductItems();

    }

}
