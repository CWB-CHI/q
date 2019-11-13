package com.chi.question.service;

import com.chi.question.domain.Question;

import java.util.List;

public interface QuestionService {

	public Question getById(int id);

	public List<Question> getLatestQuestions(int userId, int offset, int limit);

	public int addQuestion(Question q);

	public int updateCount(int id, int count);

}
