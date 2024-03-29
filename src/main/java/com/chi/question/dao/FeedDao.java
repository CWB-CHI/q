package com.chi.question.dao;


import com.chi.question.domain.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedDao {

	String TABLE_NAME = " feed ";
	String INSERT_FIELDS = " user_id, data, created_date, type ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{data},#{createdDate},#{type})"})
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	int addFeed(Feed feed);

	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
	Feed getFeedById(int id);

	List<Feed> selectUserFeeds(@Param("maxId") int maxId, @Param("userIds") List<Integer> userIds, @Param("count") int count);

}
