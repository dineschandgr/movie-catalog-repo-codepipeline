package io.microservices.moviecatalogservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.microservices.moviecatalogservice.model.CatalogItem;
import io.microservices.moviecatalogservice.model.Movie;
import io.microservices.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${MOVIE_INFO_URI:http://localhost:8082}")
	private String movieInfoHost;
	
	@Value("${RATINGS_DATA_URI:http://localhost:8083}")
	private String ratingsDataHost;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovieCatalogResource.class);
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getMovieCatalog(@PathVariable("userId") String userId){
		
		logger.info("inside getMovieCatalog movieInfoHost is "+movieInfoHost);
		logger.info("inside getMovieCatalog ratingsDataHost is "+ratingsDataHost);
		//discoveryClient.getInstancesById("RATINGS-INFO-SERVICE");
		UserRating userRating = restTemplate.getForObject(ratingsDataHost +"/ratingsdata/users/"+userId, UserRating.class);
		return userRating.getUserRating().stream().map(rating -> {
			//Rest Template
			//for each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject(movieInfoHost + "/movies/"+rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getMovieId(), movie.getMovie_name(), "no desc", rating.getRating());
				
		}).collect(Collectors.toList());
					
	}

}
