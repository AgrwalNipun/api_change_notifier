package com.nipun.api_change_notifier.models;


import java.util.Map;
import com.nipun.api_change_notifier.converters.JsonMapConverter;
import com.nipun.api_change_notifier.models.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class ChangeLog extends BaseEntity{

        

    String pastCommitId;
    String currentCommitId;


    @Enumerated(EnumType.STRING)
    Status status;



    @ManyToOne
    @JoinColumn(name = "api_id")
    private Api api;

@Convert(converter = JsonMapConverter.class)
@Lob
@Column(columnDefinition = "CLOB")

    Map<String,Object> pastValue;
    
@Convert(converter = JsonMapConverter.class)
@Lob
@Column(columnDefinition = "CLOB")

    Map<String,Object> currentValue;

    

    @Override
    public String toString() {
        return "ChangeLog [pastCommitId=" + pastCommitId + ", currentCommitId=" + currentCommitId + ", status=" + status
                + ", api=" + api.toString() + ", pastValue=" + pastValue + ", currentValue=" + currentValue + "]";
    }
    
}
