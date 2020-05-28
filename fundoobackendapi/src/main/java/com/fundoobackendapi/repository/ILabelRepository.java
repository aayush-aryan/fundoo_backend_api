package com.fundoobackendapi.repository;
import com.fundoobackendapi.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ILabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByNameAndNoteId(String name, Long noteId);
    List<Label> findByNoteId(Long noteId);
}
