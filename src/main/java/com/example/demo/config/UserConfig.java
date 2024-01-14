package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableScheduling
@Component
public class UserConfig {


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserConfigService();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( author ->
                            author.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                  .requestMatchers("/img/**").permitAll()
                                  .requestMatchers("/admin-update-product/","/admin-products","/admin-dashboard","/admin-add-product", "/admin-orders-pending", "/admin-orders-completed","/admin-feedbacks").hasAuthority("ADMIN")
                                  .requestMatchers("/my-account", "/checkoutREG").hasAuthority("CUSTOMER")
                                  .requestMatchers("/homepage", "/register", "/register-new","/shoping-cart", "/checkout","/checkoutdata","/place-order", "/contact", "/forgot-password", "/reset-password","/forgotPass", "/checkForgotPass","/login?error","/do-login", "/updatePass", "/searchProduct", "/checkExistProductInCart").permitAll()

                                .anyRequest().authenticated()

                )
                .formLogin(login ->
                        login.loginPage("/login")
                                .loginProcessingUrl("/do-login")
                                .defaultSuccessUrl("/homepage", true)

                                .permitAll()

                )
                .logout(logout ->
                        logout.invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/homepage")
                                .permitAll()
                )
                .authenticationManager(authenticationManager)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
        ;
        return http.build();
    }

}
