package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@Component
public class ItemServiceProxy {

    private static final String PRODUCT_TYPE_URL = "api/itemConfiguration/offeringCategory?languageTypeCode=en";

    public static final Logger logger = LoggerFactory.getLogger(ItemServiceProxy.class);

    @Autowired
    private CppRestTemplate cppRestTemplate;

    @Value("${item.service.url}")
    private String itemServiceUrl;

    @Autowired
    private Environment environment;

    private Map<Integer, String> allProductTypesById;

    @SuppressWarnings("unchecked")
    public Map<Integer, ProductTypeDTO> fetchProductItems() {
        return cppRestTemplate.postForObject(itemServiceUrl + PRODUCT_TYPE_URL, null, Map.class);
    }

    public Map<Integer, String> getAllProductTypesById() {
        if (MapUtils.isEmpty(allProductTypesById)) {

            allProductTypesById = new HashMap<>();

            List<ProductTypeDTO> productTypeDTOList = fetchProductType();
            for (ProductTypeDTO productTypeDTO : productTypeDTOList) {
                allProductTypesById.put(productTypeDTO.getOfferingCategoryId(), productTypeDTO.getOfferingCategoryDescription());
            }
        }
        return allProductTypesById;
    }

    List<ProductTypeDTO> fetchProductType() {

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ProductTypeDTO> productTypeDTOs = new ArrayList<>();
        try {
            String productsToExclude = environment.getProperty(CPPConstants.PRODUCTS_TO_EXCLUDE);
            List<String> productCodes = Arrays.asList(productsToExclude.split(CPPConstants.DELIMITER_COMMA));
            Map<Integer, ProductTypeDTO> productTypeDTOMap = mapper.convertValue(fetchProductItems(),
                    new TypeReference<HashMap<Integer, ProductTypeDTO>>() {
                    });
            if (null != productTypeDTOMap) {
                for (Map.Entry<Integer, ProductTypeDTO> entry : productTypeDTOMap.entrySet()) {
                    if (!productCodes.contains(String.valueOf(entry.getValue().getOfferingCategoryId())))
                        productTypeDTOs.add(entry.getValue());
                }
            }
        } catch (Exception e) {
            logger.error("The PA system service call failed " + e.getMessage());
        }
        return productTypeDTOs;

    }

    public String getProductType(Integer categoryId) {
        return getAllProductTypesById().get(categoryId);

    }
}
