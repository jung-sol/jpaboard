package com.example.jpaboard.config;

import com.example.jpaboard.entity.UserRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()            // 인증, 인가가 필요한 URL 지정
                .antMatchers("/user/info", "/board/heart/**").authenticated()  // authenticated() : 해당 URL에 진입하기 위해서 Authentication(인증, 로그인)이 필요함
                .antMatchers("/admin/**").hasAuthority(UserRole.ADMIN.name())  // hasAuthority() : 해당 URL에 진입하기 위해서 Authorization(인가, ex)권한이 ADMIN인 유저만 진입 가능)이 필요함, URL에 ** 사용 : ** 위치에 어떤 값이 들어와도 적용시킴
                .anyRequest().permitAll()       // permitAll() : Authentication, Authorization 필요 없이 통과
                .and()
                .formLogin()
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/login")
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID");

    }
}
