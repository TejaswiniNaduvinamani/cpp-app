package com.gfs.cpp.data.furtherance;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.furtherance.CppFurtheranceTrackingRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CppFurtheranceTrackingRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final int FURTHERANCE_ACTION_CODE = 2;
    private static final String USER_NAME = "test user";
    private static final String TEST_TABLE = "CIP";

    @Autowired
    private CppFurtheranceTrackingRepository target;

    @ExpectedDatabase(value = "CppFurtheranceTrackingRepositoryIntegrationTest.shouldAddTracking-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldAddTrackingEntry() {

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = buildFurtheranceWrapperDTO(-1001);
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();
        cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);

        target.batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, USER_NAME);

    }

    @ExpectedDatabase(value = "CppFurtheranceTrackingRepositoryIntegrationTest.shouldAddTracking.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppFurtheranceTrackingRepositoryIntegrationTest.shouldAddTracking-result.xml")
    @Test
    public void shouldDeleteTrackingEntry() {

        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildFurtheranceWrapperDTO(-1001);
        target.deleteTrackingEntry(furtheranceTrackingDTO, FURTHERANCE_ACTION_CODE);
    }

    @DatabaseSetup(value = "CppFurtheranceTrackingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchFurtheranceActionCode() {

        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildFurtheranceWrapperDTO(-1001);
        Integer furtheranceAction = target.fetchFurtheranceActionCode(furtheranceTrackingDTO);

        assertThat(furtheranceAction, is(1));

    }

    @DatabaseSetup(value = "CppFurtheranceTrackingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchNullFurtheranceActionCode() {

        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildFurtheranceWrapperDTO(-1002);
        Integer furtheranceAction = target.fetchFurtheranceActionCode(furtheranceTrackingDTO);

        assertThat(furtheranceAction, nullValue());

    }

    private CPPFurtheranceTrackingDTO buildFurtheranceWrapperDTO(Integer furtheranceSeq) {
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        furtheranceTrackingDTO.setGfsCustomerId("-1023");
        furtheranceTrackingDTO.setGfsCustomerTypeCode(-31);
        furtheranceTrackingDTO.setCppFurtheranceSeq(furtheranceSeq);
        furtheranceTrackingDTO.setItemPriceLevelCode(1);
        furtheranceTrackingDTO.setItemPriceId("itemId");
        furtheranceTrackingDTO.setChangeTableName(TEST_TABLE);
        furtheranceTrackingDTO.setFurtheranceActionCode(FURTHERANCE_ACTION_CODE);
        return furtheranceTrackingDTO;
    }

    @DatabaseSetup(value = "CppFurtheranceTrackingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchFurtheranceDetailsByCPPFurtheranceSeq() throws Exception {
        int cppFurtheranceSeq = -1001;

        List<CPPFurtheranceTrackingDTO> actual = target.fetchFurtheranceDetailsByCPPFurtheranceSeq(cppFurtheranceSeq);

        assertThat(actual.size(), is(1));

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = actual.get(0);

        assertThat(cppFurtheranceTrackingDTO.getCppFurtheranceSeq(), equalTo(-1001));
        assertThat(cppFurtheranceTrackingDTO.getFurtheranceActionCode(), equalTo(1));
        assertThat(cppFurtheranceTrackingDTO.getItemPriceId(), equalTo("itemId"));
        assertThat(cppFurtheranceTrackingDTO.getItemPriceLevelCode(), equalTo(1));
        assertThat(cppFurtheranceTrackingDTO.getGfsCustomerId(), equalTo("-1023"));
        assertThat(cppFurtheranceTrackingDTO.getGfsCustomerTypeCode(), equalTo(-31));
        assertThat(cppFurtheranceTrackingDTO.getChangeTableName(), equalTo("CIP"));

    }

}
