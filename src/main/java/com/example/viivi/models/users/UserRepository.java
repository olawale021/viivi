package com.example.viivi.models.users;


import com.example.viivi.models.users.UserModel;
import com.example.viivi.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
    List<UserModel> findByRole(Role role);
}