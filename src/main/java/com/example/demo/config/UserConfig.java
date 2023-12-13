package com.example.demo.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Configuration

public class UserConfig  {



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder
//                = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//
//
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests( author ->
//                        author.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                                .requestMatchers("/forgot-password", "/register", "/register-new").permitAll()
//                                .anyRequest().authenticated()
//
//                )
//                .formLogin(login ->
//                        login.loginPage("/login")
//                                .loginProcessingUrl("/do-login")
//                                .defaultSuccessUrl("/index", true)
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout.invalidateHttpSession(true)
//                                .clearAuthentication(true)
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                .logoutSuccessUrl("/login?logout")
//                                .permitAll()
//                )
//                .authenticationManager(authenticationManager)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                )
//        ;
//        return http.build();
//    }
}
