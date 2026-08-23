package br.com.pronova.prodsync.application.dtos;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CsvRecordDTO {

    @CsvBindByName(column = "Colaborador", required = true)
    private String colaborador;

    @CsvBindByName(column = "Tarefa", required = true)
    private String tarefa;

    @CsvBindByName(column = "Quantidade", required = true)
    private Double quantidade;

    @CsvBindByName(column = "DataHora", required = true)
    private String dataHora;

}
