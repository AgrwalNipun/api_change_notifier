package com.nipun.api_change_notifier.models;


import java.util.Map;


import com.nipun.api_change_notifier.converters.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Payload  extends BaseEntity  {
   
@Convert(converter = JsonMapConverter.class)
    
@Lob
@Column(columnDefinition = "CLOB")

    Map<String,String> headers;

    
@Convert(converter = JsonMapConverter.class)
@Lob
@Column(columnDefinition = "CLOB")
    Map<String,Object> body;

@Convert(converter = JsonMapConverter.class)
@Lob
@Column(name = "param",columnDefinition = "CLOB")
    Map<String,Object> params;


    
}
