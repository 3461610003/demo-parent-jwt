package com.hao.demo.shiro.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    /**
     * 更换fastjson解析
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1、需要先定义一个 convert 转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteBigDecimalAsPlain);
//        fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");
//        //3.设置成了PrettyFormat格式
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //4.处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        //5.将fastJsonConfig加到消息转换器中
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(fastConverter);
    }

//    /**
//     * 防止跨域
//     */
//    @Bean
//    public CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); // 1
//        corsConfiguration.addAllowedHeader("*"); // 2
//        corsConfiguration.addAllowedMethod("*"); // 3
//        corsConfiguration.setMaxAge(1800L);
//        corsConfiguration.setAllowCredentials(true);
//        return corsConfiguration;
//    }
//
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 4
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }


    /**
     * 默认解析器 其中locale表示默认语言
     */
    @Bean
    public LocaleResolver localeResolver() {
        List<Locale> locales = Arrays.asList(
                new Locale("zh"),
                new Locale("en"),
                new Locale("ja"),
                new Locale("ko")
        );
        return new AcceptHeaderLocaleResolver(){
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");
                Locale locale;
                try {
                    locale = Locale.lookup(Locale.LanguageRange.parse(headerLang), locales);
                } catch (Exception e) {
                    locale = Locale.CHINA;
                }
                return locale;
            }
        };
    }

}
