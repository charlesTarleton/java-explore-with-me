package ru.practicum.exploreWithMe.commonFiles.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "request_created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "request_event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "request_requester_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus status;
}
