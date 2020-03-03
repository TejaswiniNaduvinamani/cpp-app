package com.gfs.cpp.component.distributioncenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSOAPResponseDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;

@Service("distributionCenterService")
public class DistributionCenterService {

    @Autowired
    private Environment environment;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    @Qualifier("contractPriceProfShipDcRepository")
    private ContractPriceProfShipDcRepository contractPriceProfShipDcRepository;

    @Autowired
    private DistributionSOAPClientService distSOAPService;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<DistributionCenterDTO> fetchAllDistirbutionCenters(Integer companyNumber, String statusCode, String languageCode) {
        List<String> statusCodes = new ArrayList<>();
        List<DistributionCenterSOAPResponseDTO> distCenterResponse = null;
        statusCodes.add(statusCode);
        try {
            distCenterResponse = distSOAPService.getShipDCsForCompanyByLangTypeCode(companyNumber, statusCodes, languageCode);
        } catch (DistributionServiceException distributionServiceException) {
            logger.error("Request to call SOAP Service failed" + distributionServiceException);
            throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, " Service failed :" + distributionServiceException);
        }
        return filterExcludedDcList(distCenterResponse);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveDistributionCenters(DistributionCenterSaveDTO distributionCenterSaveDTO, String userName) {

        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(distributionCenterSaveDTO.getContractPriceProfileSeq());
        Collection<String> oldDcCodes = new ArrayList<>();
        List<DistributionCenterDetailDO> distributionCenterDetailDOs = contractPriceProfShipDcRepository
                .fetchAllDistributionCenter(distributionCenterSaveDTO.getContractPriceProfileSeq());
        for (DistributionCenterDetailDO distDTO : distributionCenterDetailDOs) {
            oldDcCodes.add(distDTO.getDcCode());
        }
        Collection<String> deleteDCCodes = CollectionUtils.subtract(oldDcCodes, distributionCenterSaveDTO.getDistributionCenters());
        Collection<String> newDCCodes = CollectionUtils.subtract(distributionCenterSaveDTO.getDistributionCenters(), oldDcCodes);
        if (CollectionUtils.isNotEmpty(newDCCodes)) {
            DistributionCenterDO distributionCenterToInsert = createDistributionCenterDO(distributionCenterSaveDTO, (ArrayList<String>) newDCCodes,
                    cppDateUtils.getFutureDate());
            contractPriceProfShipDcRepository.saveDistributionCenter(distributionCenterToInsert, userName);
        }
        if (CollectionUtils.isNotEmpty(deleteDCCodes)) {
            DistributionCenterDO distributionCenterToDelete = createDistributionCenterDO(distributionCenterSaveDTO, (ArrayList<String>) deleteDCCodes,
                    null);
            contractPriceProfShipDcRepository.deleteDistributionCenter(distributionCenterToDelete, userName);
        }

    }

    public List<String> fetchDistributionCentersbyContractPriceProfileSeq(int contractPriceProfileSeq) {
        List<DistributionCenterDTO> distributionCenterDTO = fetchAllDistirbutionCenters(CPPConstants.COMPANY_NUMBER, CPPConstants.STATUS_CODE,
                CPPConstants.LANGUAGE_CODE);
        List<String> dcsForContractPriceProfile = contractPriceProfShipDcRepository
                .fetchDistributionCentersbyContractPriceProfileSeq(contractPriceProfileSeq);
        List<String> distributionCenterList = new ArrayList<>();
        for (DistributionCenterDTO distributionCenter : distributionCenterDTO) {
            for (String dcNumber : dcsForContractPriceProfile) {
                if (distributionCenter.getDcNumber().equals(dcNumber)) {
                    distributionCenterList.add(distributionCenter.getName());
                }
            }
        }
        return distributionCenterList;
    }

    public List<DistributionCenterDetailDO> fetchSavedDistributionCenters(Integer contractPriceProfileSeq) {
        return contractPriceProfShipDcRepository.fetchSavedDistributionCenters(contractPriceProfileSeq);
    }

    private DistributionCenterDO createDistributionCenterDO(DistributionCenterSaveDTO distributionCenterSaveDTO, ArrayList<String> dcCodes,
            Date expirationDate) {
        DistributionCenterDO distributionCenterDO = new DistributionCenterDO();
        distributionCenterDO.setContractPriceProfileSeq(distributionCenterSaveDTO.getContractPriceProfileSeq());
        distributionCenterDO.setCreateUserID(distributionCenterSaveDTO.getCreateUserID());
        distributionCenterDO.setDcCodes(dcCodes);
        distributionCenterDO.setExpirationDate(expirationDate);
        distributionCenterDO.setEffectiveDate(cppDateUtils.getFutureDate());
        return distributionCenterDO;
    }

    private ArrayList<DistributionCenterDTO> filterExcludedDcList(List<DistributionCenterSOAPResponseDTO> distCenterResponseList) {
        String dcCodesToExclude = environment.getProperty(CPPConstants.DCCODES_TO_EXCLUDE);
        List<String> excludedDcList = Arrays.asList(dcCodesToExclude.split(CPPConstants.DELIMITER_COMMA));
        ArrayList<DistributionCenterDTO> distCenterList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(distCenterResponseList)) {
            for (DistributionCenterSOAPResponseDTO distSOAPDTO : distCenterResponseList) {
                String dcNumber = (null != distSOAPDTO.getDcNumber()) ? String.valueOf(distSOAPDTO.getDcNumber()) : StringUtils.EMPTY;
                if (StringUtils.isNotBlank(distSOAPDTO.getWmsDBInstanceId()) && !excludedDcList.contains(dcNumber)) {
                    buildDistributionCenterDTO(distCenterList, distSOAPDTO, dcNumber);
                }
            }
        }
        sortDistributionCenterList(distCenterList);
        return distCenterList;
    }

    private void sortDistributionCenterList(ArrayList<DistributionCenterDTO> distCenterList) {
        Collections.sort(distCenterList, new Comparator<DistributionCenterDTO>() {
            public int compare(DistributionCenterDTO distributionDTO1, DistributionCenterDTO distributionDTO2) {
                return distributionDTO1.getShortName().compareTo(distributionDTO2.getShortName());
            }
        });
    }

    private void buildDistributionCenterDTO(ArrayList<DistributionCenterDTO> distCenterList, DistributionCenterSOAPResponseDTO distSOAPDTO,
            String dcNumber) {
        DistributionCenterDTO distributionCenterDTO = new DistributionCenterDTO();
        distributionCenterDTO.setDcNumber(dcNumber);
        distributionCenterDTO.setName(distSOAPDTO.getName());
        distributionCenterDTO.setShortName(distSOAPDTO.getShortName());
        distributionCenterDTO.setWmsDBInstanceId(distSOAPDTO.getWmsDBInstanceId());
        distCenterList.add(distributionCenterDTO);
    }
}