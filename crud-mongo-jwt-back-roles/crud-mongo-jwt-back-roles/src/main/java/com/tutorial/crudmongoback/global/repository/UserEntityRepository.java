package com.tutorial.crudmongoback.global.repository;

import com.tutorial.crudmongoback.security.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    @Query("{id:?0},{success:1}") // no caldria {id}
    double findSuccessById(String id);
    @Query(fields = "{name:1,success:1}")
    UserEntity findFirstByOrderBySuccessDesc();
    @Query(fields = "{name:1,success:1}")
    UserEntity findFirstByOrderBySuccessAsc();
    @Query("{},{success:1}") // controlo quines dades rebo
    List<UserEntity> findAllSuccess();

    Optional<UserEntity> findByUsername(String username);
}
