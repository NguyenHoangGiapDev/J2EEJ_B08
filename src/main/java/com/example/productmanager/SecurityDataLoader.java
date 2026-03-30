package com.example.productmanager;

import com.example.productmanager.entity.Account;
import com.example.productmanager.entity.Role;
import com.example.productmanager.repository.AccountRepository;
import com.example.productmanager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SecurityDataLoader {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(RoleRepository roleRepository,
                              AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        if (accountRepository.findByLoginName("admin").isEmpty()) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);

            Account admin = new Account();
            admin.setLoginName("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRoles(adminRoles);

            accountRepository.save(admin);
        }

        if (accountRepository.findByLoginName("user1").isEmpty()) {
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);

            Account user = new Account();
            user.setLoginName("user1");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(userRoles);

            accountRepository.save(user);
        }
    }
}