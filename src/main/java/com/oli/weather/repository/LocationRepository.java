package com.oli.weather.repository;

import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    List<Location> findAllByUser(User user);
}
