package com.sab.lombokbasics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.lombokbasics.config.SecurityConfig;
import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.mapper.JuiceMapper;
import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.repository.JuiceRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sab.lombokbasics.controller.JuiceControllerTest.jwtRequestPostProcessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class JuiceControllerIT {

    @Autowired
    private JuiceController juiceController;
    @Autowired
    private JuiceRepository juiceRepository;

    @Autowired
    private JuiceMapper juiceMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }


    @Transactional
    @Rollback
    @Test
    void deleteJuice(){
        Juice juice = juiceRepository.findAll().get(0);
        ResponseEntity responseEntity = juiceController.deleteJuice(juice.getId());

        assertThat(juiceRepository.findById(juice.getId()).isEmpty());
    }

    @Test
    void deleteJuiceByIdNotFound(){
        assertThrows(NotFoundException.class, () -> juiceController.deleteJuice(UUID.randomUUID()));
    }


    @Transactional
    @Rollback
    @Test
    void updateJuice(){
        Juice juice = juiceRepository.findAll().get(0);
        JuiceDTO juiceDTO = juiceMapper.JuiceToJuiceDTO(juice);

        juiceDTO.setId(null);
        juice.setVersion(null);
        final String UpdatedName = "UpdatedName";
        juiceDTO.setJuiceName(UpdatedName);
       ResponseEntity responseEntity =  juiceController.updateJuice(juice.getId(),juiceDTO);
       assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
       Juice updatedJuice = juiceRepository.findById(juice.getId()).get();


       assertThat(updatedJuice.getJuiceName()).isEqualTo(UpdatedName);

    }
    @Test
    void updateJuiceNotFound(){
        assertThrows(NotFoundException.class,()->{
            juiceController.updateJuice(UUID.randomUUID(),JuiceDTO.builder().build());
        });
    }
    @Test
    void getAllJuices() {
     Page<JuiceDTO> juiceDTOList = juiceController.PageJuices(null,null,null, 1, 50);

     assertThat(juiceDTOList.getContent().size()).isEqualTo(50);

    }
    @Transactional
    @Rollback
    @Test
    void juiceListNotFoundException() {
        juiceRepository.deleteAll();
        Page<JuiceDTO> juiceDTOList = juiceController.PageJuices(null,null,null, 1, 2);

        assertThat(juiceDTOList.getContent().size()).isEqualTo(0);

    }

    @Test
    void getJuiceById() {
        Juice juice = juiceRepository.findAll().get(0);

        JuiceDTO juiceDTO  = juiceController.getJuiceById(juice.getId());

        assertThat(juiceDTO.getId()).isNotNull();
    }

    @Test
    void juiceIdNotFoundException() {
        assertThrows(NotFoundException.class,()->{
            juiceController.getJuiceById(UUID.randomUUID());
        });
    }
    @Transactional
    @Rollback
    @Test
    void createJuice() {
        JuiceDTO juiceDTO = JuiceDTO.builder()
                .juiceName("test juice")
                .build();

        ResponseEntity responseEntity = juiceController.createJuice(juiceDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUId = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUId[locationUUId.length - 1]);
        Juice savedJuice = juiceRepository.findById(savedUUID).get();

        assertThat(savedJuice).isNotNull();

    }


    @Test
    void patchJuiceErrorTest() throws Exception {
        Juice juice = juiceRepository.findAll().get(0);


        Map<String, Object> juiceMap = new HashMap<>();
        juiceMap.put("juiceName", "New Na1234512345123451234512345123451234512345123451234512345me123451234512312345123451234512345123451234512345123451234545123451234512345123451234512345123451234512345");

       MvcResult mvcResult= mockMvc.perform(patch(JuiceController.JUICE_PATH_WITH_ID, juice.getId())
                       .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(juiceMap)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    @Test
    void getJuiceByName() throws Exception{
        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceName", "IPA")
                .queryParam("size", "800"))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(336)))
                ;

    }
    @Test
    void getJuiceByStyle() throws Exception{
        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceStyle", JuiceStyle.IPA.name())
                .queryParam("size", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(547)))
        ;

    }
    @Test
    void getJuiceByNameAndStyle() throws Exception{

        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceName", "IPA")
                        .queryParam("juiceStyle", JuiceStyle.IPA.name())
                .queryParam("size", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(310)))
        ;

    }
    @Test
    void getJuiceByNameAndStyleAndCheckInventoryIsNotNull() throws Exception{

        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceName", "IPA")
                        .queryParam("juiceStyle", JuiceStyle.IPA.name())
                        .queryParam("size", "800")
                        .queryParam("showInventory","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(310)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand",is(IsNull.notNullValue())))
        ;

    }
    @Test
    void getJuiceByNameAndStyleAndCheckInventoryIsNull() throws Exception{

        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceName", "IPA")
                        .queryParam("juiceStyle", JuiceStyle.IPA.name())
                        .queryParam("size", "800")
                        .queryParam("showInventory","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(310)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand",is(IsNull.nullValue())))
        ;

    }
    @Test
    void getJuicesOnPageTwo() throws Exception{

        mockMvc.perform(get(JuiceController.JUICE_PATH)
                        .with(jwtRequestPostProcessor)
                        .queryParam("juiceName", "IPA")
                        .queryParam("juiceStyle", JuiceStyle.IPA.name())
                        .queryParam("showInventory","false")
                        .queryParam("pageNumber", "2")
                        .queryParam("size", "50"))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()",is(50)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand",is(IsNull.nullValue())))
        ;

    }



}