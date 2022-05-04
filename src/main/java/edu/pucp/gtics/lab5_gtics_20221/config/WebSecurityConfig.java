package edu.pucp.gtics.lab5_gtics_20221.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{



        http.formLogin().loginPage("/login").loginProcessingUrl("/processLogin").usernameParameter("correo")
                .defaultSuccessUrl("/redirectByRole",true);
        http.logout().logoutSuccessUrl("/");



    }

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("SELECT correo, password, enabled FROM usuarios WHERE correo = ?")
                .authoritiesByUsernameQuery("SELECT correo, autorizacion FROM usuarios WHERE correo = ? and enabled =1;");

    }

}