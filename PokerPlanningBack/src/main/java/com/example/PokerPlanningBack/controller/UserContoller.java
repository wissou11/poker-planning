package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.exception.UserCollectionException;
import com.example.PokerPlanningBack.model.Statut;
import com.example.PokerPlanningBack.model.User;
import com.example.PokerPlanningBack.payload.response.MessageResponse;
import com.example.PokerPlanningBack.repository.IUserRepository;
import com.example.PokerPlanningBack.service.IUserService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/**
 * Rayen Benoun
 */
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserContoller {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?>getAllTodos(){
        List<User> users=userService.getAllUsers();
        return new ResponseEntity<>(users,users.size()>0 ? HttpStatus.OK:HttpStatus.NOT_FOUND);
    }


    @PostMapping("/create")
    public ResponseEntity<?>createUser(@RequestBody User user){
        try {
            userService.createUser(user);
            return new ResponseEntity<User>(user,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch(UserCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?>getSingleUser(@PathVariable("id")String id){
        try {
            return new ResponseEntity<>(userService.getSingleUser(id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?>updateById(@PathVariable("id")String id,@RequestBody User user){
        try {
            userService.updateUser(id,user);
            return new ResponseEntity<>("update user with id ="+id,HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (UserCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteById(@PathVariable("id")String id){
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>("succefully deleted with id"+id,HttpStatus.OK);
        }catch (UserCollectionException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping("/developers")
    public ResponseEntity<?> getAllDevelopers() {
        List<User> developers = userService.getAllDeveloppers();
        return new ResponseEntity<>(developers, developers.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @PutMapping("/developers/{id}/enable")
    public ResponseEntity<?> enableDeveloper(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatut(Statut.ENABLE);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Developer enabled successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Developer not found!"));
    }

    @PutMapping("/developers/{id}/disable")
    public ResponseEntity<?> disableDeveloper(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatut(Statut.DISABLE);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Developer disabled successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Developer not found!"));
    }

    @CrossOrigin
    @GetMapping("/developers/by-ids/{ids}")
    public ResponseEntity<?> getDevelopersByIds(@PathVariable List<String> ids) {
        System.out.println("Received ids: " + ids);
        try {
            List<User> developers = userService.getDevelopersByIds(ids);
            return new ResponseEntity<>(developers, developers.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
