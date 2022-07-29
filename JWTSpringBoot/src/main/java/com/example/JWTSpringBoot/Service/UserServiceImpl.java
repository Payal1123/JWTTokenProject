package com.example.JWTSpringBoot.Service;

import com.example.JWTSpringBoot.Model.User;
import com.example.JWTSpringBoot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder pwdencoder;


    @Autowired
    private UserRepository userRepository;


    @Override
    public Integer saveUSer(User user)
    {
        user.setPassword(
        pwdencoder.encode(user.getPassword())
        );
        return userRepository.save(user).getId();
    }

    @Override
    public Optional<User> findbyUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {

        Optional<User> opt = findbyUsername(username);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User not exist");

        User user = opt.get();

        return new org.springframework.security.core.userdetails.User(
                username,
               user.getPassword(),
                user.getRoles().stream()
                        .map(role->new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList())
        );



    }
}
