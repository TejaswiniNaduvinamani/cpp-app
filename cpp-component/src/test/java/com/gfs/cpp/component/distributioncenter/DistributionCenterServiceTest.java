package com.gfs.cpp.component.distributioncenter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSOAPResponseDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.distributioncenter.DistributionCenterService;
import com.gfs.cpp.component.distributioncenter.DistributionSOAPClientService;
import com.gfs.cpp.component.distributioncenter.DistributionServiceException;
import com.gfs.cpp.component.distributioncenter.DistributionServiceFaultDetail;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;

@Configuration
@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterServiceTest {

    @InjectMocks
    private DistributionCenterService target;

    @Mock
    private ContractPriceProfShipDcRepository contractPriceProfShipDcRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private DistributionSOAPClientService distSOAPService;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Captor
    private ArgumentCaptor<DistributionCenterDO> distributionCenterDOCaptor;

    Environment environment;

    @Before
    public void before() {

        environment = mock(Environment.class);
        when(environment.getProperty(CPPConstants.DCCODES_TO_EXCLUDE)).thenReturn("1,2");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchDistributionCenters() throws Exception, DistributionServiceException {
        Integer companyNumber = 1;
        String statusCode = "AC";
        String languageCode = "en";
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add(statusCode);
        List<DistributionCenterSOAPResponseDTO> distSOAPDtos = new ArrayList<>();
        DistributionCenterSOAPResponseDTO distSOAPDto = new DistributionCenterSOAPResponseDTO();
        distSOAPDto.setDcNumber(1);
        distSOAPDto.setName("TEST");
        distSOAPDto.setShortName("TEST");
        distSOAPDto.setWmsDBInstanceId("5");
        DistributionCenterSOAPResponseDTO distSOAPDto1 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto1.setDcNumber(4);
        distSOAPDto1.setName("TEST 1");
        distSOAPDto1.setShortName("TEST 1");
        distSOAPDto1.setWmsDBInstanceId("6");
        DistributionCenterSOAPResponseDTO distSOAPDto2 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto2.setDcNumber(7);
        distSOAPDto2.setName("NEW TEST 1");
        distSOAPDto2.setShortName("NEW TEST 1");
        distSOAPDto2.setWmsDBInstanceId("5");
        distSOAPDtos.add(distSOAPDto);
        distSOAPDtos.add(distSOAPDto1);
        distSOAPDtos.add(distSOAPDto2);

        when(distSOAPService.getShipDCsForCompanyByLangTypeCode(companyNumber, statusCodes, languageCode)).thenReturn(distSOAPDtos);

        final List<DistributionCenterDTO> result = target.fetchAllDistirbutionCenters(companyNumber, statusCode, languageCode);

        assertTrue(!result.isEmpty());

        verify(distSOAPService).getShipDCsForCompanyByLangTypeCode(eq(companyNumber), eq(statusCodes), eq(languageCode));
    }

    @Test
    public void fetchDistributionCentersWhenDCNo() throws Exception, DistributionServiceException {
        Integer companyNumber = 1;
        String statusCode = "AC";
        String languageCode = "en";
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add(statusCode);
        List<DistributionCenterSOAPResponseDTO> distSOAPDtos = new ArrayList<>();
        DistributionCenterSOAPResponseDTO distSOAPDto = new DistributionCenterSOAPResponseDTO();
        distSOAPDto.setDcNumber(null);
        distSOAPDto.setName("TEST");
        distSOAPDto.setShortName("TEST");
        distSOAPDto.setWmsDBInstanceId("5");
        DistributionCenterSOAPResponseDTO distSOAPDto1 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto1.setDcNumber(4);
        distSOAPDto1.setName("TEST 1");
        distSOAPDto1.setShortName("TEST 1");
        distSOAPDto1.setWmsDBInstanceId("6");
        DistributionCenterSOAPResponseDTO distSOAPDto2 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto2.setDcNumber(7);
        distSOAPDto2.setName("NEW TEST 1");
        distSOAPDto2.setShortName("NEW TEST 1");
        distSOAPDto2.setWmsDBInstanceId("5");
        distSOAPDtos.add(distSOAPDto);
        distSOAPDtos.add(distSOAPDto1);
        distSOAPDtos.add(distSOAPDto2);

        when(distSOAPService.getShipDCsForCompanyByLangTypeCode(companyNumber, statusCodes, languageCode)).thenReturn(distSOAPDtos);

        final List<DistributionCenterDTO> result = target.fetchAllDistirbutionCenters(companyNumber, statusCode, languageCode);

        assertTrue(!result.isEmpty());

        verify(distSOAPService).getShipDCsForCompanyByLangTypeCode(eq(companyNumber), eq(statusCodes), eq(languageCode));
    }

    @Test(expected = CPPRuntimeException.class)
    public void fetchDistributionCentersException() throws Exception, DistributionServiceException {
        Integer companyNumber = 1;
        String statusCode = "AC";
        String languageCode = "en";
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add(statusCode);
        List<DistributionCenterSOAPResponseDTO> distSOAPDtos = new ArrayList<>();
        DistributionCenterSOAPResponseDTO distSOAPDto = new DistributionCenterSOAPResponseDTO();
        distSOAPDto.setDcNumber(1);
        distSOAPDto.setName("TEST");
        distSOAPDto.setShortName("TEST");
        distSOAPDto.setWmsDBInstanceId("5");
        DistributionCenterSOAPResponseDTO distSOAPDto1 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto1.setDcNumber(4);
        distSOAPDto1.setName("TEST 1");
        distSOAPDto1.setShortName("TEST 1");
        distSOAPDto1.setWmsDBInstanceId("6");
        DistributionCenterSOAPResponseDTO distSOAPDto2 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto2.setDcNumber(7);
        distSOAPDto2.setName("NEW TEST 1");
        distSOAPDto2.setShortName("NEW TEST 1");
        distSOAPDto2.setWmsDBInstanceId("5");
        distSOAPDtos.add(distSOAPDto);
        distSOAPDtos.add(distSOAPDto1);
        distSOAPDtos.add(distSOAPDto2);

        Mockito.doThrow(new DistributionServiceException(new DistributionServiceFaultDetail())).when(distSOAPService)
                .getShipDCsForCompanyByLangTypeCode(companyNumber, statusCodes, languageCode);

        target.fetchAllDistirbutionCenters(companyNumber, statusCode, languageCode);

        verify(distSOAPService).getShipDCsForCompanyByLangTypeCode(eq(companyNumber), eq(statusCodes), eq(languageCode));
    }

    @Test
    public void shouldDeleteExistingDc() throws Exception {
        DistributionCenterSaveDTO distributionCenterUpdateDTO = new DistributionCenterSaveDTO();
        ArrayList<String> dcCodes = new ArrayList<String>();
        DistributionCenterDetailDO do1 = new DistributionCenterDetailDO();
        DistributionCenterDetailDO do2 = new DistributionCenterDetailDO();
        List<DistributionCenterDetailDO> doList = new ArrayList<>();

        dcCodes.add("1");
        dcCodes.add("2");
        String userName = "vc71u";

        do1.setDcCode("3");
        do2.setDcCode("2");

        doList.add(do1);
        doList.add(do2);

        distributionCenterUpdateDTO.setDistributionCenters(dcCodes);
        distributionCenterUpdateDTO.setContractPriceProfileSeq(1);
        distributionCenterUpdateDTO.setCreateUserID(userName);
        distributionCenterUpdateDTO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        distributionCenterUpdateDTO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));

        when(cppDateUtils.getFutureDate()).thenReturn(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        when(contractPriceProfShipDcRepository.fetchAllDistributionCenter(1)).thenReturn(doList);

        target.saveDistributionCenters(distributionCenterUpdateDTO, userName);

        verify(cppDateUtils, atLeastOnce()).getFutureDate();
        verify(contractPriceProfShipDcRepository, atLeastOnce()).saveDistributionCenter(distributionCenterDOCaptor.capture(), eq(userName));
        verify(contractPriceProfShipDcRepository, atLeastOnce()).fetchAllDistributionCenter(1);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(distributionCenterUpdateDTO.getContractPriceProfileSeq());

    }

    @Test
    public void shouldExpireExistingDc() throws Exception {
        DistributionCenterSaveDTO distributionCenterSaveDTO = new DistributionCenterSaveDTO();
        ArrayList<String> dcCodes = new ArrayList<String>();
        DistributionCenterDetailDO do1 = new DistributionCenterDetailDO();
        DistributionCenterDetailDO do2 = new DistributionCenterDetailDO();
        List<DistributionCenterDetailDO> doList = new ArrayList<>();

        dcCodes.add("1");
        String userName = "vc71u";

        do1.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        do1.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));
        do1.setDcCode("3");
        do2.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        do2.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));
        do2.setDcCode("2");

        doList.add(do1);
        doList.add(do2);

        distributionCenterSaveDTO.setDistributionCenters(dcCodes);
        distributionCenterSaveDTO.setContractPriceProfileSeq(1);
        distributionCenterSaveDTO.setCreateUserID(userName);
        distributionCenterSaveDTO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        distributionCenterSaveDTO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));

        when(cppDateUtils.getFutureDate()).thenReturn(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        when(contractPriceProfShipDcRepository.fetchAllDistributionCenter(1)).thenReturn(doList);

        target.saveDistributionCenters(distributionCenterSaveDTO, userName);

        verify(cppDateUtils, atLeastOnce()).getFutureDate();
        verify(contractPriceProfShipDcRepository, atLeastOnce()).saveDistributionCenter(distributionCenterDOCaptor.capture(), eq(userName));
        verify(contractPriceProfShipDcRepository, atLeastOnce()).deleteDistributionCenter(distributionCenterDOCaptor.capture(), eq(userName));
        verify(contractPriceProfShipDcRepository, atLeastOnce()).fetchAllDistributionCenter(1);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(distributionCenterSaveDTO.getContractPriceProfileSeq());

    }

    @Test
    public void shouldSaveNewDCs() throws Exception {
        DistributionCenterSaveDTO distributionCenterSaveDTO = new DistributionCenterSaveDTO();
        ArrayList<String> dcCodes = new ArrayList<String>();
        List<DistributionCenterDetailDO> doList = new ArrayList<>();

        dcCodes.add("1");
        dcCodes.add("2");
        String userName = "vc71u";

        distributionCenterSaveDTO.setDistributionCenters(dcCodes);
        distributionCenterSaveDTO.setContractPriceProfileSeq(1);
        distributionCenterSaveDTO.setCreateUserID(userName);
        distributionCenterSaveDTO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        distributionCenterSaveDTO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));

        when(cppDateUtils.getFutureDate()).thenReturn(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        when(contractPriceProfShipDcRepository.fetchAllDistributionCenter(1)).thenReturn(doList);

        target.saveDistributionCenters(distributionCenterSaveDTO, userName);

        verify(cppDateUtils, atLeastOnce()).getFutureDate();
        verify(contractPriceProfShipDcRepository, atLeastOnce()).saveDistributionCenter(distributionCenterDOCaptor.capture(), eq(userName));
        verify(contractPriceProfShipDcRepository, atLeastOnce()).fetchAllDistributionCenter(1);
        verify(contractPriceProfileStatusValidator, atLeastOnce())
                .validateIfContractPricingEditableStatus(distributionCenterSaveDTO.getContractPriceProfileSeq());

        DistributionCenterDO actualDistributionCenterDOSaved = distributionCenterDOCaptor.getValue();

        assertThat(actualDistributionCenterDOSaved.getContractPriceProfileSeq(), equalTo(1));
        assertThat(actualDistributionCenterDOSaved.getDcCodes().get(0), equalTo("1"));
        assertThat(actualDistributionCenterDOSaved.getDcCodes().get(1), equalTo("2"));
        assertThat(actualDistributionCenterDOSaved.getCreateUserID(), equalTo(userName));
        assertThat(actualDistributionCenterDOSaved.getEffectiveDate(),
                equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));
        assertThat(actualDistributionCenterDOSaved.getExpirationDate(),
                equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));

        verify(contractPriceProfShipDcRepository, atLeastOnce()).saveDistributionCenter(distributionCenterDOCaptor.capture(), eq(userName));
        verify(contractPriceProfShipDcRepository, atLeastOnce()).fetchAllDistributionCenter(1);

    }

    @Test
    public void fetchDistributionCentersbyCPPId() throws Exception, DistributionServiceException {
        List<String> result = new ArrayList<String>();
        Integer companyNumber = 1;
        String statusCode = "AC";
        String languageCode = "en";
        List<String> statusCodes = new ArrayList<>();
        List<String> distNames = new ArrayList<>();
        statusCodes.add(statusCode);
        List<DistributionCenterSOAPResponseDTO> distSOAPDtos = new ArrayList<>();
        DistributionCenterSOAPResponseDTO distSOAPDto = new DistributionCenterSOAPResponseDTO();
        distSOAPDto.setDcNumber(3);
        distSOAPDto.setName("TEST");
        distSOAPDto.setShortName("TEST");
        distSOAPDto.setWmsDBInstanceId("5");
        distNames.add("TEST");
        DistributionCenterSOAPResponseDTO distSOAPDto1 = new DistributionCenterSOAPResponseDTO();
        distSOAPDto1.setDcNumber(4);
        distSOAPDto1.setName("TEST");
        distSOAPDto1.setShortName("TEST");
        distSOAPDto1.setWmsDBInstanceId("5");
        distSOAPDtos.add(distSOAPDto1);
        List<DistributionCenterDTO> distCenterList = new ArrayList<>();
        DistributionCenterDTO dist = new DistributionCenterDTO();
        dist.setDcNumber("1");
        dist.setName("Test");
        dist.setShortName("Test");
        distCenterList.add(dist);
        result.add("3");
        result.add("4");
        DistributionCenterDetailDO do1 = new DistributionCenterDetailDO();
        do1.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        do1.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));
        do1.setDcCode("3");
        DistributionCenterDetailDO do2 = new DistributionCenterDetailDO();
        do2.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2016"));
        do2.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/30/2017"));
        do2.setDcCode("2");
        List<DistributionCenterDetailDO> doList = new ArrayList<>();
        doList.add(do1);
        doList.add(do2);

        when(distSOAPService.getShipDCsForCompanyByLangTypeCode(companyNumber, statusCodes, languageCode)).thenReturn(distSOAPDtos);
        when(contractPriceProfShipDcRepository.fetchDistributionCentersbyContractPriceProfileSeq(1)).thenReturn(result);
        when(contractPriceProfShipDcRepository.fetchSavedDistributionCenters(1)).thenReturn(doList);
        when(target.fetchDistributionCentersbyContractPriceProfileSeq(1)).thenReturn(result);

        assertThat(contractPriceProfShipDcRepository.fetchDistributionCentersbyContractPriceProfileSeq(1), equalTo(result));
        assertThat(target.fetchDistributionCentersbyContractPriceProfileSeq(1), equalTo(distNames));
        assertThat(target.fetchSavedDistributionCenters(1), equalTo(doList));

        verify(contractPriceProfShipDcRepository, atLeastOnce()).fetchDistributionCentersbyContractPriceProfileSeq(eq(1));
        verify(distSOAPService, atLeastOnce()).getShipDCsForCompanyByLangTypeCode(eq(companyNumber), eq(statusCodes), eq(languageCode));

    }

}
