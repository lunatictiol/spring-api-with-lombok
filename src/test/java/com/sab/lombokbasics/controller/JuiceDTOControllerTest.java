package com.sab.lombokbasics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.services.JuiceService;
import com.sab.lombokbasics.services.JuiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
@WebMvcTest(JuiceController.class)
public class JuiceDTOControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JuiceService juiceService;
    @Captor
    ArgumentCaptor<JuiceDTO> juiceDTOArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;


    JuiceServiceImpl juiceServiceImpl;
    @Autowired
    private JuiceController juiceController;

    @BeforeEach
    void setUp() {
        juiceServiceImpl = new JuiceServiceImpl();
    }

    @Test
    void patchJuiceDTO() throws Exception {
        JuiceDTO juiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);


        Map<String, Object> juiceMap = new HashMap<>();
        juiceMap.put("juiceName", "New Name");

        mockMvc.perform(patch(JuiceController.JUICE_PATH_WITH_ID, juiceDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juiceMap)))
                .andExpect(status().isNoContent());

        verify(juiceService).patchJuiceById(uuidArgumentCaptor.capture(), juiceDTOArgumentCaptor.capture());

        assertThat(juiceDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(juiceMap.get("juiceName")).isEqualTo(juiceDTOArgumentCaptor.getValue().getJuiceName());
    }

    @Test
    void deleteJuice() throws Exception {
        JuiceDTO juiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);
       given(juiceService.deleteJuice(any())).willReturn(true);
        mockMvc.perform(delete(JuiceController.JUICE_PATH_WITH_ID, juiceDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(juiceService).deleteJuice(uuidArgumentCaptor.capture());
        assertThat(juiceDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }


    @Test
    void getJuiceById() throws Exception {
        JuiceDTO testJuiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);
        given(juiceService.getJuiceById(testJuiceDTO.getId())).willReturn(Optional.of(testJuiceDTO));

        mockMvc.perform(get(JuiceController.JUICE_PATH_WITH_ID, testJuiceDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testJuiceDTO.getId().toString())))
                .andExpect(jsonPath("$.juiceName", is(testJuiceDTO.getJuiceName())))
        ;
    }

    @Test
    void listJuices() throws Exception {
        given(juiceService.PageJuice(any(), any(), any(), any(), any())).willReturn(juiceServiceImpl.PageJuice(null, null, false, null, null));

        mockMvc.perform(get(JuiceController.JUICE_PATH )
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));

    }

    @Test
    void getJuiceByIdNotFound() throws Exception {
        given(juiceService.getJuiceById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(JuiceController.JUICE_PATH_WITH_ID ,UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createJuice() throws Exception {

        JuiceDTO juiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);
        juiceDTO.setId(null);
        juiceDTO.setVersion(null);
        given(juiceService.saveNewJuice(any(JuiceDTO.class))).willReturn(juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(1));

        mockMvc.perform(post(JuiceController.JUICE_PATH )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juiceDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }
    @Test
    void createJuiceWithNullValues() throws Exception {

        JuiceDTO juiceDTO = JuiceDTO.builder().build();
        given(juiceService.saveNewJuice(any(JuiceDTO.class))).willReturn(juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(1));

        MvcResult mvcResult= mockMvc.perform(post(JuiceController.JUICE_PATH )
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juiceDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()",is(6))).andReturn()
                ;

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void updateJuice() throws Exception {
        JuiceDTO juiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);
        given(juiceService.updateJuice(any(),any())).willReturn(Optional.of(juiceDTO  ));
        mockMvc.perform(put(JuiceController.JUICE_PATH_WITH_ID, juiceDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(juiceDTO)));

        verify(juiceService).updateJuice(any(UUID.class),any(JuiceDTO.class));
    }
    @Test
    void updateJuiceWithNullValues() throws Exception {
        JuiceDTO juiceDTO = juiceServiceImpl.PageJuice(null, null, null, 1, 25).getContent().get(0);
        JuiceDTO emptyDTO = JuiceDTO.builder().build();
        given(juiceService.updateJuice(any(),any())).willReturn(Optional.of(juiceDTO  ));
        MvcResult mvcResult=mockMvc.perform(put(JuiceController.JUICE_PATH_WITH_ID, juiceDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


}
