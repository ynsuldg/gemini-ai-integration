package se.iths.yunus.googlegemini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.iths.yunus.googlegemini.service.GeminiService;

@Controller
public class GeminiController {
    private final GeminiService geminiService;

    // GeminiService = Google SDK
    // GeminiRestService = REST + JSON
    // You may switch injection from "GeminiService" to "GeminiRestService"

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("conversations", geminiService.getAllConversations());

        return "index";
    }

    @PostMapping("/ask")
    public String ask(@RequestParam String prompt, Model model) {

        String answer = geminiService.askGemini(prompt);

        model.addAttribute("prompt", prompt);
        model.addAttribute("answer", answer);
        model.addAttribute("conversations", geminiService.getAllConversations());

        return "index";
    }
}
