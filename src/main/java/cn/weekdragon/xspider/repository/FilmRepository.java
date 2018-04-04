package cn.weekdragon.xspider.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


import cn.weekdragon.xspider.domain.Film;

public interface FilmRepository extends PagingAndSortingRepository<Film, Long> {
	
	Page<Film> findDistinctFilmByFullTitleContainingOrBriefCntContaining(String fullTitle,String briefCnt,Pageable pageable);
	Page<Film> findDistinctFilmByFullTitleContaining(String fullTitle,Pageable pageable);
	Film findFilmByFullTitleAndDetailUrl(String fullTitle,String detailUrl);
	

}
