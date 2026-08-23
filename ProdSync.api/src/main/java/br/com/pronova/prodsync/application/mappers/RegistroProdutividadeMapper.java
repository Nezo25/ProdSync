package br.com.pronova.prodsync.application.mappers;

import br.com.pronova.prodsync.application.dtos.WebhookPayloadDTO;
import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroProdutividadeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaborador", ignore = true)
    @Mapping(target = "tipoAtividade", ignore = true)
    @Mapping(target = "faixaHoraria", expression = "java(dto.getDataHora().getHour())")
    RegistroProdutividade toEntity(WebhookPayloadDTO dto);

}
