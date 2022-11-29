package com.tutorial.crudmongoback.security.entity;

import com.tutorial.crudmongoback.global.entity.EntityId;
import com.tutorial.crudmongoback.security.enums.RoleEnum;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class UserEntity extends EntityId {

    private String username;
    private String email;
    private String password;
    List<RoleEnum> roles;

    private LocalDateTime date;

    private double success;

    private ArrayList<Throw> _throws;


    public UserEntity() {
    }

    public UserEntity(int id, String username, String email, String password, List<RoleEnum> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.date = LocalDateTime.now();
        this.success = 0;
        this._throws = new ArrayList<Throw>();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getSuccess() {
        return success;
    }

    public void setSuccess(double success) {
        this.success = success;
    }

    public ArrayList<Throw> get_throws() {
        return _throws;
    }

    public void set_throws(ArrayList<Throw> _throws) {
        this._throws = _throws;
    }

    public void addThrow(Throw _throw) {
        this._throws.add(_throw);
        double wins = 0;
        for (Throw _throw1 : _throws) {
            if (_throw1.isWin()) {
                wins += 1;
            }
        }
        wins /= this._throws.size();
        this.success = wins*100;
    }
    public void removeThrows (){
        this._throws = new ArrayList<>();
        this.success = 0;
    }
}
