package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@Component
public class ItemServiceProxyMocker implements Resettable {

    private static final String PRODUCT_TYPE_URL = "api/itemConfiguration/offeringCategory?languageTypeCode=en";
    private static final String PRODUCT_SERVICE_HOST = "https://sititemservice.gfs.com/itemService/";

    @Autowired
    private CppRestTemplate cppRestTemplate;

    @Override
    public void reset() {
        when(cppRestTemplate.postForObject(eq(PRODUCT_SERVICE_HOST + PRODUCT_TYPE_URL), anyObject(), eq(Map.class)))
                .then(new Answer<Map<Integer, ProductTypeDTO>>() {

                    @Override
                    public Map<Integer, ProductTypeDTO> answer(InvocationOnMock invocation) throws Throwable {
                        Map<Integer, ProductTypeDTO> productTypeDTOMap = new HashMap<Integer, ProductTypeDTO>();
                        ProductTypeDTO productOne = new ProductTypeDTO();
                        ProductTypeDTO productTwo = new ProductTypeDTO();
                        ProductTypeDTO productThree = new ProductTypeDTO();
                        ProductTypeDTO productFour = new ProductTypeDTO();
                        
                        productOne.setOfferingTypeId(CukesConstants.PRODUCT_PRICE_ID);
                        productOne.setOfferingCategoryDescription(CukesConstants.PRODUCT_TYPE);
                        productOne.setOfferingCategoryId(CukesConstants.PRODUCT_PRICE_ID);
                        productOne.setOfferingCategoryDisplaySequence(CukesConstants.PRODUCT_PRICE_ID);
                        
                        productTwo.setOfferingTypeId(2);
                        productTwo.setOfferingCategoryDescription(CukesConstants.PRODUCT_TYPE);
                        productTwo.setOfferingCategoryId(2);
                        productTwo.setOfferingCategoryDisplaySequence(CukesConstants.PRODUCT_PRICE_ID);
                        
                        productThree.setOfferingTypeId(3);
                        productThree.setOfferingCategoryDescription(CukesConstants.PRODUCT_TYPE);
                        productThree.setOfferingCategoryId(3);
                        productThree.setOfferingCategoryDisplaySequence(CukesConstants.PRODUCT_PRICE_ID);
                        
                        productFour.setOfferingTypeId(4);
                        productFour.setOfferingCategoryDescription(CukesConstants.PRODUCT_TYPE);
                        productFour.setOfferingCategoryId(4);
                        productFour.setOfferingCategoryDisplaySequence(CukesConstants.PRODUCT_PRICE_ID);
                        
                        productTypeDTOMap.put(2, productTwo);
                        productTypeDTOMap.put(3, productThree);
                        productTypeDTOMap.put(1, productOne);
                        productTypeDTOMap.put(4, productFour);
                        return productTypeDTOMap;
                    }
                });
    }
}
