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
	public Film saveFilm(Film film) {
		Film film2 = filmRepository.findFilmByFullTitleAndDetailUrl(film.getFullTitle(), film.getDetailUrl());
		if(film2 == null) {
			return filmRepository.save(film);
		}
		return film2;
	}

	public Page<Film> listNewestFilms(String keyword, Pageable pageable) {
		return filmRepository.findDistinctFilmByFullTitleContaining(keyword, pageable);
	}
	
	public Page<Film> listFilms(Pageable pageable) {
		return filmRepository.findAll(pageable);
	}
}
