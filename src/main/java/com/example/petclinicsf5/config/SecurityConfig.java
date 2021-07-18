package com.example.petclinicsf5.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**", "/vets**").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and().httpBasic();
    }

    //todo -> More elegant than using bean with userDetails. {noop} means no password encoding. Other options to be tested later
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("vet").password("{noop}vet").roles("VET")
                .and()
                .withUser("owner").password("{noop}owner").roles("OWNER");
    }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails vetUser = User.withDefaultPasswordEncoder()
//                .username("vet")
//                .password("vet")
//                .roles("VET")
//                .build();
//
//        UserDetails ownerUser = User.withDefaultPasswordEncoder()
//                .username("owner")
//                .password("owner")
//                .roles("OWNER")
//                .build();
//
//        return new InMemoryUserDetailsManager(vetUser, ownerUser);
//    }
}
