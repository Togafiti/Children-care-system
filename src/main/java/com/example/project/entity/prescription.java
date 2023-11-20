package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class prescription {
    @Id
    private int id;
    private String content;
    private int reservation_id;
    public prescription() {
    }
    public prescription(int id, String content, int reservation_id) {
        this.id = id;
        this.content = content;
        this.reservation_id = reservation_id;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getReservation_id() {
        return reservation_id;
    }
    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    
}
