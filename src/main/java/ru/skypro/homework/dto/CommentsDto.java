package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentsDto {
    private int count;
    private List<CommentDto> results;
}
