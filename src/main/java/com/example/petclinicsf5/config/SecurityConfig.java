package com.example.petclinicsf5.config;

import com.example.petclinicsf5.config.google2fa.Google2faFilter;
import com.example.petclinicsf5.model.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final Google2faFilter google2faFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // for H2 console free access
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**", "/vets**").permitAll();
                })

                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginProcessingUrl("/login")
                        .loginPage("/login").permitAll()
                        .successForwardUrl("/")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error"))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                        .logoutSuccessUrl("/?success")
                        .permitAll())
                .httpBasic()
                .and().csrf().ignoringAntMatchers("/h2-console/**")
                .and().rememberMe()
                        .tokenRepository(persistentTokenRepository)
                        .userDetailsService(userDetailsService);

                // Impl for hashed remember-me token:
//                        .key("secretsuperkey")
//                        .tokenValiditySeconds(604800)
//                        .userDetailsService(userDetailsService);

        // H2 console frame config
        http.headers().frameOptions().sameOrigin();

        // 2FA filter
        http.addFilterBefore(google2faFilter, SessionManagementFilter.class);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return OwnPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
