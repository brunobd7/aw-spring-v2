package com.dantas.algamoney.awspringv2.api.repository.launch;

import com.dantas.algamoney.awspringv2.api.model.Launch;
import com.dantas.algamoney.awspringv2.api.repository.filter.LaunchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaunchRepositoryImpl implements LaunchRepositoryQuery{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Launch> search(LaunchFilter launchFilter) {

        //FIRST WE NEED INVOKE SOME INSTANCE OF OPERATOR OBJECTS TO HANDLE CRITERIA'S QUERIES
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery(Launch.class);
        Root<Launch> root = criteriaQuery.from(Launch.class);

        //HERE WE CREATE A ARRAY OF PREDICATES , IN OTHER WORDS OUR PARAMS/CONSTRAINTS FOR QUERY
        Predicate[] predicates = generateRestrictions(launchFilter, builder, root);

        //ADD SQL CLAUSE "WHERE" INTO AN OUR CRITERIA QUERY
        criteriaQuery.where(predicates);

        //GENERATING QUERY WITH PARAMS CREATED PREVIOUSLY
        TypedQuery<Launch> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private Predicate[] generateRestrictions(LaunchFilter launchFilter, CriteriaBuilder builder, Root<Launch> root) {

        List<Predicate> predicatesList = new ArrayList<>();

        if(ObjectUtils.isNotEmpty(launchFilter.getDescription()))
            predicatesList.add(
                    builder.like(
                            builder.lower(root.get("description")),"%".concat(launchFilter.getDescription()).concat("%")
                    ));

        if(Objects.nonNull(launchFilter.getInitialDate()))
            predicatesList.add(
                    builder.greaterThanOrEqualTo(
                            root.get("dueDate"),launchFilter.getInitialDate()
                    )
            );

        if(Objects.nonNull(launchFilter.getFinalDate()))
            predicatesList.add(
                    builder.lessThanOrEqualTo(
                            root.get("dueDate"), launchFilter.getFinalDate()
                    )
            );

        return predicatesList.toArray(Predicate[]::new);
    }
}
