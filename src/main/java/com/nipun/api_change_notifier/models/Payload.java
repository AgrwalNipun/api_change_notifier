package com.nipun.api_change_notifier.models;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

@Entity
public class Payload  extends BaseEntity  {
   
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String,String> headers;

    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String,String> body;

    @ElementCollection
    @CollectionTable(
        name = "payload_params",
        joinColumns = @JoinColumn(name = "payload_id")
    )
    @Column(name = "param")
    List<String> params;


    
}
