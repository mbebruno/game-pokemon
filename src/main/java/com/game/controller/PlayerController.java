package com.game.controller;

import com.game.model.dto.APIResponse;
import com.game.model.dto.PlayerDto;
import com.game.model.request.PlayerPokemonRequest;
import com.game.model.request.PlayerRequest;
import com.game.service.PlayerService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("player-management")
@AllArgsConstructor
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    ResponseEntity<APIResponse<PlayerDto>>createNewPlayerOfPlayer(@RequestBody @Valid PlayerRequest playerRequest){
        log.debug("create player , new values : {}...", playerRequest);
        APIResponse<PlayerDto> apiResponse= APIResponse.<PlayerDto>builder()
                .status(Message.MSG_SUCCES)
                .results(this.playerService.createPlayer(playerRequest))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @PatchMapping("/{id}")
    ResponseEntity<APIResponse<PlayerDto>>createLinkToPlayerAndPokemon(@RequestBody @Valid PlayerPokemonRequest playerPokemonRequest
    ,@PathVariable("id")Long id){
        log.debug("add pokemon to player (id = {}), new values : {}...",id, playerPokemonRequest);
        APIResponse<PlayerDto> apiResponse= APIResponse.<PlayerDto>builder()
                .status(Message.MSG_SUCCES)
                .results(this.playerService.addPokemonToPlayer(playerPokemonRequest))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping
    ResponseEntity<APIResponse<List<PlayerDto>>>listOfPlayer(){
        log.debug("getting list of player");
        APIResponse<List<PlayerDto>> apiResponse= APIResponse.<List<PlayerDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(this.playerService.listPlayers())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
