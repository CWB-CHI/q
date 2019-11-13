package com.chi.question.dao;


import com.chi.question.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
	String TABLE_NAME = " comment ";
	String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;


	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addComment(Comment comment);


	@Select({"select ", SELECT_FIELDS, " from comment where entity_id=#{entityId} and entity_type=#{entityType} order by created_date DESC "})
	List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"select count(id) from comment where entity_id=#{entityId} and entity_type=#{entityType}"})
	int getCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

	List<Comment> selectByEntityWithUser(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"select", SELECT_FIELDS, " from comment where id = #{id}"})
	Comment selectById(int id);

	@Select({"select count(id) from comment where user_id=#{user_id}"})
	int getUserCommentCount(int user_id);
}
