package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.*;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.DateUtils;
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
public class TopTermsServiceImpl implements TopTermsService {

    @Autowired
    private TopTermsRepository topTermsRepository;

    private final AIService aiService;

    public TopTermsServiceImpl(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public Page<TopTerms> getTopTerms(String term, String dmaName, String dmaId, String parsedWeek, Integer rank, Integer score, Pageable pageable) {
        LocalDate week = DateUtils.parseWeek(parsedWeek);

        Specification<TopTerms> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addPredicate(predicates, criteriaBuilder.like(root.get("term"), "%" + term + "%"), term);
            addPredicate(predicates, criteriaBuilder.like(root.get("dmaName"), "%" + dmaName + "%"), dmaName);
            addPredicate(predicates, criteriaBuilder.equal(root.get("dmaId"), dmaId), dmaId);
            addPredicate(predicates, criteriaBuilder.equal(root.get("week"), week), week);
            addPredicate(predicates, criteriaBuilder.equal(root.get("rank"), rank), rank);
            addPredicate(predicates, criteriaBuilder.equal(root.get("score"), score), score);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<TopTerms> result = topTermsRepository.findAll(spec, pageable);

        if (result == null || result.isEmpty()) {
            throw new ResourceNotFoundException("TopTerms", "No Data Found", dmaName);
        }

        return result;
    }

    private void addPredicate(List<Predicate> predicates, Predicate predicate, Object value) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            predicates.add(predicate);
        }
    }

    @Override
    public TermAnalysis getPredictiveInsights(Object internationalTopRisingTerms) {
        return aiService.getAIResults(internationalTopRisingTerms);
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        // Your implementation to save data from BigQuery to MySQL
        return 0L; // Placeholder return value
    }
}
