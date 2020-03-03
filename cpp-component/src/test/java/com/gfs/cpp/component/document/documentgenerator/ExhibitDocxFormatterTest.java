package com.gfs.cpp.component.document.documentgenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxFormatter;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitDocxFormatterTest {

    private static final String FILE_NAME = "TEST.docx";

    @InjectMocks
    @Spy
    private ExhibitDocxFormatter target;
    @Mock
    private File file;
    @Mock
    private FileOutputStream fileOutputStream;
    @Mock
    private XWPFDocument doc;
    @Mock
    private OPCPackage opc;
    @Mock
    private PackageProperties packageProperties;

    @Test
    public void shouldSetPropertietsForDocument() throws InvalidFormatException, IOException {

        doReturn(file).when(target).createNewFile(FILE_NAME);
        doReturn(opc).when(target).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(target).createFileOutputStream(file);
        doNothing().when(target).writeDocToFile(doc, fileOutputStream);
        doReturn(packageProperties).when(opc).getPackageProperties();
        doReturn(true).when(file).createNewFile();

        assertThat(target.setDocxProperty(doc, FILE_NAME), equalTo(file));

        verify(target).writeDocToFile(doc, fileOutputStream);
        verify(packageProperties).setCreatorProperty(CPPConstants.CPP_USER);
        verify(doc).close();
        verify(opc).close();
        verify(file, never()).delete();

    }

    @Test
    public void shouldDeleteFileWhenItIsNotCreatedAlready() throws Exception {

        doReturn(file).when(target).createNewFile(FILE_NAME);
        doReturn(opc).when(target).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(target).createFileOutputStream(file);
        doNothing().when(target).writeDocToFile(doc, fileOutputStream);
        doReturn(packageProperties).when(opc).getPackageProperties();

        assertThat(target.setDocxProperty(doc, FILE_NAME), equalTo(file));

        verify(file).delete();

    }
}
