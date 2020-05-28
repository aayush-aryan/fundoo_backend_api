package com.fundoobackendapi.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundoobackendapi.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String modDate;
    private String regDate;
    private Integer isVerified=0;
    private String status="inactive";
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "colaborator_note", joinColumns = { @JoinColumn(name = "USERID") }, inverseJoinColumns = {
            @JoinColumn(name = "NOTEID") })
    private List<Note> colaboratedNotes;

    public User(UserDto userDto) {
        this.firstName=userDto.firstName;
        this.lastName=userDto.lastName;
        this.password=userDto.password;
        this.email=userDto.email;
    }
    public List<Note> getColaboratedNotes() {
        return colaboratedNotes;
    }

    public void setColaboratedNotes(List<Note> colaboratedNotes) {
        this.colaboratedNotes = colaboratedNotes;
    }
    public String getModDate() {
        return this.modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRegDate() {
        return this.regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }
    public void setIsVerified(Integer status){
        this.isVerified = status;
    }
    public Integer getIsVerified(){
        return this.isVerified;
    }
    public Long getId() {
        return this.userId;
    }
    public Object getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", modDate='" + modDate + '\'' +
                ", regDate='" + regDate + '\'' +
                ", isVerified=" + isVerified +
                ", status='" + status + '\'' +
                '}';
    }
}
