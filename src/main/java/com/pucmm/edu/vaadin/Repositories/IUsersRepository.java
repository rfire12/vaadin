package com.pucmm.edu.vaadin.Repositories;

import com.pucmm.edu.vaadin.Models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUsersRepository extends JpaRepository<MyUser, Integer> {
    @Query("select user from MyUser user where user.email = :email and user.password = :password")
    MyUser findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query("select count(user) from MyUser user")
    Integer myCount();

    @Query(value = "select * from My_User user offset(?1) limit(?2)", nativeQuery = true)
    List<MyUser> paginate(int offset, int limit);
}
