package com.eureka.MovieCatalogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List <CatalogItem> getCatalog(@PathVariable("userId") String userId){
        //Find movies user has rated
         UserRating ratings = restTemplate.getForObject("http://eureka-rating-service/ratings/users/"+userId,UserRating.class);

        //Find names of rated movies by the user
        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://eureka-movie-info-service/movies/"+rating.getMovieId(),Movie.class);

           //combines both information from two services
           return new CatalogItem(movie.getName(), "J.K.Rowling", rating.getRating());
           
        }).collect(Collectors.toList());


    }

}
