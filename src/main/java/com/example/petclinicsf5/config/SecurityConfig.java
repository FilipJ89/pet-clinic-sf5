package com.example.petclinicsf5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // for H2 console free access
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**", "/vets**").permitAll()
                            .mvcMatchers("/owners/**").hasAnyRole("VET", "OWNER")
                            .mvcMatchers("/**").hasRole("ADMIN");
                })

                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and().httpBasic();

        // H2 console frame config
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return OwnPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("MichaelW89").password("{bcrypt15}$2y$15$4AOBk5wLL1gBR4ZxpgHS1emX2vzY1TaXXgqZdiM3z8xOYfKnZ9qkK").roles("OWNER")
//                .and()
//                .withUser("FioGle").password("{noop}password").roles("OWNER")
//                .and()
//                .withUser("admin").password("{noop}admin").roles("ADMIN");
//    }
}
