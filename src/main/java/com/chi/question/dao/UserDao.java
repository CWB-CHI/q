package com.chi.question.dao;


import com.chi.question.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

	String TABLE_NAME = " user ";
	String INSERT_FIELDS = " name, password, salt, head_url ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{name},#{password},#{salt},#{headUrl})"})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addUser(User u);

	@Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, " where id=#{id}"})
	public User selectById(int id);

	@Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, " where name=#{name}"})
	public User selectByName(String name);

	@Update({"update", TABLE_NAME, "set password=#{password} where id = #{id}"})
	public int updatePassword(int id, String password);

	@Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
	public int deleteById(int id);
}
