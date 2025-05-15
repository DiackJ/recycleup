package com.springboot.RecycleUp.Util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//grabs jwt from request header and validates it
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil){
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    //validates cookie and sets authentication in security context
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        //skip public paths
        String path = request.getServletPath();
        if(path.startsWith("/RecycleUp/auth/")){
            filterChain.doFilter(request, response);
            System.out.println("skipping");
            return;
        }

        String token = null;
        String username = null;

            if(request.getCookies() != null){
                for(Cookie cookie : request.getCookies()){
                    if(cookie.getName().equals("jwt")){
                        token = cookie.getValue();
                        username = jwtUtil.extractEmail(token);

                    }
                }
            }

        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("authentication set:" + SecurityContextHolder.getContext().getAuthentication());
            }
        }
        //debugging logs
        System.out.println("request path: " + request.getServletPath());
        System.out.println("token from cookie: " + token);
        System.out.println("authentication set:" + SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request,response);
    }
}
