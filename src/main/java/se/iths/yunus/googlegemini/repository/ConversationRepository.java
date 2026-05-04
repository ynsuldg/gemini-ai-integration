package se.iths.yunus.googlegemini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.iths.yunus.googlegemini.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
