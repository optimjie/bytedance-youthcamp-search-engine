package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    private Integer id;

    private String url;

    private String caption;
}
