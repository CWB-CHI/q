package com.chi.question.service.impl;


import com.chi.question.dao.QuestionDao;
import com.chi.question.domain.Question;
import com.chi.question.service.QuestionService;
import com.chi.question.service.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private SensitiveWordFilter sensitiveWordFilter;

	@Override
	public Question getById(int id) {
		Question question = questionDao.selectById(id);
		return question;
	}

	@Override
	public List<Question> getLatestQuestions(int userId, int offset, int limit) {
		return questionDao.selectLatestQuestions(userId, offset, limit);
	}

	public int addQuestion(Question q) {
		q.setTitle(sensitiveWordFilter.filter(HtmlUtils.htmlEscape(q.getTitle())));
		q.setContent(sensitiveWordFilter.filter(HtmlUtils.htmlEscape(q.getContent())));
		return questionDao.addQuestion(q);
	}

	@Override
	public int updateCount(int id, int count) {
		return questionDao.updateCount(id, count);
	}
}
