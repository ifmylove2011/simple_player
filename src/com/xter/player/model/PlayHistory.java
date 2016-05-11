package com.xter.player.model;

public class PlayHistory {
	private String movieName;
	private String movieUri;
	private String movieLastestDate;

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieUri() {
		return movieUri;
	}

	public void setMovieUri(String movieUri) {
		this.movieUri = movieUri;
	}

	public String getMovieLastestDate() {
		return movieLastestDate;
	}

	public void setMovieLastestDate(String movieLastestDate) {
		this.movieLastestDate = movieLastestDate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else {
			if (this.getClass() == o.getClass()) {
				if (this.getMovieName().equals(((PlayHistory) o).getMovieName())
						&& this.getMovieUri().equals(((PlayHistory) o).getMovieUri())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

}
