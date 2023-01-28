package com.game.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.conf.ApplicationProperties;
import com.game.model.dto.PokemonDto;
import com.game.model.dto.PokemonSummary;
import com.game.service.PokemonRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;


@Component
@RequiredArgsConstructor
public class PokemonRemoteServiceImpl implements PokemonRemoteService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper;

    /**
     *
     * Get list of pokemon
     * @return : list of pokemon
     */
    @Override
    public PokemonSummary getListOfPokemonFromRemote() throws JsonProcessingException {

        final String url = applicationProperties.getPokeapiList();

        return objectMapper.readValue(
                restTemplate.exchange(url, GET, null, String.class).getBody(),
                PokemonSummary.class);
    }

    /**
     *
     * Get details of one pokemon
     * @param url of pokemon
     * @return : details on pokemon
     */
    @Override
    public PokemonDto getOnePokemonFromRemote(String url) throws JsonProcessingException {

        return objectMapper.readValue(
                restTemplate.exchange(url, GET, null, String.class).getBody(),
                PokemonDto.class);
    }

}
