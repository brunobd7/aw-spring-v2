package com.dantas.algamoney.awspringv2.api.repository.launch;

import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.filter.LaunchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LaunchRepositoryQuery {

    Page<Launch> search(LaunchFilter filter, Pageable pageable);
}
