package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Mapping(source = "author.id", target = "user")
    @Mapping(target = "authorImage", expression = "java(image(comment))")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "id", target = "pk")
    CommentDto convertToCommentDto(Comment comment);

    default String image(Comment comment) {
        int id = comment.getUser().getId();
        return "/users/" + id + "/image";
    }

    Comment convertToComment(CreateOrUpdateCommentDto createOrUpdateCommentDto);

    default CommentsDto toCommentsDto(List<Comment> comments) {
        return CommentsDto.builder()
                .count(comments.size())
                .results(convertToCommentDtoList(comments))
                .build();
    }

    @Mapping(source = "author.id", target = "author")
    @Mapping(target = "authorImage", expression = "java(image(comment))")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "id", target = "pk")
    Comment updateComment(CreateOrUpdateCommentDto createOrUpdateCommentDto, @MappingTarget Comment comment, Ad ad);

    List<CommentDto> convertToCommentDtoList(Collection<Comment> commentCollection);

    List<CommentDto> toCommentsListDto(Collection<Comment> commentCollection);
}
