package cn.weekdragon.xspider.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import cn.weekdragon.xspider.domain.Film;


public interface FilmService {

	Film saveFilm(Film film);
	
	void removeFilm(Long id);
	
	Film getFilmById(Long id);
	
	Page<Film> listNewestFilms(String keyword, Pageable pageable);
	
	Page<Film> listFilms(Pageable pageable);
	
	/**
	 * 阅读量递增
	 * @param id
	 */
	void readingIncrease(Long id);
	
	/**
	 * 发表评论
	 * @param filmId
	 * @param commentContent
	 * @return
	 */
	Film createComment(Long filmId, String commentContent);
	
	/**
	 * 删除评论
	 * @param filmId
	 * @param commentId
	 * @return
	 */
	void removeComment(Long filmId, Long commentId);
	
	/**
	 * 点赞
	 * @param filmId
	 * @return
	 */
	Film createVote(Long filmId);
	
	/**
	 * 取消点赞
	 * @param filmId
	 * @param voteId
	 * @return
	 */
	void removeVote(Long filmId, Long voteId);
}
