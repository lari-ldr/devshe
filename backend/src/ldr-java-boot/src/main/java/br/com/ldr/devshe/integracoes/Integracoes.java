package br.com.ldr.devshe.integracoes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class Integracoes {

	@Value("${redis.host}")
	private String jedisHost;
	
	@Value("${redis.port}")
	private int jedisPort;
	
	@Value("${redis.maxIdle:100}")
	private int jedisMaxIdle;
	
	@Value("${redis.minIdle:10}")
	private int jedisMinIdle;

	@Bean
	public JedisPool getJedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(this.jedisMaxIdle);
		poolConfig.setMinIdle(this.jedisMinIdle);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnCreate(true);
		poolConfig.setTestOnReturn(true);
		return new JedisPool(poolConfig, this.jedisHost, this.jedisPort);
		
	}
	
}
