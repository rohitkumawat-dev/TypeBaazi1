package com.typebaazi;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
 private static final Logger LOG=LoggerFactory.getLogger(RoomController.class);
 private final RaceResultRepository results;
 private final UserRepository users;
 private final Map<String,Room> rooms=new ConcurrentHashMap<>();
 public RoomController(RaceResultRepository results,UserRepository users) {
  this.results=results; this.users=users;
 }
 public record Settings(int durationSeconds) {}
 public record Progress(String text,long sequence) {}
 public record Session(String code) {}
 public record PlayerView(String name,boolean you,boolean host,boolean ready,
  Double wpm,Double accuracy,Integer mistakes) {}
 public record RoomView(String code,String status,long serverTime,long startsAt,long endsAt,
  int durationSeconds,String paragraph,List<PlayerView> players,String winner,String winnerId,
  String yourText,long yourSequence,boolean resultSaved) {}
 private static class Player {
  final String id,name; boolean ready; String text=""; long sequence=-1;
  Player(AppUser user){id=user.id;name=user.username;}
 }
 private static class Room {
  final String code,id=UUID.randomUUID().toString();
  final List<Player> players=new ArrayList<>();
  int duration; long startsAt,endsAt,lastTouched=System.currentTimeMillis();
  String paragraph=""; boolean saved;
  Room(String code,int duration){this.code=code;this.duration=duration;}
 }
 private ResponseStatusException fail(int status,String message) {
  return new ResponseStatusException(HttpStatus.valueOf(status),message);
 }
 private void duration(int seconds) {
  if(!Set.of(30,60,90,120).contains(seconds)) throw fail(400,"Choose 30, 60, 90 or 120 seconds.");
 }
 private Room find(String code) {
  Room r=rooms.get(code.toUpperCase(Locale.ROOT));
  if(r==null) throw fail(404,"Room expired. Create a new room.");
  return r;
 }
 private Player member(Room r,String id) {
  return r.players.stream().filter(p->p.id.equals(id)).findFirst()
   .orElseThrow(()->fail(403,"You are not a player in this room."));
 }
 private void host(Room r,String id) {
  member(r,id);
  if(!r.players.get(0).id.equals(id)) throw fail(403,"Only the room creator can do that.");
 }
 private void waiting(Room r) {
  if(r.startsAt!=0) throw fail(409,"The race has already started.");
 }
 private AppUser user(Authentication auth) {
  return users.findById(auth.getName()).orElseThrow(()->fail(401,"Please log in again."));
 }
 private RaceScoring.Score score(Room r,Player p) {
  return RaceScoring.score(r.paragraph,p.text,r.duration);
 }
 private String winnerId(Room r) {
  int a=score(r,r.players.get(0)).correct(),b=score(r,r.players.get(1)).correct();
  return a==b?null:r.players.get(a>b?0:1).id;
 }
 private String winner(Room r) {
  String id=winnerId(r);
  return id==null?"It's a draw!":member(r,id).name+" wins!";
 }
 // Also called by the scheduler: browser closure must not prevent saving.
 private void finish(Room r) {
  if(r.saved || r.startsAt==0 || System.currentTimeMillis()<r.endsAt) return;
  Player a=r.players.get(0), b=r.players.get(1);
  var sa=score(r,a); var sb=score(r,b);
  RaceResult result=new RaceResult();
  result.id=r.id;result.roomCode=r.code;
  result.playerOne=a.name;result.playerTwo=b.name;
  result.playerOneId=a.id;result.playerTwoId=b.id;
  result.playerOneWpm=sa.wpm();result.playerTwoWpm=sb.wpm();
  result.playerOneAccuracy=sa.accuracy();result.playerTwoAccuracy=sb.accuracy();
  result.playerOneMistakes=sa.mistakes();result.playerTwoMistakes=sb.mistakes();
  result.winnerId=winnerId(r);result.winner=winner(r);
  result.durationSeconds=r.duration;result.paragraphTitle="Random passage mix";
  result.finishedAt=Instant.ofEpochMilli(r.endsAt);
  results.saveAndFlush(result);r.saved=true;
 }
 @Scheduled(fixedDelay=1000)
 public void maintain() {
  for(Room r:rooms.values()) synchronized(r) {
   try {finish(r);} catch(RuntimeException ex) {LOG.warn("Race result save will be retried for {}",r.code);}
   long now=System.currentTimeMillis();
   if((r.startsAt==0 && now-r.lastTouched>7200000L) ||
      (r.saved && now-r.endsAt>3600000L)) rooms.remove(r.code,r);
  }
 }
 private RoomView view(Room r,String id) {
  Player me=member(r,id); long now=System.currentTimeMillis();r.lastTouched=now;
  boolean done=r.startsAt>0 && now>=r.endsAt;
  if(done) {try {finish(r);} catch(RuntimeException ex) {LOG.warn("Race save pending for {}",r.code);}}
  String status=r.startsAt==0?"WAITING":now<r.startsAt?"COUNTDOWN":done?"FINISHED":"RACING";
  List<PlayerView> list=new ArrayList<>();
  for(Player p:r.players) {
   var s=score(r,p);
   list.add(new PlayerView(p.name,p.id.equals(id),p==r.players.get(0),p.ready,
    done?s.wpm():null,done?s.accuracy():null,done?s.mistakes():null));
  }
  return new RoomView(r.code,status,now,r.startsAt,r.endsAt,r.duration,
   now>=r.startsAt && r.startsAt>0?r.paragraph:"",list,done?winner(r):null,
   done?winnerId(r):null,me.text,me.sequence,r.saved);
 }
 @PostMapping public Session create(@RequestBody Settings settings,Authentication auth) {
  duration(settings.durationSeconds());AppUser user=user(auth);Room r;
  do {
   r=new Room(UUID.randomUUID().toString().substring(0,6).toUpperCase(Locale.ROOT),settings.durationSeconds());
   r.players.add(new Player(user));
  } while(rooms.putIfAbsent(r.code,r)!=null);
  return new Session(r.code);
 }
 @PostMapping("/{code}/join") public Session join(@PathVariable String code,Authentication auth) {
  Room r=find(code);AppUser u=user(auth);
  synchronized(r) {
   if(r.players.stream().anyMatch(p->p.id.equals(u.id))) return new Session(r.code);
   waiting(r);
   if(r.players.size()>=2) throw fail(409,"Room already has two players.");
   r.players.add(new Player(u));r.lastTouched=System.currentTimeMillis();return new Session(r.code);
  }
 }
 @GetMapping("/{code}") public RoomView get(@PathVariable String code,Authentication auth) {
  Room r=find(code);synchronized(r){return view(r,auth.getName());}
 }
 @PostMapping("/{code}/settings") public RoomView settings(@PathVariable String code,
     @RequestBody Settings settings,Authentication auth) {
  duration(settings.durationSeconds());Room r=find(code);
  synchronized(r) {
   host(r,auth.getName());waiting(r);r.duration=settings.durationSeconds();
   r.players.forEach(p->p.ready=false);return view(r,auth.getName());
  }
 }
 @PostMapping("/{code}/ready") public RoomView ready(@PathVariable String code,Authentication auth) {
  Room r=find(code);synchronized(r) {
   Player p=member(r,auth.getName());waiting(r);
   if(p==r.players.get(0)) throw fail(409,"The room creator uses Start game.");
   p.ready=true;return view(r,auth.getName());
  }
 }
 @PostMapping("/{code}/start") public RoomView start(@PathVariable String code,Authentication auth) {
  Room r=find(code);synchronized(r) {
   host(r,auth.getName());waiting(r);
   if(r.players.size()!=2 || !r.players.get(1).ready) throw fail(409,"Wait for your friend to click Ready.");
   r.paragraph=Paragraphs.choose(r.players.get(0).id, r.players.get(1).id);r.startsAt=System.currentTimeMillis()+3000;
   r.endsAt=r.startsAt+r.duration*1000L;return view(r,auth.getName());
  }
 }
 @PostMapping("/{code}/progress") public Map<String,Boolean> progress(@PathVariable String code,
     @RequestBody Progress input,Authentication auth) {
  Room r=find(code);synchronized(r) {
   Player p=member(r,auth.getName());long now=System.currentTimeMillis();
   if(r.startsAt==0 || now<r.startsAt || now>=r.endsAt) throw fail(409,"Race is not active.");
   if(input.text()==null || input.text().length()>r.paragraph.length() || input.sequence()<0)
    throw fail(400,"Invalid typing update.");
   if(input.sequence()>p.sequence) {p.text=input.text();p.sequence=input.sequence();}
   return Map.of("saved",true);
  }
 }
 @PostMapping("/{code}/leave") public Map<String,Boolean> leave(@PathVariable String code,Authentication auth) {
  Room r=find(code);synchronized(r) {
   Player p=member(r,auth.getName());
   if(r.startsAt==0) {
    if(p==r.players.get(0)) rooms.remove(r.code,r);
    else {r.players.remove(p);r.players.forEach(other->other.ready=false);}
   }
   return Map.of("left",true);
  }
 }
}
