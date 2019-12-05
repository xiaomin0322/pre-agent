package com.dominos.cloud.agent;

import java.util.HashSet;
import java.util.Set;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class OtherCollector implements Collector {

	public static final OtherCollector INSTANCE = new OtherCollector();

	private OtherCollector() {
	}

	private static final String beginSrc;
	private static final String endSrc = "inst.end(statistic);";
	private static final String errorSrc;
	
	private static Set<String> targetSet = new HashSet<>();

	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("com.dominos.cloud.agent.OtherCollector inst=com.dominos.cloud.agent.OtherCollector.INSTANCE;");
		stringBuilder.append("com.dominos.cloud.agent.Statistics statistic = inst.start(\"%s\");");
		beginSrc = stringBuilder.toString();
		errorSrc = "inst.error(statistic,e);";
		
		
		
		targetSet.add("com.dominos.cloud.agent.TestService");
		targetSet.add("com.mysql.jdbc.ConnectionImpl");
		targetSet.add("com.mysql.jdbc.PreparedStatement");
		targetSet.add("com.mysql.jdbc.PreparedStatement");
		
		targetSet.add("com.alibaba.druid.pool.DruidDataSource");
		targetSet.add("com.alibaba.druid.pool.DruidPooledConnection");
		
		
	}
	
	

	@Override
	public boolean isTarget(String className, ClassLoader classLoader, CtClass ctClass) {
		return  targetSet.contains(className);
	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		try {
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				if ((Modifier.isPublic(ctMethod.getModifiers())) && (!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers())))) {
					ClassWrapper classWrapper = new ClassWrapper();
					classWrapper.beginSrc(String.format(beginSrc, ctMethod.getLongName()));
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(ctMethod, classWrapper);
				}
			}
			return replacer.replace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public Statistics start(String methodSign) {
		return new Statistics(methodSign);
	}

	public void end(Statistics statistics) {
		statistics.end();
	}

	public void error(Statistics statistics, Throwable e) {
		statistics.error(e);
	}
}
