package com.valencia.meal.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;

    private long seq;

    public DatabaseSequence() {}

}
