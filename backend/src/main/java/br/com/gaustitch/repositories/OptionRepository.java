package br.com.gaustitch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gaustitch.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long>{}
