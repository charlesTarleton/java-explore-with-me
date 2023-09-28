package ru.practicum.exploreWithMe.commonFiles.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Common.PATTERN;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_text")
    private String text;

    @OneToOne
    @JoinColumn(name = "comment_author_id", referencedColumnName = "user_id")
    private User author;

    @OneToOne
    @JoinColumn(name = "comment_event_id", referencedColumnName = "event_id")
    private Event event;

    @Column(name = "comment_created_on")
    @JsonFormat(pattern = PATTERN)
    private LocalDateTime createdOn;

	@Column(name = "comment_edited")
    private Boolean edited;
}
