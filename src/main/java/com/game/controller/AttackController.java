package com.game.controller;

import com.game.model.dto.APIResponse;
import com.game.model.dto.RoundDto;
import com.game.model.request.AttackRequest;
import com.game.service.AttackService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("attack-management")
@AllArgsConstructor
@Slf4j
public class AttackController {

    private final AttackService attackService;

    @PatchMapping
    ResponseEntity<APIResponse<List<RoundDto>>>launchAttack(@RequestBody @Valid AttackRequest attackRequest){
        log.debug("Launch attack , new values : {}...", attackRequest);
        APIResponse<List<RoundDto>> apiResponse= APIResponse.<List<RoundDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(attackService.launchAttack(attackRequest))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);

    }



}
