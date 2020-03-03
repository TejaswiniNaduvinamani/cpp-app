package com.gfs.cpp.proxy;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;

@Component
public class ProductTypeMarkupDTOToCustomerPKTransformer implements Transformer<ProductTypeMarkupDTO, CustomerPK>{

    
    @Override
    public CustomerPK transform(final ProductTypeMarkupDTO input) {

        final CustomerPK output = new CustomerPK();
        output.setId(input.getGfsCustomerId());
        output.setTypeCode(input.getGfsCustomerTypeCode());

        return output;
    }
}
