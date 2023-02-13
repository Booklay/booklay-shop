package com.nhnacademy.booklay.server.controller.storage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.server.service.storage.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StorageController.class)
@MockBean(JpaMetamodelMappingContext.class)
class StorageControllerTest {

    @Autowired
    StorageController controller;

    @MockBean
    FileService fileService;

    @Autowired
    MockMvc mockMvc;

    private static final String URI_PREFIX = "/storage";

    MockMultipartFile file;

    @BeforeEach
    void setUp(){
        file = new MockMultipartFile(
            "file",
            "file.txt",
            "test/txt",
            "multipart".getBytes());
    }


    @Test
    void uploadFile() throws Exception {

        mockMvc.perform(multipart(URI_PREFIX)
                .file(file))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

    }

    @Test
    void downloadUrl() throws Exception {

        mockMvc.perform(get(URI_PREFIX + "/1"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

    }

}