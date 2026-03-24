package com.anush.aiproject.entity;

import com.anush.aiproject.shared.constants.MessageType;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="messages")
public class Message extends BaseEntity {
    
    private String title;

    private String subject;

    private MessageType type;

    private String message;

    @ManyToOne
    @JoinColumn(name = "profile_id",nullable = false)
    private Profile profile;


}
