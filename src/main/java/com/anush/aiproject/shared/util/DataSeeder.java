package com.anush.aiproject.shared.util;

import com.anush.aiproject.repository.ProjectRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.anush.aiproject.entity.Project;
import com.anush.aiproject.entity.Task;
import com.anush.aiproject.entity.User;
import com.anush.aiproject.repository.UserRepository;
import com.anush.aiproject.shared.constants.UserRole;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Profile("!test")
@Component
public class DataSeeder implements ApplicationRunner {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("Data already seeded. So, skipping");
            return;
        }
        log.info("Data sedding...");
        seedAdmin();
        User anush= seedUser();
        seedUserContent(anush);
        log.info("Data seeded sucessfully");
    }

    private void seedAdmin(){
        User admin = new User();
        
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin@123"));
        admin.setRole(UserRole.ADMIN);
        admin.setEmailVerified(true);
        admin.setSuspended(false);

        userRepository.save(admin);
        log.info("Admin seeded with email as admin@gmail.com and password is admin@123");

    }

    private User seedUser() {
        User user = new User();

        user.setEmail("anush.stha232@gmail.com");
        user.setPassword(passwordEncoder.encode("Anush@123"));
        user.setEmailVerified(true);
        user.setSuspended(false);
        user.setRole(UserRole.USER);

        userRepository.save(user);
        log.info("User seeded with email as anush.stha232@gmail.com and password is Anush@123");
        return user;

    }

    private void seedUserContent(User user){

        User userdata = userRepository.findByEmail(user.getEmail()).orElseThrow();
        seedUserProjects(userdata);


    }

    private void seedUserProjects(User user){
        List<Project> projects = List.of(
            project(1L,user,"Bca nothing much","hahaha"),
            project(2L,user,"lakfd","hahaha"),
            project(3L,user,"ltird","hahaha"),
            project(4L,user,"fourth ","hahaha")
            
        );
        projectRepository.saveAll(projects);
    }

    //helpers
    private Project project(Long id,User user, String title, String description){
        Project pr= new Project();
        pr.setId(id);
        pr.setTitle(title);
        pr.setDescription(description);
        pr.setUser(user);
        return pr;
    }

    private Task task(Long id, User user,Project project, String title, String description){
        Task ts = new Task();
        //neeed to add here 
        return ts;
    }
}
