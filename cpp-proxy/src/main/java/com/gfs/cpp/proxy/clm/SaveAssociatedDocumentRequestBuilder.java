package com.gfs.cpp.proxy.clm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.exception.CPPRuntimeException;

@Component
public class SaveAssociatedDocumentRequestBuilder {

    public String buildSaveAssociateDocumentRequest(File exhibitDocument, String exhibitSysId) {

        JSONObject saveDocJsonObject = new JSONObject();
        JSONObject associatedDocumentData = new JSONObject();
        associatedDocumentData.put("sysId", exhibitSysId);

        String fileByteArray = buildFileByteArray(exhibitDocument, exhibitSysId);

        saveDocJsonObject.put("fileData", fileByteArray);
        saveDocJsonObject.put("associatedDocument", associatedDocumentData);
        saveDocJsonObject.put("fileType", "1");
        return saveDocJsonObject.toString();
    }

    private String buildFileByteArray(File exhibitDocument, String exhibitSysId) {

        try (FileInputStream fileInputStreamReader = createFileInputStream(exhibitDocument)) {

            byte[] bytes = new byte[(int) exhibitDocument.length()];
            fileInputStreamReader.read(bytes);
            return encodeFileData(bytes);
        } catch (IOException ex) {
            throw new CPPRuntimeException(exhibitSysId, ex);
        }

    }

    String encodeFileData(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    FileInputStream createFileInputStream(File exhibitDocument) throws FileNotFoundException {
        return new FileInputStream(exhibitDocument);
    }

}
