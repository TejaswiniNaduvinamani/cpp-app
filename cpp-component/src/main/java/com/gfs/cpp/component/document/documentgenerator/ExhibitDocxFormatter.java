package com.gfs.cpp.component.document.documentgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;

@Component
public class ExhibitDocxFormatter {

    public static final Logger logger = LoggerFactory.getLogger(ExhibitDocxFormatter.class);

    public File setDocxProperty(XWPFDocument doc, String fileName) throws IOException, InvalidFormatException {

        File file = createNewFile(fileName);
        OPCPackage opc = null;
        boolean b = file.createNewFile();

        if (!b) {
            Boolean isDeleted = file.delete();
            if (logger.isInfoEnabled())
                logger.info("File Deleted -  " + fileName + " : " + isDeleted);
        }
        FileOutputStream fos = createFileOutputStream(file);
        writeDocToFile(doc, fos);
        fos.close();

        try {
            opc = openFileWithOPCPackage(file);
            PackageProperties pp = opc.getPackageProperties();

            pp.setCreatorProperty(CPPConstants.CPP_USER);
            pp.setLastModifiedByProperty(CPPConstants.CPP_USER + CPPConstants.UNDERSCORE + new Date());
            pp.setModifiedProperty(new Nullable<Date>(new Date()));
            doc.close();
            return file;
        } finally {
            if (opc != null) {
                opc.close();
            }
        }
    }

    void writeDocToFile(XWPFDocument doc, FileOutputStream fos) throws IOException {
        doc.write(fos);
    }

    FileOutputStream createFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    File createNewFile(String fileName) {
        return new File(fileName);
    }

    OPCPackage openFileWithOPCPackage(File file) throws InvalidFormatException {
        return OPCPackage.open(file);
    }

}
