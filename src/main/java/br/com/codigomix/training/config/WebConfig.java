package br.com.codigomix.training.config;


import br.com.codigomix.training.web.interceptor.LoggerInterceptor;
import br.com.codigomix.training.web.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    RateLimitInterceptor rateLimitInterceptor;

    @Autowired
    LoggerInterceptor loggerInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
            .addPathPatterns("/**");

        registry.addInterceptor(loggerInterceptor)
            .addPathPatterns("/**");

    }
}
