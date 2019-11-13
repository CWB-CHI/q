package com.chi.question.dao;

import com.chi.question.domain.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentDaoTest {

	@Autowired
	private CommentDao commentDao;

	@Test
	public void addComment() {
		Comment comment = new Comment();
		comment.setContent("评论c");
		comment.setCreatedDate(new Date());
		comment.setEntityId(1);
		comment.setEntityType(1);
		comment.setStatus(0);
		comment.setUserId(3);
		commentDao.addComment(comment);
	}

	@Test
	public void selectByEntity() {

		List<Comment> comments = commentDao.selectByEntity(1, 1);

		System.out.println(comments.toString());
	}

	@Test
	public void getCount() {
		int count = commentDao.getCount(1, 1);
		System.out.println(count);
	}

	@Test
	public void selectByEntityWithUser() {
		List<Comment> comments = commentDao.selectByEntityWithUser(1, 1);
		comments.forEach(System.out::println);
	}
}