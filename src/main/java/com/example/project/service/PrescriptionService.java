package com.example.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.Repository.PrescriptionRepo;
import com.example.project.entity.prescription;
import com.example.project.entity.service;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepo repo;

    public prescription getByRid(int id){
        return repo.findByreservation_id(id);
    }
    public void Add1(prescription p){
        repo.save(p);
        
    }

    public void update(prescription p){
        repo.save(p);
        
    }
}