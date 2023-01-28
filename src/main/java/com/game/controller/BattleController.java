package com.game.controller;

import com.game.model.dto.APIResponse;
import com.game.model.dto.BattleDto;
import com.game.model.dto.PlayerDto;
import com.game.model.dto.WinnerDto;
import com.game.model.request.BattleRequest;
import com.game.service.BattleService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("battle-management")
@AllArgsConstructor
@Slf4j
public class BattleController {

    private final BattleService battleService;

    @PostMapping
    ResponseEntity<APIResponse<BattleDto>>createNewGame(@RequestBody @Valid BattleRequest battleRequest){
        APIResponse<BattleDto> apiResponse= APIResponse.<BattleDto>builder()
                .status(Message.MSG_SUCCES)
                .results(battleService.createBattle(battleRequest))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping
    ResponseEntity<APIResponse<List<BattleDto>>>listOfBattle(){
        APIResponse<List<BattleDto>> apiResponse= APIResponse.<List<BattleDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(this.battleService.listBattles())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<APIResponse<WinnerDto>>getWinner(@PathVariable("id") Long id){
        APIResponse<WinnerDto> apiResponse= APIResponse.<WinnerDto>builder()
                .status(Message.MSG_SUCCES)
                .results(this.battleService.findWinner(id))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
