package com.fundoobackendapi.services;
import com.fundoobackendapi.dto.LabelDto;
import com.fundoobackendapi.exception.ResponseHelper;
import com.fundoobackendapi.exception.UserException;
import com.fundoobackendapi.model.Label;
import com.fundoobackendapi.model.Note;
import com.fundoobackendapi.repository.ILabelRepository;
import com.fundoobackendapi.repository.INoteRepository;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.ILabelService;
import com.fundoobackendapi.utility.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import springfox.documentation.swagger2.mappers.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class LabelServiceImp implements ILabelService {
    @Autowired
    private ILabelRepository labelRepository;

    @Autowired
    private INoteRepository noteRepository;

    @Autowired
    private JwtTokenUtil tokenGenerator;

    private long userId;
    public boolean verifyUser(String token){
        try{
            userId = tokenGenerator.decodeToken(token);
        }catch (UserException e){
            return false;
        }
        return true;
    }

    @Override
    public UserResponseDTO addLabelToNote(LabelDto labelDto, long noteId, String token){
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        String checkLabel = labelDto.getLabelName();
        Note note = noteRepository.findByNoteIdAndUserId(noteId,userId);
        if (note == null)
            return ResponseHelper.statusResponse(400, "Note is Not Found",
                    "Invalid Note For Given User Id");
        Optional<Label> label1 = labelRepository.findByNameAndNoteId(checkLabel,noteId);
        if (label1.isPresent()) {
            return ResponseHelper.statusResponse(400, "Level Is Already Added",
                    checkLabel);
        }
        ModelMapper mapper = new ModelMapper();
        Label label = mapper.map(labelDto, Label.class);
        label.setNoteId(noteId);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        label.setCreatDate(localDateTime.format(dateFormat));
        note.getLabels().add(label);
        noteRepository.save(note);
        return ResponseHelper.statusResponse(200, "Level is Added Successfully",
                label);
    }

    @Override
    public UserResponseDTO deleteLabel(long labelId, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Optional<Label> label = labelRepository.findById(labelId);
        if (!label.isPresent())
            ResponseHelper.statusResponse(400, "Label is not found",
                    labelId);
        labelRepository.deleteById(labelId);

        return ResponseHelper.statusResponse(200, "Level is Deleted Successfully", labelId);
    }

    @Override
    public UserResponseDTO updateLabel(LabelDto labelDto, long labelId, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:a");
        Optional<Label> label = labelRepository.findById(labelId);
        if (label != null) {
            label.get().setName(labelDto.getLabelName());
            label.get().setModDate(localDateTime.format(dateFormat));
            labelRepository.save(label.get());
            return ResponseHelper.statusResponse(200, "LevelId " + label.get().getLabelId() + " Updated Successfully",
                    "Label Title " + label.get().getName());
        }
        return ResponseHelper.statusResponse(400, "Level Doesn't Exit",
                "Invalid levelId For given User");
    }

    @Override
    public UserResponseDTO getAllLabel(Long noteId,String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        List<Label> labelList = labelRepository.findByNoteId(noteId);
        List<Label> labelList1 = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        for (Label l:labelList) {
            Label label = mapper.map(l,Label.class);
            labelList1.add(label);
        }
        return ResponseHelper.statusResponse(200, "Level Found Successfully",
                labelList1);
    }
    @Override
    public UserResponseDTO mapLabelToNote(long noteId, long labelId, String token) {
        if(!verifyUser(token)) return ResponseHelper.statusResponse(400, "Token Expired ",token);
        Note note = noteRepository.findByNoteIdAndUserId(noteId, userId);
        if (note == null) return ResponseHelper.statusResponse(400, "No Any Note Found",
                noteId);
        Optional<Label> label = labelRepository.findById(labelId);
        if (label == null) return ResponseHelper.statusResponse(400, "Level Not Exit",
                labelId);
        List<Label> labelsList = note.getLabels();
        for (Label label2 : labelsList) {
            if (label2.getLabelId() == labelId) {
                return ResponseHelper.statusResponse(400, "Level Already Exit",
                        "LevelId " + labelId);
            }
        }
        note.getLabels().add(label.get());
        noteRepository.save(note);
        return ResponseHelper.statusResponse(200, "Level Mapped Successfully",
                "LevelID " + labelId + " map with " + noteId);
    }
}


