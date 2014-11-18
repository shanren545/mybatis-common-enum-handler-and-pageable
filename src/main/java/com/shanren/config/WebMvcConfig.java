package com.shanren.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.shanren.controller")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new LocaleInterceptor());
	}

	/*
	 * @Override public void
	 * configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	 * configurer.favorPathExtension(false).favorParameter(true); }
	 */

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}

	/*
	 * @Override public void configureViewResolvers(ViewResolverRegistry
	 * registry) { registry.enableContentNegotiation(new
	 * MappingJackson2JsonView()); registry.freeMarker().cache(false); }
	 */

	// <!-- Resolves views selected for rendering by @Controllers to .jsp
	// resources in the /WEB-INF/views directory -->
	// <beans:bean
	// class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	// <beans:property name="prefix" value="/WEB-INF/views/" />
	// <beans:property name="suffix" value=".jsp" />
	public void configureViewResolvers(ViewResolverRegistry registry) {
		/*
		 * InternalResourceViewResolver inv = new
		 * InternalResourceViewResolver(); inv.setPrefix("/WEB-INF/views/");
		 * inv.setSuffix(".jsp");
		 */
		// registry.enableContentNegotiation(new MappingJackson2JsonView());
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	/*
	 * @Bean public FreeMarkerConfigurer freeMarkerConfigurer() {
	 * FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
	 * configurer.setTemplateLoaderPath("/WEB-INF/"); return configurer; }
	 */

	// <resources mapping="/resources/**" location="/resources/" />
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

}
