package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.DailySums;
import ua.foodtracker.domain.HomeModel;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Record;
import ua.foodtracker.domain.User;
import ua.foodtracker.entit.RecordEntity;
import ua.foodtracker.exception.AccessDeniedException;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.repository.RecordRepository;
import ua.foodtracker.service.DateProvider;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.mapper.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordServiceImpl implements RecordService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM");
    private static final int WEEKS_COUNT = 1;
    private static final int PERIOD = 1;
    private static final int DAYS_COUNT = 8;
    public static final String INCORRECT_DATA = "incorrect.data";

    private final RecordRepository recordRepository;
    private final Mapper<Record, RecordEntity> recordMapper;
    private final DateProvider dateProvider;

    @Override
    public List<Record> getRecordsByDate(User user, String date) {
        return recordRepository.findAllByUserIdAndDate(user.getId(), dateProvider.parseOrCurrentDate(date))
                .stream()
                .map(recordMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Record record) {
        if (nonNull(record.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        recordRepository.save(recordMapper.mapToEntity(record));
    }

    @Override
    public void delete(String id, User user) {
        RecordEntity entity = findByStringParam(id, recordRepository::findById)
                .orElseThrow(() -> new IncorrectDataException(INCORRECT_DATA));
        if (entity.getUser().getId().equals(user.getId())) {
            recordRepository.delete(entity);
        } else {
            throw new AccessDeniedException("access.denied");
        }
    }

    @Override
    public void modify(Record record) {
        if (isNull(record.getId())) {
            throw new IncorrectDataException(INCORRECT_DATA);
        }
        recordRepository.save(recordMapper.mapToEntity(record));
    }

    @Override
    public Optional<Record> findById(String id) {
        return findByStringParam(id, recordRepository::findById).map(recordMapper::mapToDomain);
    }

    @Override
    public DailySums calculateDailySums(User user, String date) {
        List<Record> records = getRecordsByDate(user, date);
        int energy = 0;
        int protein = 0;
        int fat = 0;
        int carbohydrate = 0;
        int water = 0;
        for (Record record : records) {
            Meal meal = record.getMeal();
            energy += meal.calculateEnergy();
            protein += meal.getProtein();
            fat += meal.getFat();
            carbohydrate += meal.getCarbohydrate();
            water += meal.getWater();
        }

        return DailySums.builder()
                .sumCarbohydrates(carbohydrate)
                .sumEnergy(energy)
                .sumFat(fat)
                .sumProtein(protein)
                .sumWater(water)
                .build();
    }

    @Override
    public HomeModel getHomeModel(User user) {
        DailySums dailySums = calculateDailySums(user, LocalDate.now().toString());
        List<LocalDate> dateList = dateProvider.getLastWeek();
        List<String> labels = dateList.stream()
                .map(DATE_TIME_FORMATTER::format)
                .collect(Collectors.toList());

        Map<String, DailySums> weeklyStat = getStatisticsByDates(user, dateList);

        return HomeModel.builder()
                .dailySums(dailySums)
                .dailyGoal(HomeModel.calculateDailyGoal(dailySums, user.getUserGoal()))
                .labels(labels)
                .weeklyStats(weeklyStat)
                .build();
    }

    private Map<String, DailySums> getStatisticsByDates(User user, List<LocalDate> dates) {
        return dates.stream()
                .collect(Collectors.toMap(DATE_TIME_FORMATTER::format, date -> calculateDailySums(user, date.toString())));
    }
}
