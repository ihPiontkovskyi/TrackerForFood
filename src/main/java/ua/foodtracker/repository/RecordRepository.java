package ua.foodtracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foodtracker.entity.RecordEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Integer> {
    List<RecordEntity> findAllByUserIdAndDate(int id, LocalDate date);
}
