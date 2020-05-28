package com.fundoobackendapi.services;
import com.fundoobackendapi.dto.NoteDto;
import com.fundoobackendapi.exception.ResponseHelper;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.model.Note;
import com.fundoobackendapi.repository.INoteRepository;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.IElasticSearch;
import com.fundoobackendapi.serviceint.INoteService;
import com.fundoobackendapi.utility.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImp implements INoteService {
    @Autowired
    private INoteRepository noteRepository;

    @Autowired
    private JwtTokenUtil tokenGenerator;
   // @Autowired
   // private IElasticSearch elasticSearch;
    private long userId;
    private Note note;

   /* public void updateDocument(Note noteDocument){
        try {
            elasticSearch.Update(noteDocument);
        }catch (Exception e){
            e.printStackTrace();
        }

    */

    public boolean verifyUser(String token){
        try{
            userId = tokenGenerator.decodeToken(token);
        }catch (UserException e){
            return false;
        }
        return true;
    }
    @Override
    public UserResponseDTO createNote(NoteDto noteDto, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        if(noteDTOValidator(noteDto))
            return ResponseHelper.statusResponse(400, "Empty Notes Not Created",noteDto);
        ModelMapper mapper = new ModelMapper();
        note = mapper.map(noteDto, Note.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        note.setRegDate(localDateTime.format(dateFormat));
        note.setUserId(userId);
        noteRepository.save(note);
       /* try {
            elasticSearch.createNote(note);
        }catch(Exception e){
            e.printStackTrace();
        }*/
        return ResponseHelper.statusResponse(200, "Notes Created SuccessFully", noteDto);
    }

    private boolean noteDTOValidator(NoteDto noteDto) {
        return (noteDto.getTitle() == null || noteDto.getDescription() == null)
                || (noteDto.getTitle() == "" && noteDto.getDescription() == "");
    }
    @Override
    public UserResponseDTO deleteNote(long noteId,String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        note = noteRepository.findByNoteIdAndUserId(noteId,userId);
        if(note!=null){
            noteRepository.deleteById(noteId);
            return ResponseHelper.statusResponse(200, "Note Deleted successfully", noteId);
        }
        return ResponseHelper.statusResponse(400, "Note Doesn't Exit", noteId);
    }
    @Override
    public UserResponseDTO getAllNotes(String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        List<Note> allNotes = noteRepository.findAllByUserId(userId);
       /* List<Note> allNotes1=null;
        try{
            allNotes1 = elasticSearch.findAll(userId);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        return ResponseHelper.statusResponse(200, "Note Found Successfully", allNotes);
    }
    @Override
    public UserResponseDTO getNoteByNoteId(Long noteId, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Note note = noteRepository.findByNoteIdAndUserId(noteId,userId);
       /* try {
             note = elasticSearch.findById(noteId);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        if (note == null) {
            return ResponseHelper.statusResponse(400, "Given Note Id Not Exit", noteId);
        }
        return ResponseHelper.statusResponse(200, "Note Found Successfully",note);
    }

    @Override
    public UserResponseDTO updateNote(Long noteId, NoteDto noteDto,String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        note = noteRepository.findByNoteIdAndUserId(noteId,userId);
        if(note==null) return ResponseHelper.statusResponse(400, "Note Can't Be Updated","Note Id : "+noteId);
        note.setTitle(noteDto.getTitle());
        note.setDescription(noteDto.getDescription());
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        note.setRegDate(localDateTime.format(dateFormat));
        noteRepository.save(note);
        // updateDocument(note);
        return ResponseHelper.statusResponse(200, "Note Updated SuccessFully",noteId);
    }
    @Override
    public UserResponseDTO colorUpdate(Long noteId, String color,String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        note = noteRepository.findByNoteIdAndUserId(noteId,userId);
        String[] colorArray = { "red", "green", "blue", "white", "pink", "brown", "grey", "yellow", "teal", "dark blue",
                "purple", "orange" };
        if(note==null) return ResponseHelper.statusResponse(400, "Note Doesn't Exist","Note Id : "+noteId);
        String colour = color.toLowerCase();
        for (String string : colorArray) {
            if (string.equals(colour)) {
                note.setColor(colour);
                noteRepository.save(note);
                //updateDocument(note);
                return ResponseHelper.statusResponse(200, "Color Updated successfully ", color);
            }
        }
        return ResponseHelper.statusResponse(400, "Color Not Present In List ", colorArray);
    }
    @Override
    public UserResponseDTO setReminder(Long noteId, String reminders, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        note = noteRepository.findByNoteIdAndUserId(noteId,userId);
        if (note == null) return ResponseHelper.statusResponse(400, "Note Doesn't Exist", "Note Id : " + noteId);
        String reminder = reminders.toLowerCase();
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime nextWeek = LocalDateTime.now().plusWeeks(1);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        switch (reminder){
            case "today" :   note.setReminder(today.format(dateFormat));
                noteRepository.save(note);
                //updateDocument(note);
                return ResponseHelper.statusResponse(200, "Reminder Is Set Successfully",
                        "Reminder set for today "+today);
            case "tomorrow": note.setReminder(tomorrow.format(dateFormat));
                noteRepository.save(note);
                //updateDocument(note);
                return ResponseHelper.statusResponse(200, "Reminder Is Set Successfully",
                        "Reminder set for tomorrow "+tomorrow);
            case "nextWeek": note.setReminder(nextWeek.format(dateFormat));
                noteRepository.save(note);
                //updateDocument(note);
                return ResponseHelper.statusResponse(200, "Reminder Is Set Successfully",
                        "Reminder set for next week "+nextWeek);
            default:        return ResponseHelper.statusResponse(400, "Error Occurring to Set Reminder",
                    "Enter a valid reminder");
        }
    }
    @Override
    public UserResponseDTO searchNoteByTitle(String title,String token){
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        List<Note> list = (ArrayList)getAllNotes(token).result;
        List<Note> notes = new ArrayList<>();
        for(Note note:list){
            if(note.getTitle().toLowerCase().contains(title.toLowerCase())){
                notes.add(note);
            }
        }
        return ResponseHelper.statusResponse(200, "Title is found Successfully",
                notes);
    }
    @Override
    public UserResponseDTO searchNoteByDescription(String description,String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        List<Note> list = (ArrayList)getAllNotes(token).result;
        List<Note> notes = new ArrayList<>();
        for(Note note:list){
            if(note.getDescription().toLowerCase().contains(description.toLowerCase())){
                notes.add(note);
            }
        }
        return ResponseHelper.statusResponse(200, "description is found Successfully",
                notes);
    }
    @Override
    public UserResponseDTO flagUpdate(long noteId, String mode, String token){
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        String[] modes = { "pin", "unpin", "trash", "untrash", "unarchieve", "archieve" };
        Optional<Note> note = noteRepository.findById(noteId);
        String status = mode.toLowerCase();
        if(note==null) ResponseHelper.statusResponse(400, "Note doesn't exit",
                noteId);
        if (status.equals("trash")) {
            note.get().setTrashStatus("trash");
            noteRepository.save(note.get());
            //updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Trash updated Successfully", "trashed");
        } else if (status.equals("untrash")) {
            note.get().setTrashStatus("untrash");
            noteRepository.save(note.get());
            //updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Untrash updated successfully", "untrashed");

        } else if (status.equals("pin")) {
            note.get().setPinStatus("pin");
            noteRepository.save(note.get());
            //updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Pin updated Successfully", "pinned");
        } else if (status.equals("unpin")) {
            note.get().setPinStatus("unpin");
            noteRepository.save(note.get());
            //updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Unpin updated Successfully", "unpinned");

        } else if (status.equals("archieve")) {
            note.get().setArchieveStatus("archieve");
            noteRepository.save(note.get());
            //updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Archieve updated Successfully", "archieved");
        } else if (status.equals("unarchieve")) {
            note.get().setArchieveStatus("unarchieve");
            noteRepository.save(note.get());
            // updateDocument(note.get());
            return ResponseHelper.statusResponse(200, "Unarchieved updated successfully", "unarchieved");
        }
        return ResponseHelper.statusResponse(501, "Please enter a valid String from -", modes);
    }
}
