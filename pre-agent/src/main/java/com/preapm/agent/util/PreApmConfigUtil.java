package com.preapm.agent.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreApmConfigUtil {
	
	private static Map<String, Set<String>> targetMap = new HashMap<>();

	static {

		Set<String> methodSet = new HashSet<>();
		methodSet.add("com.preapm.agent.Bootstrap.print(java.lang.String)");
		methodSet.add("com.preapm.agent.Bootstrap.print(java.lang.String,java.lang.String)");
		targetMap.put("com.preapm.agent.Bootstrap", methodSet);

		methodSet = new HashSet<>();
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection()");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnectionDirect(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(java.lang.String, java.lang.String)");
		targetMap.put("com.alibaba.druid.pool.DruidDataSource", methodSet);

		methodSet = new HashSet<>();
		methodSet.add(
				"com.dominos.cloud.im.controller.StoreController.test(com.dominos.cloud.im.controller.ProductController)");
		methodSet.add(
				"com.dominos.cloud.im.controller.StoreController.test2(com.dominos.cloud.im.controller.ProductController,com.dominos.cloud.im.model.StoreGroupsWithBLOBs)");
		targetMap.put("com.dominos.cloud.im.controller.StoreController", methodSet);
		//methodSet.clear();
		targetMap.put("org.springframework.cloud.sleuth.Tracer", methodSet);

	}
	
	public static Set<String>  get(String key){
		return targetMap.get(key);
	}
	
	
	public static boolean isTarget(String className, String method) {
		boolean flag = isTarget(className);
		if(!flag) {
			return flag;
		}
		flag = PreApmConfigUtil.get(className).contains(method);
		return flag;
	}

	public static boolean isTarget(String className) {
		Set<String> methodSet = PreApmConfigUtil.get(className);
		if (methodSet == null || methodSet.isEmpty()) {
			return false;
		}
		return true;
	}
	
	
	

}
