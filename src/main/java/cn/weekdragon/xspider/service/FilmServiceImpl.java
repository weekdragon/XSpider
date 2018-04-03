package cn.weekdragon.xspider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.repository.FilmRepository;

@Service
public class FilmServiceImpl implements FilmService {

	@Autowired
	private FilmRepository filmRepository;
	
	@Override
	public Film saveFilm(Film film) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFilm(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Film getFilmById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Film> listNewestFilms(String keyword, Pageable pageable) {
		return filmRepository.findDistinctFilmByFullTitleContainingOrBriefCntContaining(keyword, keyword, pageable);
	}
	
	@Override
	public Page<Film> listFilms(Pageable pageable) {
		return filmRepository.findAll(pageable);
	}
	

	@Override
	public void readingIncrease(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Film createComment(Long filmId, String commentContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeComment(Long filmId, Long commentId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Film createVote(Long filmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeVote(Long filmId, Long voteId) {
		// TODO Auto-generated method stub

	}


}
