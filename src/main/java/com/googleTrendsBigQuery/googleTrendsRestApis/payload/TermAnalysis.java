package com.googleTrendsBigQuery.googleTrendsRestApis.payload;

public class TermAnalysis {

    private String termMeaning;
    private String possibleReasonsForTrend;
    private String culturalAndContextualSignificance;
    private String exploreRegionalInterest;
    private String analyzeRelatedQueriesAndTopics;
    private String forecastingAndPredictiveInsights;
    private String quantifyInterestAndMarketDemand;
    private String relatedTopicsAndQueries;
    private String insightsForMarketingAndStrategy;
    private String policyChangeSuggestions;

    public TermAnalysis(String termMeaning, String possibleReasonsForTrend, String culturalAndContextualSignificance, String exploreRegionalInterest, String analyzeRelatedQueriesAndTopics, String forecastingAndPredictiveInsights, String quantifyInterestAndMarketDemand, String relatedTopicsAndQueries, String insightsForMarketingAndStrategy, String policyChangeSuggestions) {
        this.termMeaning = termMeaning;
        this.possibleReasonsForTrend = possibleReasonsForTrend;
        this.culturalAndContextualSignificance = culturalAndContextualSignificance;
        this.exploreRegionalInterest = exploreRegionalInterest;
        this.analyzeRelatedQueriesAndTopics = analyzeRelatedQueriesAndTopics;
        this.forecastingAndPredictiveInsights = forecastingAndPredictiveInsights;
        this.quantifyInterestAndMarketDemand = quantifyInterestAndMarketDemand;
        this.relatedTopicsAndQueries = relatedTopicsAndQueries;
        this.insightsForMarketingAndStrategy = insightsForMarketingAndStrategy;
        this.policyChangeSuggestions = policyChangeSuggestions;
    }

    public TermAnalysis() {
    }

    public String getTermMeaning() {
        return termMeaning;
    }

    public void setTermMeaning(String termMeaning) {
        this.termMeaning = termMeaning;
    }

    public String getPossibleReasonsForTrend() {
        return possibleReasonsForTrend;
    }

    public void setPossibleReasonsForTrend(String possibleReasonsForTrend) {
        this.possibleReasonsForTrend = possibleReasonsForTrend;
    }

    public String getCulturalAndContextualSignificance() {
        return culturalAndContextualSignificance;
    }

    public void setCulturalAndContextualSignificance(String culturalAndContextualSignificance) {
        this.culturalAndContextualSignificance = culturalAndContextualSignificance;
    }

    public String getExploreRegionalInterest() {
        return exploreRegionalInterest;
    }

    public void setExploreRegionalInterest(String exploreRegionalInterest) {
        this.exploreRegionalInterest = exploreRegionalInterest;
    }

    public String getAnalyzeRelatedQueriesAndTopics() {
        return analyzeRelatedQueriesAndTopics;
    }

    public void setAnalyzeRelatedQueriesAndTopics(String analyzeRelatedQueriesAndTopics) {
        this.analyzeRelatedQueriesAndTopics = analyzeRelatedQueriesAndTopics;
    }

    public String getForecastingAndPredictiveInsights() {
        return forecastingAndPredictiveInsights;
    }

    public void setForecastingAndPredictiveInsights(String forecastingAndPredictiveInsights) {
        this.forecastingAndPredictiveInsights = forecastingAndPredictiveInsights;
    }

    public String getQuantifyInterestAndMarketDemand() {
        return quantifyInterestAndMarketDemand;
    }

    public void setQuantifyInterestAndMarketDemand(String quantifyInterestAndMarketDemand) {
        this.quantifyInterestAndMarketDemand = quantifyInterestAndMarketDemand;
    }

    public String getRelatedTopicsAndQueries() {
        return relatedTopicsAndQueries;
    }

    public void setRelatedTopicsAndQueries(String relatedTopicsAndQueries) {
        this.relatedTopicsAndQueries = relatedTopicsAndQueries;
    }

    public String getInsightsForMarketingAndStrategy() {
        return insightsForMarketingAndStrategy;
    }

    public void setInsightsForMarketingAndStrategy(String insightsForMarketingAndStrategy) {
        this.insightsForMarketingAndStrategy = insightsForMarketingAndStrategy;
    }

    public String getPolicyChangeSuggestions() {
        return policyChangeSuggestions;
    }

    public void setPolicyChangeSuggestions(String policyChangeSuggestions) {
        this.policyChangeSuggestions = policyChangeSuggestions;
    }

    @Override
    public String toString() {
        return "TermAnalysis{" +
                "termMeaning='" + termMeaning + '\'' +
                ", possibleReasonsForTrend='" + possibleReasonsForTrend + '\'' +
                ", culturalAndContextualSignificance='" + culturalAndContextualSignificance + '\'' +
                ", exploreRegionalInterest='" + exploreRegionalInterest + '\'' +
                ", analyzeRelatedQueriesAndTopics='" + analyzeRelatedQueriesAndTopics + '\'' +
                ", forecastingAndPredictiveInsights='" + forecastingAndPredictiveInsights + '\'' +
                ", quantifyInterestAndMarketDemand='" + quantifyInterestAndMarketDemand + '\'' +
                ", relatedTopicsAndQueries='" + relatedTopicsAndQueries + '\'' +
                ", insightsForMarketingAndStrategy='" + insightsForMarketingAndStrategy + '\'' +
                ", policyChangeSuggestions='" + policyChangeSuggestions + '\'' +
                '}';
    }
}