package com.fundoobackendapi.services;
import com.fundoobackendapi.dto.RabbitMqDto;
        import com.fundoobackendapi.exception.ResponseHelper;
        import com.fundoobackendapi.exception.UserException;
        import com.fundoobackendapi.model.Note;
        import com.fundoobackendapi.model.User;
        import com.fundoobackendapi.repository.INoteRepository;
        import com.fundoobackendapi.repository.IUserRepository;
        import com.fundoobackendapi.responsedto.UserResponseDTO;
        import com.fundoobackendapi.serviceint.IColaboratorService;
        import com.fundoobackendapi.utility.JwtTokenUtil;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.mail.SimpleMailMessage;
        import org.springframework.mail.javamail.JavaMailSender;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;

@Service
public class ColaboratorServiceImpl implements IColaboratorService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private INoteRepository noteRepository;
    @Autowired
    private JwtTokenUtil tokenGenerator;
    @Autowired
    private JavaMailSender javaMailSender;
    private long userId;

    public boolean verifyUser(String token){
        try{
            userId = tokenGenerator.decodeToken(token);
        }catch (UserException e){
            return false;
        }
        return true;
    }

    public void sendMessage(String email,String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("ubuntushell89@gmail.com");
        message.setSubject("Collaboration Invitation!");
        message.setText(body);
        javaMailSender.send(message);
    }
    @Override
    public UserResponseDTO addColaborator(String token, long noteId, String emailId) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Optional<User> user = userRepository.findById(userId);
        Optional<Note> validNote = noteRepository.findByNoteId(noteId);
        Optional<User> validColaborator = userRepository.findByEmail(emailId);
        if(!validColaborator.isPresent()){
            String body = user.get().getFirstName()+" "+user.get().getLastName()+" wants to Collaborate With his Note "+" " +
                    "if you want to collaborate Please Register first "+
                    "for Registration Click On this Link http://localhost:8081/users/registration";
            sendMessage(emailId,body);
            return ResponseHelper.statusResponse(400, "A Registration link is Send to this EmailID","Unregistered User");
        }
        validNote.get().getColaboratedUsers().add(validColaborator.get());
        validColaborator.get().getColaboratedNotes().add(validNote.get());
        userRepository.save(validColaborator.get());
        String body = "Hi "+ validColaborator.get().getFirstName()+" "+validColaborator.get().getLastName()+".! "+
                user.get().getFirstName()+" "+user.get().getLastName()+" wants to Collaborate With his Note "+
                "Title : "+validNote.get().getTitle()+" Description : "+validNote.get().getDescription()+" to view it click on this link "+
                "http://localhost:8081/notes/updateNote/"+noteId;
        sendMessage(emailId,body);
        return ResponseHelper.statusResponse(200, "Collaborator Added Successfully",noteId);
    }
    @Override
    public UserResponseDTO getColaboratorsOfNote(String token, long noteId) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Optional<Note> fetchedValidNote = noteRepository.findByNoteId(noteId);
        List<User> list = fetchedValidNote.get().getColaboratedUsers();
        return ResponseHelper.statusResponse(200, "Collaborator Found Successfully",list);
    }

    @Override
    public UserResponseDTO removeColaborator(String token, long noteId, String emailId) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Optional<Note> validNote = noteRepository.findByNoteId(noteId);
        Optional<User> validColaborator = userRepository.findByEmail(emailId);
        validNote.get().getColaboratedUsers().remove(validColaborator.get());
        validColaborator.get().getColaboratedNotes().remove(validNote.get());
        userRepository.save(validColaborator.get());
        return ResponseHelper.statusResponse(200, "Collaborator Remove Successfully",
                emailId);
    }

}
