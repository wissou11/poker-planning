package com.example.PokerPlanningBack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "posts")
@NoArgsConstructor
@Getter
@Setter

public class Post {
    @Id
    private String id;
    private EPost name;

    public Post(EPost name){
        this.name=name;
    }


}