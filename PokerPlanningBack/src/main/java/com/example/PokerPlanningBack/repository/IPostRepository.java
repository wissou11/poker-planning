package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.EPost;
import com.example.PokerPlanningBack.model.Post;
import com.example.PokerPlanningBack.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IPostRepository extends MongoRepository<Post,String> {
    @Query("{'name': ?0}")
    Optional<Post>findByName(EPost name);




}
