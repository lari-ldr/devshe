package br.com.ldr.devshe.services.cache;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import br.com.ldr.devshe.services.CacheService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisCache implements CacheService, HealthIndicator {
	
	@Value("${redis.ttl:720}")
	private int ttl;
	
	private final JedisPool pool;
	
	@Autowired
	public RedisCache(JedisPool pool) {
		this.pool = pool;
	}
	
	/**
	 * Realiza a codificação da chave, essencial para evitarmos vazamento
	 * de dados caso o redis seja exposto acidentalmente.
	 * @param code
	 * @return
	 */
	private String getKey(String code) {
		return code != null ? DigestUtils.md5DigestAsHex(code.getBytes()) : "nulo";
	}
	
	@Override
	public String get(String key) {
		String hashedKey = getKey(key);
		try (Jedis jedis = this.pool.getResource()) {
			return jedis.get(hashedKey);
		}
	}

	@Override
	public void set(String key, String value) {
		this.set(key,  value, this.ttl);
	}

	@Override
	public void set(String key, String value, int ttl) {
		String hashedKey = getKey(key);
		try (Jedis jedis = this.pool.getResource()) {
			jedis.set(hashedKey, value);
			jedis.expire(hashedKey, ttl);
		}
		
	}

	@Override
	public void remove(String key) {
		try (Jedis jedis = this.pool.getResource()) {
			jedis.del(getKey(key));
		}
	}

	@Override
	public void clean() {
		try (Jedis jedis = this.pool.getResource()) {
			jedis.flushAll();
		}
	}

	@Override
	public String getType() {
		return "Redis";
	}

	@Override
	public Health health() {
		
		try (Jedis jedis = this.pool.getResource()) {
			
			String testKey = "healthCheck-" + UUID.randomUUID().toString();
			String testValue = UUID.randomUUID().toString();
			
			this.set(testKey, testValue);
			
			String storedValue = this.get(testKey);
			if (! testValue.equals(storedValue)) {
				return Health.down().withDetail("redis.inconsistent", "Os valores armazenados no Redis como teste não batem").build();
			}
			this.remove(testKey);
			if (this.get(testKey) != null) {
				return Health.down().withDetail("redis.inconsistent", "Não está sendo possível remover valores do Redis").build();
			}
		} catch (Throwable t) {
			return Health.down().withDetail("redis.error", t.getMessage()).build();
		}
		
		
		return Health.up().build();
	}

}
