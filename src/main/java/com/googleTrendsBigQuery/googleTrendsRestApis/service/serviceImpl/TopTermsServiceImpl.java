package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<TopTerms> getTopTerms(String dmaName, String dmaId, LocalDate week, Integer rank, Integer score, Pageable pageable) {
    // this method is like a query its make a query from our given parameter
        Specification<TopTerms> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dmaName != null && !dmaName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("dmaName"), "%" + dmaName + "%"));
            }
            if (dmaId != null && !dmaId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("dmaId"), dmaId));
            }
            if (week != null) {
                predicates.add(criteriaBuilder.equal(root.get("week"), week));
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
            throw new ResourceNotFoundException("TopTerms", "Dma Name", dmaName);
        }
        return result;
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        // Your implementation to save data from BigQuery to MySQL
        return 0L; // Placeholder return value
    }
}