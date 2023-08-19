package ru.effectivemobile.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.entity.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

}
