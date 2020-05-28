package com.fundoobackendapi.serviceint;

import com.fundoobackendapi.model.Note;
import java.io.IOException;
import java.util.List;

public interface IElasticSearch {
    void createNote(Note noteDocument) throws Exception;

    Note findById(long id) throws Exception;

    void Update(Note noteDocument) throws Exception;

    void deleteNote(long noteId) throws Exception;

    List<Note> findByKeyword(String keyword)throws Exception;

    List<Note> findAll(long userId) throws IOException;

}
