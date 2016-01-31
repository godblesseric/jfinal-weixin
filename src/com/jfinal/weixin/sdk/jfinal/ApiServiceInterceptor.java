package com.jfinal.weixin.sdk.jfinal;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

public class ApiServiceInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Object target = inv.getTarget();
		
		if (target instanceof ApiService == false)
			throw new RuntimeException("Service需要继承 ApiService");
		
		// 略过getApiConfig的拦截
		if ("getApiConfig".equals(inv.getMethodName())) {
			inv.invoke();
			return;
		} 
		
		try {
			ApiConfigKit.setThreadLocalApiConfig(((ApiService)target).getApiConfig());
			inv.invoke();
		}
		finally {
			ApiConfigKit.removeThreadLocalApiConfig();
		}
	}

}
