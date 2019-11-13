package com.chi.question.service.impl;

import com.chi.question.dao.CommentDao;
import com.chi.question.domain.Comment;
import com.chi.question.service.CommentService;
import com.chi.question.service.QuestionService;
import com.chi.question.service.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private SensitiveWordFilter sensitiveWordFilter;
	@Autowired
	private QuestionService questionService;

	@Override
	public int addComment(Comment c) {
		String content = c.getContent();
		c.setContent(sensitiveWordFilter.filter(HtmlUtils.htmlEscape(content)));
		int count = commentDao.getCount(c.getEntityId(), c.getEntityType());
		questionService.updateCount(c.getEntityId(), count);
		return commentDao.addComment(c);
	}

	@Override
	public Comment getCommentById(int id) {
		return commentDao.selectById(id);
	}

	@Override
	public List<Comment> getCommentListByEntityId(int id, int type) {
		List<Comment> comments = commentDao.selectByEntity(id, type);
		return comments;
	}

	@Override
	public List<Comment> getCommentListByEntityIdWithUser(int id, int type) {
		List<Comment> comments = commentDao.selectByEntityWithUser(id, type);
		return comments;
	}

	@Override
	public int getUserCommentCount(int uid) {
		return commentDao.getUserCommentCount(uid);
	}
}
