package com.game.controller;

import com.game.model.dto.APIResponse;
import com.game.model.dto.GameDto;
import com.game.model.request.GameRequest;
import com.game.service.GameService;
import com.game.util.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("game-management")
@AllArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping
    ResponseEntity<APIResponse<GameDto>>createNewGame(@RequestBody @Valid GameRequest gameRequest){
        APIResponse<GameDto> apiResponse= APIResponse.<GameDto>builder()
                .status(Message.MSG_SUCCES)
                .results(gameService.createGame(gameRequest))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping
    ResponseEntity<APIResponse<List<GameDto>>>listOfGame(){
        APIResponse<List<GameDto>> apiResponse= APIResponse.<List<GameDto>>builder()
                .status(Message.MSG_SUCCES)
                .results(gameService.listGames())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
