package ru.practicum.exploreWithMe.commonFiles.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "event_category_id")
    private Category category;

    @Column(name = "event_confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "event_created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Column(name = "event_description")
    private String description;

    @Column(name = "event_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "event_initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "event_location_id")
    private Location location;

    @Column(name = "event_paid")
    private Boolean paid;

    @Column(name = "event_participant_limit")
    private Long participantLimit;

    @Column(name = "event_published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "event_request_moderation")
    private Boolean requestModeration;

    @Column(name = "event_state_action")
    @Enumerated(EnumType.STRING)
    private EventState stateAction;

    @Column(name = "event_title")
    private String title;

    @Column(name = "event_views")
    private Integer views;
}
