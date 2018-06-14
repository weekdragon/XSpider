package cn.weekdragon.xspider.repository;

import org.springframework.data.repository.CrudRepository;

import cn.weekdragon.xspider.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findUserByUserName(String userName);
	
}
