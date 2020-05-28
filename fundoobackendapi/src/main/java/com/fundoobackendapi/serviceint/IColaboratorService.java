package com.fundoobackendapi.serviceint;
import com.fundoobackendapi.model.User;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import java.util.List;

public interface IColaboratorService {
    UserResponseDTO addColaborator(String token, long noteId, String emailId);

    UserResponseDTO getColaboratorsOfNote(String token, long noteId);

    UserResponseDTO removeColaborator(String token, long noteId, String emailId);
}
