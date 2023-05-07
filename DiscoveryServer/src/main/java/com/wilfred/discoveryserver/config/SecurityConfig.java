package com.wilfred.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${eureka.username}")
    private String username;
    @Value("${eureka.password}")
    private String password;


    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails userDetails = User.withDefaultPasswordEncoder().username(username).
                password(password).authorities("USER").build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername(username)
                .password(password)
                .roles("USER")
                .build());
        return manager;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        /*return http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic().and().build();
*/
        http
                .securityMatcher(antMatcher("/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antMatcher("/user/**")).hasRole("USER")
                        .requestMatchers(regexMatcher("/admin/.*")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());
        return http.build();
    }


}
