package com.fundoobackendapi.controller;
import com.fundoobackendapi.dto.NoteDto;
import com.fundoobackendapi.model.Note;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/notes")
public class NoteController {
    @Qualifier("noteServiceImp")
    @Autowired
    private INoteService noteService;

    @PostMapping("/create")
    public UserResponseDTO noteCreation(@RequestBody NoteDto noteDto, @RequestHeader String token) {
        return noteService.createNote(noteDto, token);
    }
    @PutMapping("/update/{noteId}")
    public UserResponseDTO UpdateNotes(@PathVariable Long noteId, @RequestBody NoteDto noteDto, @RequestHeader String token) {
        return noteService.updateNote(noteId, noteDto,token);
    }
    @PutMapping("/addColor/{noteId}/{color}")
    public UserResponseDTO UpdateColour(@PathVariable String color, @PathVariable Long noteId, @RequestHeader String token) {
        return noteService.colorUpdate(noteId, color,token);
    }
    @PutMapping("/reminder/{noteId}/{reminder}")
    public UserResponseDTO setReminder(@PathVariable String reminder, @PathVariable Long noteId, @RequestHeader String token) {
        return noteService.setReminder(noteId, reminder, token);
    }
    @GetMapping("/getAllNotes")
    public UserResponseDTO getAllNotes(@RequestHeader String token) {
        return noteService.getAllNotes(token);
       /* List<Note> list=null;
        try {
           list = noteService.getAllNotes(token);
        }catch (UserException e){
            return ResponseHelper.statusResponse(400, "Token Expire", token);
        }
        return ResponseHelper.statusResponse(200, "Note Found Successfully", list);*/
    }
    @GetMapping("/getNoteById/{noteId}")
    public UserResponseDTO getNoteById(@PathVariable Long noteId,@RequestHeader String token) {
        return noteService.getNoteByNoteId(noteId,token);
    }
    @DeleteMapping("/delete/{noteId}")
    public UserResponseDTO deleteNote(@PathVariable Long noteId, @RequestHeader String token) {
        return noteService.deleteNote(noteId,token);
    }
    @GetMapping("/searchTitle/{title}")
    public UserResponseDTO searchTitle(@PathVariable String title, @RequestHeader String token) {
        return noteService.searchNoteByTitle(title,token);
    }
    @GetMapping("/searchDescription/{description}")
    public UserResponseDTO searchDescription(@PathVariable String description, @RequestHeader String token) {
        return noteService.searchNoteByDescription(description,token);
    }
    @PutMapping("/updateFlag/{noteId}/{mode}")
    public UserResponseDTO flagUpdate(@PathVariable long noteId, @PathVariable String mode,@RequestHeader String token){
        return noteService.flagUpdate(noteId,mode,token);
    }
}
