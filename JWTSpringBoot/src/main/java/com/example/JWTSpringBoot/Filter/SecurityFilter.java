package com.example.JWTSpringBoot.Filter;

import com.example.JWTSpringBoot.JWTUtility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component

public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        String token = request.getHeader("Authorization");

        if(token!=null){
            //do validation
             String username = jwtUtil.getUSername(token);
   //username should not be null and scgcga must be null
             if(username!=null && SecurityContextHolder
                     .getContext()
                     .getAuthentication()==null)
             {
                 UserDetails usr = userDetailsService.loadUserByUsername(username);

                 //validate token
                  boolean isValid = jwtUtil.ValidateToken(token,usr.getUsername());

                  if(isValid){
                      UsernamePasswordAuthenticationToken authenticationToken =
                              new UsernamePasswordAuthenticationToken
                                      (username,usr.getPassword(),
                                      usr.getAuthorities());
                      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                      //final object
                      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  }

             }

        }

        filterChain.doFilter(request,response);


    }
}
