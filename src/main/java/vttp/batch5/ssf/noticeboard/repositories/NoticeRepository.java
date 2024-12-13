package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class NoticeRepository {

	@Autowired
	@Qualifier("notice")
	private RedisTemplate<String, Object> template;

	// TODO: Task 4
	/*
	 * Redis command:
	 * HSET notices <notice_id> <json_response>
	 * 
	 * Example:
	 * HSET notices 013DDR76K71TZPSP956GFNWZ3 "{\"id\":\"013DDR76K71TZPSP956GFNWZ3\",\"timestamp\":1734998400000}"
	 */
	public void insertNotices(String noticeId, JsonObject response) {
		template.opsForHash()
			.put("notices", noticeId, response.toString());
	}

	/*
	 * Redis command:
	 * RANDOMKEY
	 */
	public boolean isHealthy() {
		try {
			template.randomKey();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
