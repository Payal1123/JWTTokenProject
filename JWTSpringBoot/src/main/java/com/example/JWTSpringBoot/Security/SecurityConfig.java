package com.example.JWTSpringBoot.Security;

import com.example.JWTSpringBoot.Filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {
     @Autowired
      private UserDetailsService userDetailsService;

     @Autowired
     private BCryptPasswordEncoder bCryptPasswordEncoder;

     @Autowired
     private InvalidUserAuthenticationPoint authenticationentryPoint;

     @Autowired
     private SecurityFilter securityFilter;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager()
            throws Exception
    {
        return super.authenticationManager();
    }

    @Override

    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
           auth
               .userDetailsService(userDetailsService)
               .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/user/save","/user/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationentryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //second request
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        ;


    }
}
