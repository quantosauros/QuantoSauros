package com.quantosauros.manager.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan({"com.quantosauros.manager.web", "com.quantosauros.manager.service",
	"com.quantosauros.manager.dao", "com.quantosauros.manager.exception", 
	"com.quantosauros.manager.validator" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
//	@Bean
//	public BasicDataSource dataSource(){
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://dirondol.synology.me:3306/kirudaAAD");
//		dataSource.setUsername("kiruda");
//		dataSource.setPassword("1qaz2wsx");
//		return dataSource;
//	}
//	
//	@Bean
//	public DataSourceTransactionManager dataSourceTransactionManager(){
//		DataSourceTransactionManager manager = new DataSourceTransactionManager();		
//		return manager;
//	}
	
//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//		ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
//		rb.setBasenames(new String[] { "messages/messages", "messages/validation" });
//		return rb;
//	}
}
