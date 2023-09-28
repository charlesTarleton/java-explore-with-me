package ru.practicum.exploreWithMe.privatePackage.service.commentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.NewCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.UpdateCommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.model.Comment;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.utils.CommentMapper;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.UserExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.CommentAuthorException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventNotPublicisedException;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Private.COMMENT_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	@Transactional(readOnly = true)
	public List<CommentDto> getUserComments(Long userId, Integer from, Integer size) {
		log.info(COMMENT_SERVICE_LOG, "получение всех комментариев пользователя с id: ", userId);
		checkUserIsExist(userId);
		Pageable pageable = new ExploreWithMePageable(from, size, Sort.by("id").ascending());
		return commentRepository.getUserComments(userId, pageable).stream()
				.map(CommentMapper::toDto)
				.collect(Collectors.toList());
	}

    public CommentDto addComment(Long userId, NewCommentDto commentDto) {
		log.info(COMMENT_SERVICE_LOG, "добавление комментария ", commentDto);
		User user = checkUserIsExist(userId);
		Event event = checkEventIsExist(commentDto.getEventId());
		checkEventIsPublished(event);
		checkUserIsNotInitiator(userId, event.getInitiator().getId());
		return CommentMapper.toDto(commentRepository.save(CommentMapper.toComment(commentDto, user, event)));
	}

	@Transactional(readOnly = true)
	public CommentDto getComment(Long userId, Long commentId) {
		log.info(COMMENT_SERVICE_LOG, "получение комментария по id: ", commentId);
		checkUserIsExist(userId);
		return CommentMapper.toDto(checkCommentIsExist(commentId));
	}

	public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto commentDto) {
		log.info(COMMENT_SERVICE_LOG, "обновление комментария с id: ", commentId);
		checkUserIsExist(userId);
		Comment oldComment = checkCommentIsExist(commentId);
		checkUserIsCommentAuthor(userId, oldComment.getAuthor().getId());
		return CommentMapper.toDto(commentRepository.save(CommentMapper.toComment(commentDto, oldComment)));
	}

	public void deleteComment(Long userId, Long commentId) {
		log.info(COMMENT_SERVICE_LOG, "удаление комментария с id: ", commentId);
		checkUserIsExist(userId);
		Long authorId = checkCommentIsExist(commentId).getAuthor().getId();
		checkUserIsCommentAuthor(userId, authorId);
		commentRepository.deleteById(commentId);
	}

    private User checkUserIsExist(Long userId) {
        log.info("Начата процедура проверки наличия пользователя с id: {}", userId);
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserExistException("Указанный пользователь не найден"));
    }

    private Event checkEventIsExist(Long eventId) {
        log.info("Начата процедура проверки наличия события с id: {}", eventId);
        return eventRepository.findById(eventId)
				.orElseThrow(() -> new EventExistException("Указанное событие не найдено"));
    }

    private Comment checkCommentIsExist(Long commentId) {
        log.info("Начата процедура проверки наличия комментария с id: {}", commentId);
		return commentRepository.findById(commentId)
				.orElseThrow(() -> new UserExistException("Указанный комментарий не найден"));
    }

	private void checkUserIsNotInitiator(Long userId, Long initiatorId) {
		log.info("Начата процедура проверки несоответствия автора комментария {} и инициатора события {}",
				userId, initiatorId);
		if (userId.equals(initiatorId)) {
			throw new CommentAuthorException("Инициатор события не может оставлять комментарии к своему событию");
		}
	}

	private void checkUserIsCommentAuthor(Long userId, Long authorId) {
		log.info("Начата процедура проверки соответствия пользователя {} и автора комментария {}",
				userId, authorId);
		if (!userId.equals(authorId)) {
			throw new CommentAuthorException("Изменять или удалять комментарий может только пользователь, " +
					"его написавший");
		}
	}

	private void checkEventIsPublished(Event event) {
        if (!event.getStateAction().equals(EventState.PUBLISHED)) {
            throw new EventNotPublicisedException("Указанное событие не опубликовано");
        }
    }
}
