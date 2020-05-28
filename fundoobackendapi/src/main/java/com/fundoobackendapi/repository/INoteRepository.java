package com.fundoobackendapi.repository;
import com.fundoobackendapi.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByNoteId(Long noteId);

    List<Note> findAllByUserId(Long userId);

    Optional<Note> findByUserId(Long userId);

    Note  findByNoteIdAndUserId(Long noteId,Long userId);
}
