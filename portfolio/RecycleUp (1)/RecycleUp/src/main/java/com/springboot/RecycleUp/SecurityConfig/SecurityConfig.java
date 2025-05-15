package com.springboot.RecycleUp.SecurityConfig;

import com.springboot.RecycleUp.Util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService){
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable()) //not needed for stateless JWT
                .formLogin(form -> form.disable()) //unnecessary right now
                //define endpoint accessibility
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/RecycleUp/auth/**").permitAll()

                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/RecycleUp/log-out")
                        .deleteCookies("jwt")
                        .logoutSuccessUrl("/RecycleUp//auth/log-in?log-out"))
                //explicitly define stateless session (needed for JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider((authenticationProvider())) //custom auth provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //JWT filter before default filter
        return http.build();
    }

    //configures password encryption
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    //links user details and password encoder
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    //needed for programmatic auth (like JWT)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
