package org.tlh.springboot.jpa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tlh.springboot.jpa.entity.User;
import org.tlh.springboot.jpa.repositories.UserRepository;
import org.tlh.springboot.jpa.service.UserService;

/**
 * @author huping
 * @desc
 * @date 18/10/14
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user){
        User save = this.userRepository.save(user);
        int i=4/0;
        return save;
    }

}
