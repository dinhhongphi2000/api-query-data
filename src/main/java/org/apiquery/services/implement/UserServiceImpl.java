package org.apiquery.services.implement;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.apiquery.dtos.*;
import org.apiquery.entities.*;
import org.apiquery.services.*;
import org.apiquery.repositories.*;
import org.apiquery.shared.exceptions.ServiceException;
import org.apiquery.shared.utils.EntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserDTO, UserEntity, Integer> implements UserService {
    
    private UserRepository userRepo;
    
    @Autowired
    public UserServiceImpl(UserRepository baseRepo) {
        super(baseRepo);
    }
}