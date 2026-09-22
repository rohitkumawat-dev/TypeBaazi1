package com.typebaazi;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
@Configuration
@EnableScheduling
public class SecurityConfig {
 @Bean org.springframework.security.core.userdetails.UserDetailsService userDetailsService(UserRepository users) {
  return email -> {
   AppUser user=users.findByEmail(email).orElseThrow(() ->
    new org.springframework.security.core.userdetails.UsernameNotFoundException("Account not found"));
   return org.springframework.security.core.userdetails.User.withUsername(user.id)
    .password(user.passwordHash).roles("USER").build();
  };
 }
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean HttpSessionSecurityContextRepository contextRepository() {
  return new HttpSessionSecurityContextRepository();
 }
 @Bean SecurityFilterChain security(HttpSecurity http,
     HttpSessionSecurityContextRepository repository) throws Exception {
  return http
   .authorizeHttpRequests(a -> a
    .requestMatchers("/api/auth/csrf", "/api/auth/login", "/api/auth/register").permitAll()
    .requestMatchers("/api/**").authenticated().anyRequest().permitAll())
   .securityContext(c -> c.securityContextRepository(repository))
   .requestCache(c -> c.disable())
   .formLogin(c -> c.disable()).httpBasic(c -> c.disable())
   .logout(c -> c.logoutUrl("/api/auth/logout").deleteCookies("JSESSIONID")
    .logoutSuccessHandler((req,res,auth) -> res.setStatus(204)))
   .exceptionHandling(e -> e
    .authenticationEntryPoint((req,res,ex) -> res.sendError(401))
    .accessDeniedHandler((req,res,ex) -> res.sendError(403)))
   .build();
 }
}
