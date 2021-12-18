package com.bot.engBot;

import lombok.*;
import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class User {
//    private Chat chat;
    private Long id;
    private String type;
    private String first_name;
    private String username;
    private String role;

    public User(Chat chat){
        this.id = chat.getId();
        this.type = chat.getType();
        this.first_name = chat.getFirstName();
        this.username = chat.getUserName();
        this.role = "user";
    }

//    public String getFirst_name() {
//        return first_name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public String toString(){
//        return this.getClass().getName() + "(id=" +this.id.toString() +
//                ", type=" + this.type +
//                ", first_name=" + this.first_name +
//                ", username=" + this.username +
//                ", role=" + this.role;
//    }

}
