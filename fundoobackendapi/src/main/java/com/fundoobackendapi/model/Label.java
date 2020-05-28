package com.fundoobackendapi.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LABEL")
@NoArgsConstructor
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long labelId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATDATE")
    private String creatDate;

    @Column(name = "MODDATE")
    private String modDate;

    @Column(name = "NOTEID")
    private long noteId;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "NOTEID")
    private List<Note> notes = new ArrayList<>();

    public long getLabelId() {
        return labelId;
    }

    public long getNoteId() {
        return noteId;
    }
    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCreatDate() {
        return creatDate;
    }
    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }
    public String getModDate() {
        return modDate;
    }
    public void setModDate(String modDate) {
        this.modDate = modDate;
    }
}
