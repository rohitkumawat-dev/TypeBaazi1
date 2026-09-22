package com.typebaazi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "race_results")
public class RaceResult {

    @Id
    public String id;

    public String roomCode;
    // Nullable additions preserve existing guest race rows.
    public String playerOneId;
    public String playerTwoId;
    public String winnerId;
    public Integer durationSeconds;
    public String paragraphTitle;
    public String playerOne;
    public String playerTwo;

    public double playerOneWpm;
    public double playerTwoWpm;

    public double playerOneAccuracy;
    public double playerTwoAccuracy;

    public int playerOneMistakes;
    public int playerTwoMistakes;

    public String winner;
    public Instant finishedAt;

    public RaceResult() {}
}
