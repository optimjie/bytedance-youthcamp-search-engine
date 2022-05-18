package com.searchengine.config;

import com.searchengine.MyCorsFilter;
import com.searchengine.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
<<<<<<< HEAD
        http
                //关闭跨站请求防护
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 不通过session获取sessionContext;
=======
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();

        //配置403无权限页面
        http.exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests()
//                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/", "/register", "/login","/search_test","/related_word","/imageUpload","/searchImg").permitAll()//不拦截这些路径
                .anyRequest().authenticated()
>>>>>>> 2a7fdd4249c5943b5b06c3ac190ed5d0ea88e0b2
                .and()
                .authorizeRequests()
                .antMatchers("/user/login", "/","/user/logout", "/register", "/search_test", "/related_word", "/imageUpload","/survival").permitAll()//不拦截这些路径
                .anyRequest().authenticated();

    }
}
