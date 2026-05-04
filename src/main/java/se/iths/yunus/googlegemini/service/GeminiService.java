package se.iths.yunus.googlegemini.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;
import se.iths.yunus.googlegemini.model.Conversation;
import se.iths.yunus.googlegemini.repository.ConversationRepository;

import java.util.List;

@Service
public class GeminiService {
    private final ConversationRepository conversationRepository;

    public GeminiService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public String askGemini(String prompt) {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "ERROR: GOOGLE_API_KEY not found.";
        }

        try {
            Client client = Client.builder().apiKey(apiKey).build();

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash", prompt, null);

            String answer = response.text();

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
}
