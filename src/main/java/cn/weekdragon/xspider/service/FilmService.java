package cn.weekdragon.xspider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.repository.FilmRepository;

@Service
public class FilmService {

	@Autowired
	private FilmRepository filmRepository;
	public Film insertDistinctFilm(Film film) {
		Film film2 = filmRepository.findFilmByFullTitleAndDetailUrl(film.getFullTitle(), film.getDetailUrl());
		if(film2 == null) {
			return filmRepository.save(film);
		}
		return film2;
	}
	
	public Film saveFilm(Film film) {
			return filmRepository.save(film);
	}

	public Page<Film> listNewestFilms(String keyword, Pageable pageable) {
		return filmRepository.findDistinctFilmByFullTitleContaining(keyword, pageable);
	}
	
	public Page<Film> listFilms(Pageable pageable) {
		return filmRepository.findAll(pageable);
	}
	
	public Film getFilmById(Integer id) {
		return filmRepository.findOne(Long.valueOf(id));
	}
	
	public void deleteFilm(Long id) {
		filmRepository.delete(id);
	}
	
	public void deleteFilm(Film film) {
		filmRepository.delete(film);
	}
}
