package com.card_management_system.card_management_system.config;

import com.card_management_system.card_management_system.dto.CardResponseDTO;
import com.card_management_system.card_management_system.model.Card;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        configureCardMappings(modelMapper);

        return modelMapper;
    }

    private void configureCardMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Card.class, CardResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getAccount().getId(),
                            CardResponseDTO::setAccountId);
                });
    }
}