package com.example.PokerPlanningBack.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Rayen Benoun
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection ="users")
public class User {
    @Id
    private String id;
    @NonNull
    private String userName;
    @NonNull
    @Size(max = 50)
    private String email;
    @NonNull
    @Size(max = 120)
    private String password;
    @NonNull
    private EPost post;
    // Add getter for statut
    @Getter
    @NonNull
    private Statut statut; // Ensure this is the correct field

    private Date createdAt;
    private Date updatedAt;

    @DBRef
    private Set<Post> posts = new HashSet<>();
    

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

}
