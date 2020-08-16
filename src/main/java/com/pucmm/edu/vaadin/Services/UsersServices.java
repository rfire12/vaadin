package com.pucmm.edu.vaadin.Services;

import com.pucmm.edu.vaadin.Models.MyUser;
import com.pucmm.edu.vaadin.Repositories.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UsersServices {
    @Autowired
    private IUsersRepository usersRepository;

    public MyUser createUser(String name, String email, String password) throws Exception {
        try {
            return usersRepository.save(new MyUser(name, email, password));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void editUser(MyUser user) throws Exception {
        try {
            usersRepository.save(user);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public boolean validate(String email, String password) {
        MyUser user = usersRepository.findByEmailAndPassword(email, password);
        return (user != null);
    }

    public List<MyUser> listUsers() {
        return usersRepository.findAll();
    }


    public List<MyUser> listUsersWithPagination(int offset, int limit){
        return usersRepository.paginate(offset, limit);
    }

    @Transactional
    public Integer totalUsers() {
        return usersRepository.myCount();
    }

    @Transactional
    public void removeUser(Integer userId){
        usersRepository.deleteById(userId);
    }
}
