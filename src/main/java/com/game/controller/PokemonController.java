package com.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.model.dto.APIResponse;
import com.game.model.dto.PlayerDto;
import com.game.model.dto.PokemonDto;
import com.game.model.dto.PokemonSummary;
import com.game.model.request.PlayerRequest;
import com.game.model.request.PokemonRequest;
import com.game.service.PlayerService;
import com.game.service.PokemonService;
import com.game.service.impl.PokemonRemoteServiceImpl;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("pokemon-management")
@AllArgsConstructor
@Slf4j
public class PokemonController {

    private final PokemonService pokemonService;

    @PatchMapping("{id}")
    ResponseEntity<APIResponse<PokemonDto>>configureHPofPokemon(@RequestBody @Valid PokemonRequest pokemonRequest,
                                                                      @PathVariable("id")long id){
        log.debug("Configure pokemon (id = {}), new values : {}...", id, pokemonRequest);
        APIResponse<PokemonDto> apiResponse= APIResponse.<PokemonDto>builder()
                .status(Message.MSG_SUCCES)
                .results(this.pokemonService.configureHealthPoints(pokemonRequest,id))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<APIResponse<List<PokemonDto>>>listOfPokemon(){
        log.debug("getting list of pokemon");
        APIResponse<List<PokemonDto>> apiResponse= APIResponse.<List<PokemonDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(this.pokemonService.listPokemon())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/init")
    ResponseEntity<APIResponse<String>>loadPokemon() throws JsonProcessingException {
        log.debug("load 50 pokemon from remote api ");
        APIResponse<String> apiResponse= APIResponse.<String>builder()
                .status(Message.MSG_SUCCES)
                .results(this.pokemonService.loadPokemonFromRemoteApi())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }



}
