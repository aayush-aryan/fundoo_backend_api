package com.fundoobackendapi.serviceint;
import com.fundoobackendapi.dto.LabelDto;
import com.fundoobackendapi.responsedto.UserResponseDTO;

public interface ILabelService {

    UserResponseDTO addLabelToNote(LabelDto labelDto, long noteId,String token);

    UserResponseDTO deleteLabel(long labelId,String token);

    UserResponseDTO updateLabel(LabelDto labelDto, long labelId,String token);

    UserResponseDTO getAllLabel(Long noteId,String token);

    UserResponseDTO mapLabelToNote(long noteId, long labelId,String token);
}
