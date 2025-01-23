package com.example.PokerPlanningBack.repository;

import com.example.PokerPlanningBack.model.EPost;
import com.example.PokerPlanningBack.model.Post;
import com.example.PokerPlanningBack.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Rayen Benoun
 */
public interface IUserRepository extends MongoRepository<User,String> {
    @Query("{'user': ?0}")
    Optional<User> findUserBy(String user);

    Optional<User>findByUserName(String username);
    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    List<User> findByPost(EPost post);

    List<User>findByPostName(String name);

    //List<User> findByPosts_Name(EPost post);

    List<User> findAllById(List<String> ids);


}
