package com.fundoobackendapi.model;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NOTE")
@NoArgsConstructor
public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long noteId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATDATE")
    private String creatDate;

    @Column(name = "USERID")
    private long userId;

    @Column(name = "TRASH")
    private String trashStatus = "untrash";

    @Column(name = "PIN")
    private String pinStatus = "unpin";

    @Column(name = "ARCHIEVE")
    private String archieveStatus = "unarchieve";

    @Column(name = "COLOUR")
    private String color="white";

    @Column(name = "REMINDER")
    private String reminder;

    @ManyToMany(cascade = CascadeType.ALL)
    @Column(name = "NOTEID")
    private List<Label> labels = new ArrayList<>();

    @ManyToMany(mappedBy = "colaboratedNotes")
    private List<User> colaboratedUsers;

    public List<User> getColaboratedUsers() {
        return colaboratedUsers;
    }

    public void setColaboratedUsers(List<User> colaboratedUsers) {
        this.colaboratedUsers = colaboratedUsers;
    }
    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegDate() {
        return creatDate;
    }

    public void setRegDate(String regDate) {
        this.creatDate = regDate;
    }

  /*  public long getUserId() {
        return userId;
    }*/

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTrashStatus() {
        return trashStatus;
    }

    public void setTrashStatus(String trashStatus) {
        this.trashStatus = trashStatus;
    }

    public String getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(String pinStatus) {
        this.pinStatus = pinStatus;
    }

    public String getArchieveStatus() {
        return archieveStatus;
    }

    public void setArchieveStatus(String archieveStatus) {
        this.archieveStatus = archieveStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}
