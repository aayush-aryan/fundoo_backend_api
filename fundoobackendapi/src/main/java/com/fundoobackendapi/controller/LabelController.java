package com.fundoobackendapi.controller;
import com.fundoobackendapi.dto.LabelDto;
import com.fundoobackendapi.model.Label;
import com.fundoobackendapi.responsedto.UserResponseDTO;
import com.fundoobackendapi.serviceint.ILabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/labels")
public class LabelController {
    @Qualifier("labelServiceImp")
    @Autowired
    private ILabelService labelService;

    @PostMapping("/addLabelByNote/{noteId}")
    public UserResponseDTO addLabelToNote(@RequestBody LabelDto labelDto, @PathVariable long noteId,@RequestHeader String token) {
        return labelService.addLabelToNote(labelDto, noteId,token);
    }
    @PutMapping("/map/{noteId}/{labelId}")
    public UserResponseDTO mapLabel(@PathVariable long noteId, @PathVariable long labelId,@RequestHeader String token) {
        return labelService.mapLabelToNote(noteId, labelId,token);
    }
    @DeleteMapping("/delete/{labelId}")
    public UserResponseDTO deleteLabel(@PathVariable long labelId,@RequestHeader String token){
        return labelService.deleteLabel(labelId,token);
    }

    @PutMapping("/update/{labelId}")
    public UserResponseDTO updateLabel(@RequestBody LabelDto labelDto, @PathVariable long labelId, @RequestHeader String token){
        return labelService.updateLabel(labelDto, labelId,token);
    }

    @GetMapping("/getAll/{noteId}")
    public UserResponseDTO getAllLabel(@PathVariable Long noteId,@RequestHeader String token) {
        return labelService.getAllLabel(noteId,token);
    }
}
