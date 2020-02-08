package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Record;
import ua.foodtracker.repository.RecordRepository;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.utility.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.utility.Mapper.mapRecordDomainToRecordEntity;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    @Override
    public List<Record> getRecordsByDate(int userId, String date) {
        if (date == null) {
            return Collections.emptyList();
        }
        try {
            return recordRepository.findAllByUserIdAndDate(userId, LocalDate.parse(date)).stream()
                    .map(Mapper::mapRecordEntityToRecordDomain)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException ex) {

            return recordRepository.findAllByUserIdAndDate(userId, LocalDate.now()).stream()
                    .map(Mapper::mapRecordEntityToRecordDomain)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void add(Record record) {
        recordRepository.save(mapRecordDomainToRecordEntity(record));
    }

    @Override
    public void delete(Record record) {
        recordRepository.delete(mapRecordDomainToRecordEntity(record));
    }

    @Override
    public void modify(Record record) {
        recordRepository.save(mapRecordDomainToRecordEntity(record));
    }

    @Override
    public Optional<Record> findById(String id) {
        return findByStringParam(id, recordRepository::findById).map(Mapper::mapRecordEntityToRecordDomain);
    }
}
