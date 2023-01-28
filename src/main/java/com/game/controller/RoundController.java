package com.game.controller;

import com.game.model.dto.APIResponse;
import com.game.model.dto.BattleDto;
import com.game.model.dto.RoundDto;
import com.game.model.request.BattleRequest;
import com.game.service.BattleService;
import com.game.service.RoundService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("round-management")
@AllArgsConstructor
@Slf4j
public class RoundController {

    private final RoundService roundService;



    @GetMapping
    ResponseEntity<APIResponse<List<RoundDto>>>listOfRounds(){
        APIResponse<List<RoundDto>> apiResponse= APIResponse.<List<RoundDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(this.roundService.listRounds())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
