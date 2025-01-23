package com.example.PokerPlanningBack.controller;

import com.example.PokerPlanningBack.model.EPost;
import com.example.PokerPlanningBack.model.Post;
import com.example.PokerPlanningBack.model.Statut;
import com.example.PokerPlanningBack.model.User;
import com.example.PokerPlanningBack.payload.request.LoginRequest;
import com.example.PokerPlanningBack.payload.request.SignupRequest;
import com.example.PokerPlanningBack.payload.response.JwtResponse;
import com.example.PokerPlanningBack.payload.response.MessageResponse;
import com.example.PokerPlanningBack.repository.IPostRepository;
import com.example.PokerPlanningBack.repository.IUserRepository;
import com.example.PokerPlanningBack.security.jwt.JwtUtils;
import com.example.PokerPlanningBack.security.services.UserDetailsImpl;
import com.example.PokerPlanningBack.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.PokerPlanningBack.security.jwt.JwtUtils.logger;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> posts = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Check if the user's status is 'DISABLE'
        if ("DISABLE".equals(userDetails.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is disabled");
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                posts));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strPosts = signUpRequest.getPosts();
        Set<Post> posts = new HashSet<>();

        if (strPosts == null) {
            Post userPost = postRepository.findByName(EPost.DEVELOPPER)
                    .orElse(null);
            if (userPost == null) {
                logger.error("Error: Post 'DEVELOPPER' is not found.");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Post 'DEVELOPPER' is not found."));
            }
            posts.add(userPost);
        } else {
            for (String post : strPosts) {
                Post postEntity;
                switch (post) {
                    case "PRODUCTOWNER":
                        postEntity = postRepository.findByName(EPost.PRODUCTOWNER)
                                .orElse(null);
                        if (postEntity == null) {
                            logger.error("Error: Post 'PRODUCTOWNER' is not found.");
                            return ResponseEntity
                                    .badRequest()
                                    .body(new MessageResponse("Error: Post 'PRODUCTOWNER' is not found."));
                        }
                        posts.add(postEntity);
                        break;
                    default:
                        postEntity = postRepository.findByName(EPost.DEVELOPPER)
                                .orElse(null);
                        if (postEntity == null) {
                            logger.error("Error: Post 'DEVELOPPER' is not found.");
                            return ResponseEntity
                                    .badRequest()
                                    .body(new MessageResponse("Error: Post 'DEVELOPPER' is not found."));
                        }
                        posts.add(postEntity);
                }
            }
        }

        user.setPosts(posts);
        user.setStatut(Statut.ENABLE); // Set the status to ENABLE
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }






}
