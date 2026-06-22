package com.serviciotecnico.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serviciotecnico.auth.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmailAndActiveTrue(String email);
}