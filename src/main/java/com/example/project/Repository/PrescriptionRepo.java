package com.example.project.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.prescription;

@Repository
public interface PrescriptionRepo extends JpaRepository<prescription, Integer>{
    @Query(value = "SELECT * FROM prescription b WHERE b.reservation_id= ?1",nativeQuery = true)
    prescription findByreservation_id(int id);

    @Query(value = "INSERT INTO `childrencare_system`.`prescription`(`content`,`reservation_id`) VALUES (:c,:r)", nativeQuery = true)
        void Add(@Param("c") String content,
                        @Param("r") Integer rid);

    
}
