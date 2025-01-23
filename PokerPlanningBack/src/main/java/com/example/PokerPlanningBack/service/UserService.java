package com.example.PokerPlanningBack.service;

import com.example.PokerPlanningBack.exception.UserCollectionException;
import com.example.PokerPlanningBack.model.EPost;
import com.example.PokerPlanningBack.model.Post;
import com.example.PokerPlanningBack.model.User;
import com.example.PokerPlanningBack.repository.IPostRepository;
import com.example.PokerPlanningBack.repository.IUserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * Rayen Benoun
 */
@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;


    @Override
    public void createUser(User user) throws ConstraintViolationException, UserCollectionException {
        Optional<User> userOptional=userRepository.findUserBy(user.getUserName());
        if (userOptional.isPresent()){
            throw new UserCollectionException(UserCollectionException.UserAlreadyExists());
        }else {
            user.setCreatedAt(new Date(System.currentTimeMillis()));
            userRepository.save(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users=userRepository.findAll();
        if (users.size()>0){
            return users;
        }else {
            return new ArrayList<User>();
        }
    }


    @Override
    public User getSingleUser(String id) throws UserCollectionException {
        Optional<User>optionalUser=userRepository.findById(id);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void updateUser(String id, User user) throws UserCollectionException {
        Optional<User>optionalUser=userRepository.findById(id);
        if (optionalUser.isPresent()){
            User userUpdate=optionalUser.get();
            userUpdate.setUserName(user.getUserName());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setPassword(user.getPassword());
            userUpdate.setPost(user.getPost());
            userUpdate.setStatut(user.getStatut());
            userUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            userRepository.save(userUpdate);
        }else {
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteUserById(String id) throws UserCollectionException {
        Optional<User>optionalUser=userRepository.findById(id);
        if (!optionalUser.isPresent()){
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        }else {
            userRepository.deleteById(id);
        }
    }

    /*@Override
    public List<User> getAllDeveloppers() {
        return userRepository.findByPost(EPost.DEVELOPPER);
    }*/


    /*@Override
    public List<User> getAllDeveloppers() {
        List<User> developers = userRepository.findByPost(EPost.DEVELOPPER);
        if (developers.size() > 0) {
            return developers;
        } else {
            return new ArrayList<>(); // Return an empty list if no developers are found
        }
    }*/
    @Override
    public List<User> getAllDeveloppers() {
        List<User> allUsers = userRepository.findAll();
        List<User> developers = new ArrayList<>();
        for (User user : allUsers) {
            for (Post postRef : user.getPosts()) {
                Optional<Post> post = postRepository.findById(postRef.getId());
                if (post.isPresent() && post.get().getName() == EPost.DEVELOPPER) {
                    developers.add(user);
                    break;
                }
            }
        }
        return developers;
    }

    @Override
    public List<User> getDevelopersByIds(List<String> ids) {
        // Récupérer tous les utilisateurs
        List<User> allUsers = userRepository.findAll();

        // Créer une liste pour stocker les développeurs trouvés
        List<User> developers = new ArrayList<>();

        // Parcourir tous les utilisateurs
        for (User user : allUsers) {
            // Si l'utilisateur a un ID correspondant à l'un des IDs fournis
            if (ids.contains(user.getId())) {
                // Vérifier si l'utilisateur est un développeur
                for (Post postRef : user.getPosts()) {
                    Optional<Post> post = postRepository.findById(postRef.getId());
                    if (post.isPresent() && post.get().getName() == EPost.DEVELOPPER) {
                        // Ajouter à la liste des développeurs
                        developers.add(user);
                        break;
                    }
                }
            }
        }
        return developers;
    }






}
