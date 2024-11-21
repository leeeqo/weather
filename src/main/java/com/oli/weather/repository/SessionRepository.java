package com.oli.weather.repository;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    Optional<Session> findByUser(User user);
}
