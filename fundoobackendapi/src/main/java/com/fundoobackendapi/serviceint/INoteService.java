package com.fundoobackendapi.serviceint;
import com.fundoobackendapi.dto.NoteDto;
import com.fundoobackendapi.responsedto.UserResponseDTO;

public interface INoteService {
    UserResponseDTO createNote(NoteDto noteDto, String token);

    UserResponseDTO deleteNote(long noteId, String token);

    UserResponseDTO getAllNotes(String token);
    UserResponseDTO getNoteByNoteId(Long noteId, String token);
    UserResponseDTO updateNote(Long noteId, NoteDto noteDto, String token);
    UserResponseDTO colorUpdate(Long noteId, String color,String token);
    UserResponseDTO setReminder(Long noteId, String reminder, String token);
    UserResponseDTO searchNoteByDescription(String description,String token);
    UserResponseDTO searchNoteByTitle(String title,String token);
    UserResponseDTO flagUpdate(long noteId, String mode, String token);
}
