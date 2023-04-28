package com.dantas.algamoney.awspringv2.api.repository.launch;

import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.filter.LaunchFilter;

import java.util.List;

public interface LaunchRepositoryQuery {

    List<Launch> search(LaunchFilter filter);
}
