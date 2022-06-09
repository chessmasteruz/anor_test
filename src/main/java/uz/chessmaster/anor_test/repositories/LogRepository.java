package uz.chessmaster.anor_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.chessmaster.anor_test.models.LogEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

    public List<LogEntity> findAllByVehOwnerNameAndVehRegNumAndDateBetweenOrderByDateAscOdometerBeginAsc(String vehOwnerName, String regNum, Date begin, Date end);

    public List<LogEntity> findAllByOrderByDateAscOdometerBeginAsc();

}
