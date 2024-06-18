package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.DateUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopTermsServiceImpl implements  TopTermsService {

    @Autowired
    private TopTermsRepository topTermsRepository;

    @Override
    public Page<TopTerms> getTopTerms(String dmaName, String dmaId, String parsedWeek, Integer rank, Integer score,Integer page,Integer pageSize, Pageable pageaable) {
//       this method is like a query its make a query from our given parameter


        //this method also makesure date is valid and not in future
        LocalDate week = DateUtils.parseWeek(parsedWeek);

        // Create custom pageable with default values for page and pageSize
        Pageable pageable = PageRequest.of(page, pageSize, pageaable.getSort());

        //specification is api from spring jpa is allowed to make a manual query
        // root is access data from entitiy
        // criteriabuilder is make a query for like, and, or in manual
        // purpose of this method is clean code
        Specification<TopTerms> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dmaName != null && !dmaName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("dmaName"), "%" + dmaName + "%"));
            }
            if (dmaId != null && !dmaId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("dmaId"), dmaId));
            }
            if (week != null) {
//                predicates.add(criteriaBuilder.equal(root.get("week"), week));
                LocalDate nearestWeek = findNearestWeek(week);
                predicates.add(criteriaBuilder.equal(root.get("week"), nearestWeek));
            }
            if (rank != null) {
                predicates.add(criteriaBuilder.equal(root.get("rank"), rank));
            }
            if (score != null) {
                predicates.add(criteriaBuilder.equal(root.get("score"), score));
            }
            // Combine all predicates with AND operation
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<TopTerms> result = topTermsRepository.findAll(spec, pageable);
        if (result == null || result.getTotalElements()==0) {
            throw new ResourceNotFoundException("TopTerms", "No Data Found", dmaName);
        }
        return result;
    }
    private LocalDate findNearestWeek(LocalDate week) {
        // Query database for nearest week
        List<TopTerms> nearestWeeks = topTermsRepository.findByNearestWeek(week, PageRequest.of(0, 1));
        if (!nearestWeeks.isEmpty()) {
            return nearestWeeks.get(0).getWeek();
        }
        return week; // Return original week if no nearest week found
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        // Your implementation to save data from BigQuery to MySQL
        return 0L; // Placeholder return value
    }
}