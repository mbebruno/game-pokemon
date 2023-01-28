package com.game.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.model.dto.RoundDto;
import com.game.model.request.AttackRequest;
import com.game.service.AttackService;
import com.game.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AttackController.class)
 class AttackControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AttackService attackService;

    @Autowired
     ObjectMapper objectMapper;

    @Test
    @DisplayName("test add launch attack")
    void test_attack() throws Exception {

        AttackRequest attackRequest=AttackRequest.builder().battleId(1L).type("SPECIAL").build();
        List<RoundDto> roundDtos=Arrays.asList(RoundDto.builder().id(1L).build());

       Mockito.when(this.attackService.launchAttack(attackRequest)).thenReturn(roundDtos);

        mockMvc.perform(patch("/attack-management").contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(attackRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(jsonPath("$.status").value(Message.MSG_SUCCES))
                .andExpect(jsonPath("$.results",hasSize(1)));
                //.andExpect(jsonPath("$.results.name").value(gameDto.getName()));
    }


}
