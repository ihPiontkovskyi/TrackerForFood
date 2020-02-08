package ua.foodtracker.service;

import ua.foodtracker.domain.Record;

import java.util.List;
import java.util.Optional;

public interface RecordService {
    List<Record> getRecordsByDate(int userId, String date);

    void add(Record record);

    void delete(Record record);

    void modify(Record record);

    Optional<Record> findById(String id);
}
