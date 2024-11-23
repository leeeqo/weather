package com.oli.weather.repository;

import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    List<Location> findAllByUser(User user);

    /*@Query(nativeQuery = true, value =
            "DELETE FROM locations " +
            "WHERE user_id = :user_id " +
            "AND latitude = :latitude " +
            "AND longitude = :longitude"
    )
    void deleteByUserLatitudeLongitude(User user, String latitude, String longitude);*/
}
