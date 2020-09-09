package com.haider.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// since  AUTHENTICATON FILTER IS NOT A BEAN WE CANNOT AUTOWIRE IT NOT AUTOWIRE TO IT SO WE THIS 
// APPLICATION CONTEXT

@Component
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		CONTEXT = applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

}
