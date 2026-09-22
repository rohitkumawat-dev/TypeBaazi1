package com.typebaazi;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RaceResultRepository extends JpaRepository<RaceResult,String> {
 List<RaceResult> findByPlayerOneIdOrPlayerTwoIdOrderByFinishedAtDesc(String first,String second);
}
