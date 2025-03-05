package com.awstest.examples_labs.lambda.domain;

import lombok.Data;

@Data
public class Character {
    
    private long id;
    private String name;
    private Long healthPoints;
    private String[] skills;
}
