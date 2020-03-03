package com.gfs.cpp.proxy.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.proxy.clm.FileRemover;

@RunWith(MockitoJUnitRunner.class)
public class FileRemoverTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @InjectMocks
    private FileRemover target;
    
    @Mock
    private File file;
    
    @Test
    public void shouldDeleteFileIfExists() throws IOException
    {
        file= folder.newFile("abc.docx");
        boolean isDeleted=target.deleteFile(file);
        assertThat(isDeleted, equalTo(true));
        assertThat(file.exists(), equalTo(false)); 
    }
}
