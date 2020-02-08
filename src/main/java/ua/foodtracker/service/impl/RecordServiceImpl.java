package ua.foodtracker.service.impl;

import ua.foodtracker.annotation.Autowired;
import ua.foodtracker.annotation.Service;
import ua.foodtracker.dao.RecordDao;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.domain.Record;
import ua.foodtracker.service.utility.EntityMapper;
import ua.foodtracker.validator.impl.RecordValidator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.foodtracker.service.utility.EntityMapper.mapRecordToEntityRecord;
import static ua.foodtracker.service.utility.ServiceUtility.addByType;
import static ua.foodtracker.service.utility.ServiceUtility.deleteByStringId;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.modifyByType;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private RecordValidator recordValidator;

    @Override
    public List<Record> getRecordsByDate(int userId, String date) {
        LocalDate dateCurrent;
        try {
            dateCurrent = LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            //
            dateCurrent = LocalDate.now();
        }
        return recordDao.findByUserIdAndDate(userId, dateCurrent).stream().map(EntityMapper::mapEntityRecordToRecord).collect(Collectors.toList());
    }

    @Override
    public void add(Record record) {
        addByType(record, recordValidator, obj -> recordDao.save(mapRecordToEntityRecord(obj)));
    }

    @Override
    public void delete(String id) {
        deleteByStringId(id, recordValidator, intId -> recordDao.deleteById(intId));
    }

    @Override
    public void modify(Record record) {
        modifyByType(record, recordValidator, obj -> recordDao.update(mapRecordToEntityRecord(obj)));
    }

    @Override
    public Optional<Record> findById(String id) {
        return findByStringParam(id, recordValidator, intId -> recordDao.findById(intId).map(EntityMapper::mapEntityRecordToRecord));
    }

    @Override
    public void setLocale(Locale locale) {
        recordValidator.setLocale(locale);
    }
}
