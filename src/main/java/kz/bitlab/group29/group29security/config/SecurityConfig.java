package kz.bitlab.group29.group29security.config;

import kz.bitlab.group29.group29security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().
                antMatchers("/css/**", "/js/**").permitAll().
                antMatchers("/").permitAll();

        http.formLogin().
                loginProcessingUrl("/auth").permitAll().
                loginPage("/loginpage").permitAll().
                usernameParameter("user_email").
                passwordParameter("user_password").
                defaultSuccessUrl("/").
                failureUrl("/loginpage?error");

        http.logout().permitAll().
                logoutUrl("/tologout").
                logoutSuccessUrl("/loginpage").permitAll();

        http.csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
