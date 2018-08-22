package com.curisprofound.spring.reactivestackdemo.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Publisher {
    @Id
    private String id;
    private String name;
    private String postalCode;
    private List<String> bookIds;

    public List<String> getBookIds(){
        if(bookIds == null)
            bookIds = new ArrayList<>();
        return bookIds;
    }
}
