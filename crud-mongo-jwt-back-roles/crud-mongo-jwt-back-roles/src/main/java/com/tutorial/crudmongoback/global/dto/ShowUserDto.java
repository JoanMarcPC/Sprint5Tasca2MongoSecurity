package com.tutorial.crudmongoback.global.dto;

import com.tutorial.crudmongoback.security.entity.Throw;
import com.tutorial.crudmongoback.security.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowUserDto {

    private String username;

    private String email;

    List<RoleEnum> roles;

    private LocalDateTime date;

    private double success;

    private ArrayList<Throw> _throws;
}
