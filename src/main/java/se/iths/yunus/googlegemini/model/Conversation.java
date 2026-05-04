package se.iths.yunus.googlegemini.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prompt;
    @Column(columnDefinition = "TEXT")
    private String answer;

    public Conversation() {
    }

    public Conversation(String prompt, String answer) {
        this.prompt = prompt;
        this.answer = answer;
    }
}
