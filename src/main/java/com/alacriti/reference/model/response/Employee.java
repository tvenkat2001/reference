package com.alacriti.reference.model.response;



import lombok.*;

@Builder
public record Employee (Long id,
   String firstName,
     String lastName,
     String email,
   Double salary,
    String department
){
}
