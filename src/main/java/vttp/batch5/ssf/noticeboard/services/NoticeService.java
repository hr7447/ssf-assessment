package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Value("${notice.publish.url}")
	private String publishUrl;

	private RestTemplate template = new RestTemplate();

	@Autowired
	private NoticeRepository noticeRepo;

	public Optional<JsonObject> postToNoticeServer(Notice notice) {
		JsonObjectBuilder builder = Json.createObjectBuilder()
			.add("title", notice.getTitle())
			.add("poster", notice.getPoster())
			.add("text", notice.getText())
			.add("postDate", notice.getPostDate()
					.atStartOfDay(ZoneId.systemDefault())
					.toInstant()
					.toEpochMilli());

		builder.add("categories", Json.createArrayBuilder(notice.getCategories()));

		JsonObject payload = builder.build();

		RequestEntity<String> req = RequestEntity
			.post(publishUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(payload.toString());

		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);
			
			JsonObject responseBody = Json.createReader(
				new StringReader(resp.getBody())
			).readObject();

			noticeRepo.insertNotices(responseBody.getString("id"), responseBody);

			return Optional.of(responseBody);

		} catch (RestClientException ex) {
			return Optional.empty();
		}
	}
}
