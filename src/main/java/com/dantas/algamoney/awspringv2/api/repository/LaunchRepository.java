package com.dantas.algamoney.awspringv2.api.repository;

import com.dantas.algamoney.awspringv2.api.model.Launch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchRepository extends JpaRepository<Launch,Long> {
}
