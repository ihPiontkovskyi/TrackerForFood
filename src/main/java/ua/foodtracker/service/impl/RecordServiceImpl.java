package ua.foodtracker.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foodtracker.domain.Meal;
import ua.foodtracker.domain.Record;
import ua.foodtracker.domain.User;
import ua.foodtracker.domain.dto.DailySums;
import ua.foodtracker.domain.dto.HomeModel;
import ua.foodtracker.entity.RecordEntity;
import ua.foodtracker.entity.RoleEntity;
import ua.foodtracker.repository.RecordRepository;
import ua.foodtracker.service.RecordService;
import ua.foodtracker.service.exception.IncorrectDataException;
import ua.foodtracker.service.mapper.impl.RecordMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    @Override
    public List<Record> getRecordsByDate(User user, String date) {
        if (date == null) {
            return Collections.emptyList();
        }
        try {
            return recordRepository.findAllByUserIdAndDate(user.getId(), LocalDate.parse(date)).stream()
                    .map(recordMapper::mapToDomain)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException ex) {
            //log
            return recordRepository.findAllByUserIdAndDate(user.getId(), LocalDate.now()).stream()
                    .map(recordMapper::mapToDomain)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void add(Record record) {
        recordRepository.save(recordMapper.mapToEntity(record));
    }

    @Override
    public void delete(String id, User user) {
        Optional<RecordEntity> entity = findByStringParam(id, recordRepository::findById);
        if (entity.isPresent()) {
            if (entity.get().getUser().getRole() == RoleEntity.ADMIN
                    || entity.get().getUser().getId().equals(user.getId())) {
                recordRepository.delete(entity.get());
                return;
            } else {
                throw new IncorrectDataException("access.denied");
            }
        }
        throw new IncorrectDataException("incorrect.data");
    }

    @Override
    public void modify(Record record) {
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
    public HomeModel prepareHomeModel(User user) {
        DailySums dailySums = calculateDailySums(user, LocalDate.now().toString());
        int dailyEnergyGoal = Math.min((int) (((double) dailySums.getSumEnergy() / user.getUserGoal().getDailyEnergyGoal()) * 100), 100);
        int dailyProteinGoal = Math.min((int) (((double) dailySums.getSumProtein() / user.getUserGoal().getDailyProteinGoal()) * 100), 100);
        int dailyFatGoal = Math.min((int) (((double) dailySums.getSumFat() / user.getUserGoal().getDailyFatGoal()) * 100), 100);
        int dailyCarbohydratesGoal = Math.min((int) (((double) dailySums.getSumCarbohydrates() / user.getUserGoal().getDailyCarbohydrateGoal()) * 100), 100);
        int dailyWaterGoal = Math.min((int) (((double) dailySums.getSumWater() / user.getUserGoal().getDailyWaterGoal()) * 100), 100);

        List<String> labels = new ArrayList<>();
        List<Integer> energyWeeklyStat = new ArrayList<>();
        List<Integer> proteinWeeklyStat = new ArrayList<>();
        List<Integer> fatWeeklyStat = new ArrayList<>();
        List<Integer> carbWeeklyStat = new ArrayList<>();
        List<Integer> waterWeeklyStat = new ArrayList<>();

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd.MM");
        while (!start.isAfter(end)) {
            labels.add(dtf2.format(start));
            dateList.add(start);
            start = start.plusDays(1);
        }
        for (LocalDate d : dateList) {
            DailySums ds = calculateDailySums(user, d.toString());
            energyWeeklyStat.add(ds.getSumEnergy());
            proteinWeeklyStat.add(ds.getSumProtein());
            fatWeeklyStat.add(ds.getSumFat());
            carbWeeklyStat.add(ds.getSumCarbohydrates());
            waterWeeklyStat.add(ds.getSumWater());
        }
        return HomeModel.builder()
                .dsto(dailySums)
                .carbWeeklyStat(carbWeeklyStat)
                .dailyCarbohydratesGoal(dailyCarbohydratesGoal)
                .dailyEnergyGoal(dailyEnergyGoal)
                .dailyFatGoal(dailyFatGoal)
                .dailyProteinGoal(dailyProteinGoal)
                .dailyWaterGoal(dailyWaterGoal)
                .energyWeeklyStat(energyWeeklyStat)
                .fatWeeklyStat(fatWeeklyStat)
                .labels(labels)
                .proteinWeeklyStat(proteinWeeklyStat)
                .waterWeeklyStat(waterWeeklyStat)
                .build();
    }
}
