package com.tutorial.crudmongoback.security.service;

import com.tutorial.crudmongoback.global.dto.ShowThrowsDto;
import com.tutorial.crudmongoback.global.dto.ShowUserDto;
import com.tutorial.crudmongoback.global.dto.UpdateUserDto;
import com.tutorial.crudmongoback.global.exceptions.AttributeException;
import com.tutorial.crudmongoback.global.exceptions.ResourceNotFoundException;
import com.tutorial.crudmongoback.global.utils.Operations;
import com.tutorial.crudmongoback.security.dto.CreateUserDto;
import com.tutorial.crudmongoback.security.dto.JwtTokenDto;
import com.tutorial.crudmongoback.security.dto.LoginUserDto;
import com.tutorial.crudmongoback.security.entity.Throw;
import com.tutorial.crudmongoback.security.entity.UserEntity;
import com.tutorial.crudmongoback.security.enums.RoleEnum;
import com.tutorial.crudmongoback.security.jwt.JwtProvider;
import com.tutorial.crudmongoback.global.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEntityService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;


    public UserEntity create(CreateUserDto dto) throws AttributeException {
        if(dto.getUsername().equalsIgnoreCase("")) {
            dto.setUsername("anonymous"+ Math.random()*10);
        }
        if (userEntityRepository.existsByUsername(dto.getUsername()))
            throw new AttributeException("username already in use");
        if (userEntityRepository.existsByEmail(dto.getEmail()))
            throw new AttributeException("email already in use");
        return userEntityRepository.save(mapUserFromCreateDto(dto));
    }

    public JwtTokenDto login(LoginUserDto dto) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new JwtTokenDto(token);
    }

    public ShowThrowsDto getOne(int id) throws ResourceNotFoundException {
        Optional<UserEntity> userEntity = userEntityRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new ResourceNotFoundException("not found");
        }


        return new ShowThrowsDto(userEntity.get().get_throws());
    }

    public List<ShowUserDto> getAll() {
        List<UserEntity> users = userEntityRepository.findAll();
        List<ShowUserDto> dtos = new ArrayList<ShowUserDto>();
        for (UserEntity user : users) {
            dtos.add(mapShowDtoFromUser(user));

        }
        return dtos;
    }
    public ShowUserDto deleteThrows(int id) throws ResourceNotFoundException, AttributeException {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        userEntity.removeThrows();
        return mapShowDtoFromUser(userEntityRepository.save(userEntity));
    }

    public ShowUserDto updateThrows(int id) throws ResourceNotFoundException, AttributeException {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        userEntity.addThrow(new Throw());
        return mapShowDtoFromUser(userEntityRepository.save(userEntity));
    }
    public ShowUserDto updateName(int id, UpdateUserDto dto) throws ResourceNotFoundException, AttributeException {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        if (userEntityRepository.existsByUsername(dto.getUsername())) {
            throw new AttributeException("name already in use");
        }
        userEntity.setUsername(dto.getUsername());

        return mapShowDtoFromUser(userEntityRepository.save(userEntity));
    }



    public ShowUserDto delete(int id) throws ResourceNotFoundException {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
        userEntityRepository.delete(userEntity);
        return mapShowDtoFromUser(userEntity);
    }
    public Optional<ShowUserDto> findMaxSuccess() {
        return Optional.of(mapShowDtoFromUser(userEntityRepository.findFirstByOrderBySuccessDesc()));
    }


    public Optional<ShowUserDto> findMinSuccess() {
        return Optional.of(mapShowDtoFromUser(userEntityRepository.findFirstByOrderBySuccessAsc()));
    }


    public double findAllSuccess() {
        List<UserEntity> users = userEntityRepository.findAllSuccess();
        double average = 0;

        if (users.isEmpty()){
            average = -1;
        }
        for (int i = 0; i < users.size(); i++) {
            average += users.get(i).getSuccess();

        }
        average /= users.size();
        return average;

    }
    public int extractID (String username) throws ResourceNotFoundException{
        Optional<UserEntity> userEntity = userEntityRepository.findByUsername(username);
        if(userEntity.isEmpty()){
            throw new ResourceNotFoundException("not found");
        }
            return userEntity.get().getId();


    }

    // private methods
    private UserEntity mapUserFromCreateDto(CreateUserDto dto) {
        int id = Operations.autoIncrement(userEntityRepository.findAll());
        String password = passwordEncoder.encode(dto.getPassword());
        List<RoleEnum> roles =
                dto.getRoles().stream().map(rol -> RoleEnum.valueOf(rol)).collect(Collectors.toList());
        return new UserEntity(id, dto.getUsername(), dto.getEmail(), password, roles);
    }

    private ShowUserDto mapShowDtoFromUser(UserEntity userEntity) {
        return new ShowUserDto(userEntity.getUsername(), userEntity.getEmail(), userEntity.getRoles(), userEntity.getDate(), userEntity.getSuccess(), userEntity.get_throws());
    }




}
