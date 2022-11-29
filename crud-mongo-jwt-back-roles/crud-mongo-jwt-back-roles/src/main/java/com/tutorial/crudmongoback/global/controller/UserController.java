package com.tutorial.crudmongoback.global.controller;

import com.tutorial.crudmongoback.global.dto.ShowThrowsDto;
import com.tutorial.crudmongoback.global.dto.ShowUserDto;
import com.tutorial.crudmongoback.global.dto.UpdateUserDto;
import com.tutorial.crudmongoback.global.exceptions.AttributeException;
import com.tutorial.crudmongoback.global.exceptions.ResourceNotFoundException;
import com.tutorial.crudmongoback.security.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/players")
@CrossOrigin
public class UserController {


    @Autowired
    UserEntityService userEntityService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<ShowUserDto>> getAll() {
        return ResponseEntity.ok(userEntityService.getAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateName(@PathVariable("id") int id,@Valid @RequestBody UpdateUserDto dto) throws AttributeException,ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        int _id = userEntityService.extractID(username);

        if(id!=_id){
            throw new BadCredentialsException("don't try it");
        }

        return ResponseEntity.ok( userEntityService.updateName(id,dto));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{id}/games")
    public ResponseEntity<ShowUserDto> addThrow(@PathVariable("id") int id) throws ResourceNotFoundException,AttributeException {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        int _id = userEntityService.extractID(username);

        if(id!=_id){
            throw new BadCredentialsException("don't try it");
        }

        return ResponseEntity.ok(userEntityService.updateThrows(id));
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}/games")
    public ResponseEntity<ShowUserDto> deleteThrows(@PathVariable("id") int id) throws ResourceNotFoundException, AttributeException {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        int _id = userEntityService.extractID(username);

        if(id!=_id){
            throw new BadCredentialsException("don't try it");
        }


        return ResponseEntity.ok( userEntityService.deleteThrows(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}/games")
    public ResponseEntity<ShowThrowsDto> getOne(@PathVariable("id") int id) throws ResourceNotFoundException {

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        String username = authentication.getName();
        int _id = userEntityService.extractID(username);

        if(id!=_id){
            throw new BadCredentialsException("don't try it");
        }

        return ResponseEntity.ok(userEntityService.getOne(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/ranking")
    public ResponseEntity<?> getMediumRanking() throws ResourceNotFoundException {

        return ResponseEntity.ok(userEntityService.findAllSuccess());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/ranking/loser")
    public ResponseEntity<?> getWorstRanking() {

        return ResponseEntity.ok(userEntityService.findMinSuccess().get());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> getBestRanking(){

        return ResponseEntity.ok(userEntityService.findMaxSuccess().get());
    }
}
