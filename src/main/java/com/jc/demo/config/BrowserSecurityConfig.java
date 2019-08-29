package com.jc.demo.config;

import com.jc.demo.config.filter.ValidateCodeFilter;
import com.jc.demo.config.handler.MyAuthenticationFailureHandler;
import com.jc.demo.config.handler.MyAuthenticationSuccessHandler;
import com.jc.demo.config.session.MySessionExpiredStrategy;
import com.jc.demo.service.impl.UserDetailService;
import com.jc.demo.smscode.SmsAuthenticationConfig;
import com.jc.demo.smscode.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author jinxingjia
 * 8/22/19 3:12 PM
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private SmsCodeFilter smsCodeFilter;
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Autowired
    private MySessionExpiredStrategy sessionExpiredStrategy;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)// 添加验证码校验过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加短信验证码校验过滤器
                .formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/authentication/require")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository()) //配置 token 持久化仓库
                .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                .userDetailsService(userDetailService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/login_upc.html", "/login_sms.html", "/css/**",
                        "/authentication/require", "/code/image", "/code/sms","/session/invalid").permitAll()
                .anyRequest()  // 所有请求
                .authenticated()
                .and()
                .sessionManagement() // 添加 Session管理器
                .invalidSessionUrl("/session/invalid") // Session失效后跳转到这个链接
                .maximumSessions(1)
                .expiredSessionStrategy(sessionExpiredStrategy).and()
                .and().csrf().disable()
                .apply(smsAuthenticationConfig);
        ;
    }

    /**
     * 注入密码编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
}
