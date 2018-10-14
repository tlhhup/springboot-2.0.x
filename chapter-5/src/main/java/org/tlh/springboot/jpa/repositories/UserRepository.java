package org.tlh.springboot.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tlh.springboot.jpa.entity.User;

/**
 * @author huping
 * @desc
 * @date 18/10/14
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
