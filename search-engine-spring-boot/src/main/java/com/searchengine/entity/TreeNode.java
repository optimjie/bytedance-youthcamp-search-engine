package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TreeNode {
    String id;
    String pid;
    String name;
    boolean isLeaf;
    String url;
}
