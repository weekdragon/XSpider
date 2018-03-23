package cn.weekdragon.xspider.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.weekdragon.xspider.domain.Film;

public interface FilmRepository extends PagingAndSortingRepository<Film, Long> {

}
