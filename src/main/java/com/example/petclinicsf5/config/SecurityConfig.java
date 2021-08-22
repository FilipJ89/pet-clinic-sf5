package com.example.petclinicsf5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // for H2 console free access
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**", "/vets**").permitAll();
//                            .mvcMatchers("/owners/**").hasAnyRole("VET", "OWNER")
//                            .mvcMatchers("/**").hasRole("ADMIN");
                })

                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginProcessingUrl("/login")
                            .loginPage("/login").permitAll()
                            .successForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureUrl("/login?error");
                })
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                            .logoutSuccessUrl("/?success")
                            .permitAll();
                })
                .httpBasic();

        // H2 console frame config
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return OwnPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
