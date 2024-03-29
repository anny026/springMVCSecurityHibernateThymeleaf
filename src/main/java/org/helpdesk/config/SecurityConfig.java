package org.helpdesk.config;

import org.helpdesk.service.security.AuthProviderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        PasswordEncoder pass = new BCryptPasswordEncoder(4);
        String passStr = pass.encode("spring");
        System.out.println("//////getEncoder");
        System.out.println("***************");
        System.out.println(passStr);
        return new BCryptPasswordEncoder(4);
    }
     @Autowired
    public AuthProviderAdapter authProviderAdapter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http    .csrf().disable()
               .authorizeRequests()
               .antMatchers("/login")
               .permitAll()
               .and()
               .authorizeRequests()
               .antMatchers("/tickets/**")   //
               .hasAnyRole("EMPLOYEE", "MANAGER", "ENGINEER" )             //
               .anyRequest()
               .authenticated()
               .and()
               .formLogin()
//                .loginPage("/login")         //
               .loginProcessingUrl("/login")             //
               .failureUrl("/404")                         //
               .defaultSuccessUrl("/tickets", true)
//                .successForwardUrl("/success")
//                .passwordParameter("password")           //
//                .usernameParameter("email")            //
               .permitAll()
               .and()
               .logout()
               .logoutSuccessUrl("/") //??
               .logoutUrl("/logout")
               .invalidateHttpSession(Boolean.TRUE)
               .permitAll()
               .and()
               .sessionManagement()
               .invalidSessionUrl("/login")
               .maximumSessions(1)
               .expiredUrl("/login");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProviderAdapter);
    }

}
