package com.example.jwtspring.token;

import com.example.jwtspring.enums.TokenType;
import com.example.jwtspring.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Data
public class Token {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer Id;

    private  String token;
    @Enumerated (EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

  @ManyToOne()
  @JoinColumn(name = "user_Id" )
    private User user;
}
