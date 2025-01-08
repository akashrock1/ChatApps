package com.example.ChatApp.configuration;


import com.example.ChatApp.filters.JwtAuthFilter;
import com.example.ChatApp.handler.Oauth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//By default Secured() is set to false
@EnableMethodSecurity(securedEnabled = true)
public class webSecurityConfig  {

    @Autowired
private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private Oauth2SuccessHandler oauth2SuccessHandler;

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    System.out.println("inside the security filer");
    httpSecurity.authorizeHttpRequests(auth->
            auth.requestMatchers("/error","/login","/home.html").permitAll()
                    .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                    // instead we have used Pre Authorize in Post Method
//                    .requestMatchers("/post/add").hasAuthority("CREATE_POST")

                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
/*
 Uncomment it when we have to use Auth 2.0 and add client id in application.yml

                .oauth2Login(oauthConfig->oauthConfig
                    .failureUrl("/login?error=true")
                    .successHandler(oauth2SuccessHandler))

*/
            ;
    return httpSecurity.build();
}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

}
