package com.typebaazi;
import java.time.Instant;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/history")
public class HistoryController {
 private final RaceResultRepository results;
 public HistoryController(RaceResultRepository results) {this.results=results;}
 public record Item(String id,String opponent,String outcome,String winner,
  double wpm,double opponentWpm,double accuracy,int mistakes,int durationSeconds,Instant finishedAt) {}
 public record History(long played,long wins,double bestWpm,List<Item> races) {}
 @GetMapping public History history(Authentication auth) {
  String id=auth.getName();
  List<RaceResult> rows=results.findByPlayerOneIdOrPlayerTwoIdOrderByFinishedAtDesc(id,id);
  List<Item> items=new ArrayList<>(); long wins=0; double best=0;
  for(RaceResult r:rows) {
   boolean first=id.equals(r.playerOneId);
   String outcome=r.winnerId==null ? "Draw" : id.equals(r.winnerId) ? "Won" : "Lost";
   if(outcome.equals("Won")) wins++;
   double wpm=first ? r.playerOneWpm : r.playerTwoWpm;
   best=Math.max(best,wpm);
   items.add(new Item(r.id,first?r.playerTwo:r.playerOne,outcome,r.winner,wpm,
    first?r.playerTwoWpm:r.playerOneWpm,first?r.playerOneAccuracy:r.playerTwoAccuracy,
    first?r.playerOneMistakes:r.playerTwoMistakes,r.durationSeconds==null?60:r.durationSeconds,r.finishedAt));
  }
  return new History(rows.size(),wins,best,items);
 }
}
