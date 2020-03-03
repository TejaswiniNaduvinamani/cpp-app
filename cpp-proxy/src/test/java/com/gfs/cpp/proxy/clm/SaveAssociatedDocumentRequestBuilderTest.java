package com.gfs.cpp.proxy.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.clm.SaveAssociatedDocumentRequestBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SaveAssociatedDocumentRequestBuilderTest {

    @InjectMocks
    @Spy
    private SaveAssociatedDocumentRequestBuilder target;

    @Mock
    private File exhibitDocument;
    @Mock
    private FileInputStream fileInputStreamMock;

    @Test
    public void shouldBuildSaveAssociatedDocumentWithAllData() throws Exception {

        String exhibitSysId = "sysId";

        doReturn(10l).when(exhibitDocument).length();
        doReturn(fileInputStreamMock).when(target).createFileInputStream(exhibitDocument);
        String fileData = "FileByteData";
        doReturn(fileData).when(target).encodeFileData(any(byte[].class));

        String actualResponse = target.buildSaveAssociateDocumentRequest(exhibitDocument, exhibitSysId);

        JSONObject actualObject = new JSONObject(actualResponse);

        assertThat(actualObject.get("fileType").toString(), equalTo("1"));
        assertThat(actualObject.get("fileData").toString(), equalTo(fileData));
        assertThat(actualObject.getJSONObject("associatedDocument").get("sysId").toString(), equalTo(exhibitSysId));

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowCppRuntimeExceptionWhenThereIsIoException() throws Exception {

        doThrow(IOException.class).when(target).createFileInputStream(exhibitDocument);
        target.buildSaveAssociateDocumentRequest(exhibitDocument, "sysId");
    }

}
