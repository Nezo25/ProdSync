package br.com.pronova.prodsync.application.services;

import br.com.pronova.prodsync.application.dtos.CsvRecordDTO;
import br.com.pronova.prodsync.domain.entities.Colaborador;
import br.com.pronova.prodsync.domain.entities.RegistroProdutividade;
import br.com.pronova.prodsync.domain.entities.TipoAtividade;
import br.com.pronova.prodsync.domain.repositories.ColaboradorRepository;
import br.com.pronova.prodsync.domain.repositories.RegistroProdutividadeRepository;
import br.com.pronova.prodsync.domain.repositories.TipoAtividadeRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final RegistroProdutividadeRepository registroRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TipoAtividadeRepository tipoAtividadeRepository;

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "metricas_produtividade", allEntries = true)
    public void processarCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<CsvRecordDTO> csvToBean = new CsvToBeanBuilder<CsvRecordDTO>(reader)
                    .withType(CsvRecordDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();

            List<CsvRecordDTO> records = csvToBean.parse();
            List<RegistroProdutividade> registrosParaSalvar = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            java.util.Set<java.time.LocalDate> datasParaLimpar = new java.util.HashSet<>();
            for (CsvRecordDTO record : records) {
                LocalDateTime dh = LocalDateTime.parse(record.getDataHora(), formatter);
                datasParaLimpar.add(dh.toLocalDate());
            }
            for (java.time.LocalDate data : datasParaLimpar) {
                registroRepository.deleteByDataHoraBetween(data.atStartOfDay(), data.atTime(23, 59, 59));
            }

            java.util.Map<String, Colaborador> colabCache = new java.util.HashMap<>();
            java.util.Map<String, TipoAtividade> ativCache = new java.util.HashMap<>();

            for (CsvRecordDTO record : records) {
                Colaborador colaborador = colabCache.computeIfAbsent(record.getColaborador(), nome -> 
                    colaboradorRepository.findFirstByNome(nome)
                        .orElseGet(() -> colaboradorRepository.save(Colaborador.builder().nome(nome).build()))
                );

                TipoAtividade atividade = ativCache.computeIfAbsent(record.getTarefa(), nome -> 
                    tipoAtividadeRepository.findFirstByNome(nome)
                        .orElseGet(() -> tipoAtividadeRepository.save(TipoAtividade.builder().nome(nome).unidadeMedida("UN").build()))
                );

                LocalDateTime dataHora = LocalDateTime.parse(record.getDataHora(), formatter);

                registrosParaSalvar.add(RegistroProdutividade.builder()
                        .colaborador(colaborador)
                        .tipoAtividade(atividade)
                        .quantidadePrimaria(record.getQuantidade())
                        .dataHora(dataHora)
                        .faixaHoraria(dataHora.getHour())
                        .build());
            }

            registroRepository.saveAll(registrosParaSalvar);

        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg += " | Cause: " + e.getCause().getMessage();
                if (e.getCause().getCause() != null) {
                    errorMsg += " | Root: " + e.getCause().getCause().getMessage();
                }
            }
            throw new br.com.pronova.prodsync.exceptions.CsvProcessingException("Erro ao processar arquivo CSV: " + errorMsg, e);
        }
    }
}




