package com.anush.aiproject.controller;


import com.anush.aiproject.entity.Project;
import com.anush.aiproject.repository.AbstractRepository;
import java.util.List;


public interface ProjectController extends AbstractRepository<Project,Long> {

List<Project> findByTitle(String title);
    
}
