package com.imooc.apigetway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @ClassName: CorsConfig
 * @Description: C - Cross O - Origin R - Resource S - sharing
 * @Author: ZhangChen
 * @Date: 2019/6/30 12:57
 **/
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        final CorsConfiguration config = new CorsConfiguration();
        //支持cookie跨域
        config.setAllowCredentials(true);
        //放原始域(http:www.a.com) *是全部允许
        config.setAllowedOrigins(Arrays.asList("*"));
        //放允许的头
        config.setAllowedHeaders(Arrays.asList("*"));
        //放允许哪些方法
        config.setAllowedMethods(Arrays.asList("*"));
        //设置缓存时间,在这个时间段里,对于相同的跨域请求不再进行检查.
        config.setMaxAge(300l);
        //将跨域的配置注册到source上面去
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source) ;
    }
}
