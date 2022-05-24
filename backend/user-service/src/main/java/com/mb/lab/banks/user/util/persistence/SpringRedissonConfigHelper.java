package com.mb.lab.banks.user.util.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;

import com.mb.lab.banks.utils.common.StringUtils;

public class SpringRedissonConfigHelper {

	@SuppressWarnings("unchecked")
	public static void applySpringRedisConfig(Config config, RedisProperties redisProperties) {
		Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
		Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
		Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
		int timeout;
		if (null == timeoutValue) {
			timeout = 10000;
		} else if (!(timeoutValue instanceof Integer)) {
			Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
			timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
		} else {
			timeout = (Integer) timeoutValue;
		}

		if (StringUtils.isEmpty(redisProperties.getPassword())) {
			redisProperties.setPassword(null);
		}

		// Override connection configuration from Spring Boot's properties
		if (redisProperties.getSentinel() != null) {
			Method nodesMethod = ReflectionUtils.findMethod(Sentinel.class, "getNodes");
			Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

			String[] nodes;
			if (nodesValue instanceof String) {
				nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
			} else {
				nodes = convert((List<String>) nodesValue);
			}

			// @formatter:off
            config.useSentinelServers()
                .setMasterName(redisProperties.getSentinel().getMaster())
                .addSentinelAddress(nodes)
                .setDatabase(redisProperties.getDatabase())
                .setConnectTimeout(timeout)
                .setPassword(redisProperties.getPassword());
            // @formatter:on
		} else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
			Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
			Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
			List<String> nodesObject = (List<String>) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

			String[] nodes = convert(nodesObject);

			// @formatter:off
            config.useClusterServers()
                .addNodeAddress(nodes)
                .setConnectTimeout(timeout)
                .setPassword(redisProperties.getPassword());
            // @formatter:on
		} else {
			String prefix = "redis://";
			Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
			if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
				prefix = "rediss://";
			}

			// @formatter:off
            config.useSingleServer()
                .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setConnectTimeout(timeout)
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword());
            // @formatter:on
		}
	}

	public static Config loadConfig(ApplicationContext ctx, String configPath) throws IOException {
		Resource resource = ctx.getResource(configPath);
		if (resource == null) {
			return null;
		}

		InputStream is = resource.getInputStream();
		if (is != null) {
			try {
				return Config.fromYAML(is);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return null;
	}

	public static Config loadConfig(ClassLoader classLoader, String fileName) throws IOException {
		InputStream is = classLoader.getResourceAsStream(fileName);
		if (is != null) {
			try {
				return Config.fromYAML(is);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return null;
	}

	private static String[] convert(List<String> nodesObject) {
		List<String> nodes = new ArrayList<String>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
				nodes.add("redis://" + node);
			} else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[nodes.size()]);
	}
}
