package com.gfs.cpp.component.assignment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Service
public class AssignmentService {

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private MarkupAssignmentFetcher markupAssignmentFetcher;

    @Autowired
    private ItemAssignmentHelper itemAssignmentHelper;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Autowired
    private FindCustomerAssignmentValidator findCustomerAssignmentValidator;

    @Autowired
    private SaveCustomerAssignmentValidator saveCustomerAssignmentValidator;

    @Autowired
    private CustomerAssignmentBuilder customerAssignmentBuilder;

    static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    public List<MarkupAssignmentDTO> fetchMarkupAssignment(int contractPriceProfileSeq) {
        return markupAssignmentFetcher.fetchMarkupAssignment(contractPriceProfileSeq);
    }

    public List<CMGCustomerTypeDTO> fetchCustomerTypes() {
        List<CMGCustomerTypeDTO> customerTypeList = new ArrayList<>();
        for (CustomerTypeCodeEnum cutomerTypeCode : CustomerTypeCodeEnum.values()) {
            if (!CPPConstants.CUSTOMER_GROUP_CMG.equals(cutomerTypeCode.getGroupType())) {
                CMGCustomerTypeDTO cmgCustomerTypeDTO = new CMGCustomerTypeDTO();
                cmgCustomerTypeDTO.setCustomerTypeId(cutomerTypeCode.getGfsCustomerTypeCode());
                cmgCustomerTypeDTO.setCustomerTypeValue(cutomerTypeCode.getGfsCustomerTypeName());
                customerTypeList.add(cmgCustomerTypeDTO);
            }
        }
        return customerTypeList;
    }

    public List<ItemAssignmentWrapperDTO> fetchAllFutureItems(int contractPriceProfileSeq) {
        return itemAssignmentHelper.fetchAllFutureItems(contractPriceProfileSeq);
    }

    public RealCustomerResponseDTO findCustomerName(String gfsCustomerId, int gfsCustomerType, int contractPriceProfileSeq, String cmgCustomerId,
            int cmgCustomerType) {
        RealCustomerResponseDTO realCustomerResponseDTO = new RealCustomerResponseDTO();
        try {
            String customerName = customerServiceProxy.findCustomerName(gfsCustomerId, gfsCustomerType);

            CustomerAssignmentDTO customerAssignmentDTO = customerAssignmentBuilder.buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                    contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
            findCustomerAssignmentValidator.validateCustomer(customerAssignmentDTO);

            realCustomerResponseDTO.setCustomerName(customerName);
            realCustomerResponseDTO.setValidationCode(HttpStatus.OK.value());
            realCustomerResponseDTO.setValidationMessage(StringUtils.EMPTY);

        } catch (CPPRuntimeException cpp) {
            if (anyCustomerValidationError(cpp)) {
                realCustomerResponseDTO.setCustomerName(StringUtils.EMPTY);
                realCustomerResponseDTO.setValidationCode(cpp.getErrorCode());
                realCustomerResponseDTO.setValidationMessage(cpp.getMessage());
            } else {
                throw cpp;
            }
        }
        return realCustomerResponseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public StatusDTO saveAssignments(CustomerAssignmentDTO customerAssignmentDTO, String userName) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            contractPriceProfileStatusValidator.validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

            if (CollectionUtils.isNotEmpty(customerAssignmentDTO.getRealCustomerDTOList())) {
                saveCustomerAssignmentValidator.validateCustomer(customerAssignmentDTO);
                List<CustomerAssignmentDO> saveAssignmentDOList = buildSaveAssignmentDOList(customerAssignmentDTO);
                cppConceptMappingRepository.saveAssignments(saveAssignmentDOList, userName);
            }
        } catch (CPPRuntimeException cpp) {
            if (anyCustomerValidationError(cpp)) {
                statusDTO.setErrorCode(cpp.getErrorCode());
                statusDTO.setErrorMessage(cpp.getMessage());
                statusDTO.setErrorType(cpp.getType().toString());
            } else {
                throw cpp;
            }
        }
        return statusDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteCustomerAssignment(String realCutomerId, int realCustomerType, int contractPriceProfileSeq, String cmgCustomerId,
            int cmgCustomerType) {
        contractPriceProfileStatusValidator.validateIfCustomerAssignmentEditableStatus(contractPriceProfileSeq);

        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setGfsCustomerId(realCutomerId);
        customerAssignmentDO.setGfsCustomerType(realCustomerType);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq,
                cmgCustomerId, cmgCustomerType);
        if (cmgCustomerResponseDTO.getDefaultCustomerInd() == CPPConstants.INDICATOR_ONE) {
            cppConceptMappingRepository.deleteAllCustomerMapping(contractPriceProfileSeq);
        } else {
            cppConceptMappingRepository.deleteCustomerMapping(customerAssignmentDO);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public StatusDTO assignItems(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO, String userName) {
        StatusDTO statusDTO = new StatusDTO();
        try {
            contractPriceProfileStatusValidator.validateIfItemAssignmentEditableStatus(itemAssignmentWrapperDTO.getContractPriceProfileSeq());
            if (CollectionUtils.isNotEmpty(itemAssignmentWrapperDTO.getItemAssignmentList())) {

                itemAssignmentHelper.assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);
                statusDTO.setErrorCode(HttpStatus.OK.value());
            }
        } catch (CPPRuntimeException cpp) {
            if (cpp.getType() == CPPExceptionType.NOT_VALID_STATUS) {
                throw cpp;
            }
            statusDTO.setErrorCode(cpp.getErrorCode());
            statusDTO.setErrorMessage(cpp.getMessage());
        }
        return statusDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteItemAssignment(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode, String itemId, String futureItemDesc,
            String userName) {
        contractPriceProfileStatusValidator.validateIfItemAssignmentEditableStatus(contractPriceProfileSeq);
        ItemLevelMarkupDTO futureItemDTO = customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, futureItemDesc);

        itemAssignmentHelper.expireItemMapping(futureItemDTO.getCustomerItemDescSeq(), itemId, userName);

        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        customerItemPriceRepository.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList,
                ItemPriceLevel.ITEM.getCode());
    }

    private boolean anyCustomerValidationError(CPPRuntimeException cpp) {
        return cpp.getType().equals(CPPExceptionType.HAS_MULTIPLE_CUSTOMER_MAPPING)
                || cpp.getType().equals(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT)
                || cpp.getType().equals(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_CONCEPTS)
                || cpp.getType().equals(CPPExceptionType.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER)
                || cpp.getType().equals(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT)
                || cpp.getType().equals(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE)
                || cpp.getType().equals(CPPExceptionType.INVALID_CUSTOMER_FOUND) || cpp.getType().equals(CPPExceptionType.INACTIVE_CUSTOMER_FOUND);
    }

    private List<CustomerAssignmentDO> buildSaveAssignmentDOList(CustomerAssignmentDTO customerAssignmentDTO) {
        List<CustomerAssignmentDO> customerAssignmentDOList = new ArrayList<>();
        int cppCustomerSeq = contractPriceProfCustomerRepository.fetchCPPCustomerSeq(customerAssignmentDTO.getCmgCustomerId(),
                customerAssignmentDTO.getCmgCustomerType(), customerAssignmentDTO.getContractPriceProfileSeq());
        for (RealCustomerDTO realCustomerDTO : customerAssignmentDTO.getRealCustomerDTOList()) {
            CustomerAssignmentDO customerAssignmentDO = buildCustomerAssignmentDO(cppCustomerSeq, realCustomerDTO);
            customerAssignmentDOList.add(customerAssignmentDO);
        }
        return customerAssignmentDOList;
    }

    private CustomerAssignmentDO buildCustomerAssignmentDO(int cppCustomerSeq, RealCustomerDTO realCustomerDTO) {
        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setCppCustomerSeq(cppCustomerSeq);
        customerAssignmentDO.setGfsCustomerId(realCustomerDTO.getRealCustomerId());
        customerAssignmentDO.setGfsCustomerType(realCustomerDTO.getRealCustomerType());
        return customerAssignmentDO;
    }

}
