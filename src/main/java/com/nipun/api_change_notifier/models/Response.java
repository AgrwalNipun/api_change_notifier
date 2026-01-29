package com.nipun.api_change_notifier.models;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class Response extends BaseEntity {
   
    
int StatusCode = 0;
    
@JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String,Object> body;

    
@JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String,String> headers;

}
