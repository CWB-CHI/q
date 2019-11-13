package com.chi.question.dao;

import com.chi.question.domain.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init.sql")
public class QuestionDaoTest {

	@Autowired
	private QuestionDao questionDao;

	@Test
	public void addQuestion() {
		for (int i = 1000; i < 2; i++) {
			Question question = new Question();
			question.setTitle("TITLE " + i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
			question.setCreatedDate(date);
			question.setContent("this is question NO." + (i + 1));
			question.setUserId(i + 1);
			question.setCommentCount(i);

			questionDao.addQuestion(question);
		}
	}

	@Test
	public void selectById() {
		Question question = questionDao.selectById(1000);
		System.out.println(question);
	}

	@Test
	public void deleteById() {
		questionDao.deleteById(1000);
	}

	@Test
	public void selectLatestQuestion() {
		List<Question> questions = questionDao.selectLatestQuestions(0, 0, 10);
		questions.forEach((i) -> {
			System.out.format("id %d, content %s \n", i.getId(), i.getContent());
		});
	}
}