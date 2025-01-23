package com.example.PokerPlanningBack.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String type= "Bearer";
    private String id;
    private String username;

    private String email;
    private List<String>posts;


    public JwtResponse(String accessToken,String id,String username,String email,List<String> posts){
        this.token=accessToken;
        this.id=id;
        this.username=username;
        this.email=email;
        this.posts=posts;
    }



}
