package com.packt.webstore.config;

import java.util.ArrayList;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;
import org.springframework.web.util.UrlPathHelper;

import com.packt.webstore.domain.Product;

@Configuration
public class WebApplicationContextConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
       UrlPathHelper urlPathHelper = new UrlPathHelper();
       urlPathHelper.setRemoveSemicolonContent(false);

       configurer.setUrlPathHelper(urlPathHelper);
    }
    
    @Bean
    public MessageSource messageSource() { 
       ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
       resource.setBasename("messages");
       return resource;    
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/img/**")
              .addResourceLocations("/resources/images/");
    }
    
    @Bean
    public MappingJackson2JsonView jsonView() {
       MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
       jsonView.setPrettyPrint(true);
       
       return jsonView; 
    }
    
    @Bean
    public MarshallingView xmlView() {
       Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
       marshaller.setClassesToBeBound(Product.class);
       
       MarshallingView xmlView = new MarshallingView(marshaller);
       return xmlView;
    }
    
    @Bean
    public ViewResolver viewResolver(
             ContentNegotiationManager manager) {
       ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
       resolver.setContentNegotiationManager(manager);
          
       ArrayList<View>   views = new ArrayList<>();
       views.add(jsonView());
       views.add(xmlView());
          
       resolver.setDefaultViews(views);
             
       return resolver;
    }


}
