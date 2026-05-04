package se.iths.yunus.googlegemini.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.iths.yunus.googlegemini.model.Conversation;
import se.iths.yunus.googlegemini.repository.ConversationRepository;

import java.util.List;
import java.util.Map;

@Service
public class GeminiRestService {

    private final ConversationRepository conversationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public GeminiRestService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public String askGemini(String prompt) {

        String apiKey = System.getenv("GOOGLE_API_KEY");

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of(
                                                "text",
                                                prompt
                                        )
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            String answer = extractAnswer(response.getBody());

            Conversation conversation = new Conversation(prompt, answer);

            conversationRepository.save(conversation);

            return answer;

        } catch (Exception e) {
            return "ERROR: Gemini is currently unavailable or quota has been exceeded. Please try again later.";
        }
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    private String extractAnswer(Map responseBody) {

        try {
            List candidates = (List) responseBody.get("candidates");

            Map candidate = (Map) candidates.get(0);

            Map content = (Map) candidate.get("content");

            List parts = (List) content.get("parts");

            Map part = (Map) parts.get(0);

            return part.get("text").toString();

        } catch (Exception e) {
            return "ERROR: Gemini is currently unavailable or quota has been exceeded. Please try again later.";
        }
    }
}
