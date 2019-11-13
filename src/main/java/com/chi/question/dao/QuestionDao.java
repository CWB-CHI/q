package com.chi.question.dao;


import com.chi.question.domain.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {

	String TABLE_NAME = "question";
	String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;


	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
	public int addQuestion(Question q);

	@Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where id = #{id}"})
	public Question selectById(int id);

	@Delete({"delete from ", TABLE_NAME, "where id = #{id}"})
	public int deleteById(int id);

	@Update({"update " + TABLE_NAME + " set comment_count=#{count} where id = #{id}"})
	public int updateCount(@Param("id") int id, @Param("count") int count);

	public List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

}
