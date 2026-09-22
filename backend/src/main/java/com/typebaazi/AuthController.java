package com.typebaazi;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import jakarta.servlet.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users;
 private final PasswordEncoder encoder;
 private final HttpSessionSecurityContextRepository contexts;
 private final String dummyHash;
 public AuthController(UserRepository users, PasswordEncoder encoder,
     HttpSessionSecurityContextRepository contexts) {
  this.users=users; this.encoder=encoder; this.contexts=contexts;
  dummyHash=encoder.encode(UUID.randomUUID().toString());
 }
 public record Credentials(String username,String email,String password) {}
 public record Profile(String id,String username,String email) {}
 @GetMapping("/csrf") public Map<String,String> csrf(CsrfToken token) {
  return Map.of("token",token.getToken(),"headerName",token.getHeaderName());
 }
 @GetMapping("/me") public Profile me(Authentication auth) {
  AppUser user=users.findById(auth.getName()).orElseThrow(() -> error(401,"Please log in again."));
  return profile(user);
 }
 @PostMapping("/register") public Profile register(@RequestBody Credentials input,
     HttpServletRequest request,HttpServletResponse response) {
  String email=email(input.email());
  String name=input.username()==null ? "" : input.username().trim();
  if(name.length()<2 || name.length()>20) throw error(400,"Username must be 2–20 characters.");
  if(input.password()==null || input.password().length()<8 ||
     input.password().getBytes(StandardCharsets.UTF_8).length>72)
   throw error(400,"Use a password of at least 8 characters, at most 72 UTF-8 bytes.");
  AppUser user=new AppUser();
  user.id=UUID.randomUUID().toString(); user.username=name; user.email=email;
  user.passwordHash=encoder.encode(input.password()); user.createdAt=Instant.now();
  try { users.saveAndFlush(user); }
  catch(DataIntegrityViolationException ex) { throw error(409,"That email already has an account. Please log in."); }
  return signIn(user,request,response);
 }
 @PostMapping("/login") public Profile login(@RequestBody Credentials input,
     HttpServletRequest request,HttpServletResponse response) {
  String email=email(input.email());
  if(input.password()==null || input.password().getBytes(StandardCharsets.UTF_8).length>72)
   throw error(401,"Email or password is incorrect.");
  AppUser user=users.findByEmail(email).orElse(null);
  boolean matches=encoder.matches(input.password(),user==null ? dummyHash : user.passwordHash);
  if(user==null || !matches) throw error(401,"Email or password is incorrect.");
  return signIn(user,request,response);
 }
 private Profile signIn(AppUser user,HttpServletRequest req,HttpServletResponse res) {
  HttpSession old=req.getSession(false);
  if(old!=null) old.invalidate();
  req.getSession(true).setMaxInactiveInterval(7200);
  var context=SecurityContextHolder.createEmptyContext();
  context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
   user.id,null,List.of(new SimpleGrantedAuthority("ROLE_USER"))));
  SecurityContextHolder.setContext(context);
  contexts.saveContext(context,req,res);
  return profile(user);
 }
 private Profile profile(AppUser u) {return new Profile(u.id,u.username,u.email);}
 private String email(String value) {
  String email=value==null ? "" : value.trim().toLowerCase(Locale.ROOT);
  if(email.length()>254 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
   throw error(400,"Enter a valid email address.");
  return email;
 }
 private ResponseStatusException error(int status,String text) {
  return new ResponseStatusException(HttpStatus.valueOf(status),text);
 }
}
