package com.example.SiteCercolaFioravante.security;

import com.example.SiteCercolaFioravante.customer.services.CustomerAuthenticationService;
import com.example.SiteCercolaFioravante.exception.DelegatedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebMvc
public class SecurityConfig {

    private final CustomerAuthenticationService customerAuthenticationService;


    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    private final HandlerExceptionResolver resolver;


    public SecurityConfig(
                          @Autowired CustomerAuthenticationService customerAuthenticationService,
                            @Autowired DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint,
                         @Autowired @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolve) {

        this.customerAuthenticationService= customerAuthenticationService;
        this.delegatedAuthenticationEntryPoint = delegatedAuthenticationEntryPoint;
        this.resolver = handlerExceptionResolve;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(delegatedAuthenticationEntryPoint) )
                .cors(cors -> cors.configurationSource(corsFilter()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionMan -> sessionMan.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatchers((matchers) -> matchers.requestMatchers(antMatcher("/api/**"))).
                addFilterBefore(new JWTAuthFilter(customerAuthenticationService,resolver), UsernamePasswordAuthenticationFilter.class);



        ;

        return http.build();
    }

    private CorsConfigurationSource corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedHeaders(Collections.singletonList("*"));

        config.setExposedHeaders( Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**",config);

        return source;

    }
}
