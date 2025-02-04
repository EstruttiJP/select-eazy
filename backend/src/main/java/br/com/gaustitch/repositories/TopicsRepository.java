package br.com.gaustitch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gaustitch.model.Topic;

public interface TopicsRepository extends JpaRepository<Topic, Long>{}
