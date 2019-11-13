package com.chi.question.service;

import com.chi.question.domain.Comment;

import java.util.List;

public interface CommentService {

	public int addComment(Comment c);

	public Comment getCommentById(int id);

	public List<Comment> getCommentListByEntityId(int id, int type);

	public List<Comment> getCommentListByEntityIdWithUser(int id, int type);

	int getUserCommentCount(int uid);
}
