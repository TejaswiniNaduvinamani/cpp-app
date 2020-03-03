package com.gfs.cpp.data.review;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.review.ReviewRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;

public class ReviewRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    ReviewRepository target;

    @Test
    @DatabaseSetup(value = { "ReviewRepositoryIntegrationTest.xml" })
    public void shouldFetchContractSequenceMarkup() throws Exception {
        assertThat(target.fetchContractLanguageSeq("column desc"), equalTo(-201));
    }

    @Test
    @DatabaseSetup(value = { "ReviewRepositoryIntegrationTest.shouldFetchDefaultContractLanguage.xml" })
    public void shouldFetchContractLanguageMarkup() throws Exception {
        assertThat(target.fetchContractLanguageMarkup(), equalTo("default contract desc"));
    }

    @Test
    @DatabaseSetup(value = { "ReviewRepositoryIntegrationTest.shouldFetchContractLanguage.xml" })
    public void shouldFetchContractLanguage() throws Exception {
        assertThat(target.fetchContractLanguage(-231, "attribute colum value"), equalTo("contract desc"));
    }
}
