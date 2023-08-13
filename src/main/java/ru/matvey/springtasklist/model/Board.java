package ru.matvey.springtasklist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String _id;
    private String title;
    private User owner;
    private ArrayList<String> tasks;
}
