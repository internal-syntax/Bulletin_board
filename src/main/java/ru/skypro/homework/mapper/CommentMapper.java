package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto outDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getUser().getId());
        commentDto.setAuthorImage(String.format("/users/avatars/%d",comment.getUser().getAvatar().getId()));
        commentDto.setAuthorFirstName(comment.getUser().getFirstName());
        commentDto.setCreatedAt(comment.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public CommentsDto outDtos(List<Comment> commentsList) {
        CommentsDto commentsDto = new CommentsDto();
        List<CommentDto> commentDtoList = commentsList.stream()
                .map(this::outDto)
                .collect(Collectors.toList());
        commentsDto.setResults(commentDtoList);
        commentsDto.setCount(commentDtoList.size());
        return commentsDto;
    }

    public Comment inDto(User user, Ad ad, CreateOrUpdateCommentDto crOrUpdComDto) {
        Comment comment = new Comment();
        comment.setDateTime(LocalDateTime.now());
        comment.setText(crOrUpdComDto.getText());
        comment.setUser(user);
        comment.setAd(ad);
        return comment;
    }
}
