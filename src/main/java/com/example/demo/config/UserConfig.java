package com.example.demo.config;

import com.example.demo.Demo2Application;
import com.example.demo.Respories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                                  .requestMatchers("/admin-update-product/","/admin-products","/admin-dashboard","/admin-add-product", "/admin-orders-pending", "/admin-orders-completed").hasAuthority("ADMIN")
                                  .requestMatchers("/my-account", "/checkoutREG").hasAuthority("CUSTOMER")
                                  .requestMatchers("/homepage", "/register", "/register-new","/shoping-cart", "/checkout","/checkoutdata","/place-order").permitAll()

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
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    // Xóa thông tin principal sau khi đăng xuất thành công
                                    SecurityContextHolder.clearContext();
                                    response.sendRedirect("/homepage");
                                })
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
