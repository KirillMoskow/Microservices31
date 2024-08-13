package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String name;
    private String lastName;
    private Integer age;
    private Long id;
public User(Long id, String name, String lastName, Integer age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.id = id;
    }
    public User() {
    }
}
