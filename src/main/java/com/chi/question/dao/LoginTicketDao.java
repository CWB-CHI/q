package com.chi.question.dao;


import com.chi.question.domain.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketDao {
	public static final String TABLE_NAME = " login_ticket ";
	public static final String INSERT_FIELDS = " user_id,expired,status,ticket ";
	public static final String SELECT_FIELDS = " id," + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{userId},#{expired},#{status},#{ticket})"})
	public int addLoginTicket(LoginTicket ticket);


	@Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where ticket = #{ticket}"})
	public LoginTicket selectByTicket(@Param("ticket") String ticket);

	@Update({"update", TABLE_NAME, " set status = 1 where ticket=#{ticket}"})
	public void disable(@Param("ticket") String ticket);
}
