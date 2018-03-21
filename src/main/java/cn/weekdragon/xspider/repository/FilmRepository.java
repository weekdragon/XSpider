package cn.weekdragon.xspider.repository;

import org.springframework.data.repository.CrudRepository;

import cn.weekdragon.xspider.domain.Film;

public interface FilmRepository extends CrudRepository<Film, Long> {

}
