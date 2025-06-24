package com.chat.chatapp.chat;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ActiveUsers {

    private final Set<String> users = ConcurrentHashMap.newKeySet();

    public void addUser(String userName){

        users.add(userName);
    }

    public void removeUser(String userName){

         users.remove(userName);
    }

    public int getUserCount(){

        return users.size(); 
    }

    public Set<String> getUsers(){
        return Collections.unmodifiableSet(users);
    }

}
