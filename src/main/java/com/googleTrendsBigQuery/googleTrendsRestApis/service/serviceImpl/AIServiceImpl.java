package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.gson.Gson;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    private final ChatClient chatClient;

    public AIServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public TermAnalysis getAIResults(Object input) {
        String systemMessage = """
                Your primary function is to share the information about the Google Trends terms and the relevant info. The output should be in consistent format. If asked anything else, you can say you can only respond to info related to this Google Trends project. Try to include details of the terms, more context to the search, its relevance, reason for the popularity, location, and so on. Give me output in the following JSON format only.
                 Give insightful and interesting answers! Feel free to include more details.

                     "termMeaning": "Explain the meaning of the term. Provide a clear definition or explanation of what the term signifies.",
                     "possibleReasonsForTrend": "Identify and discuss potential reasons for the trend. Analyze factors that could be contributing to the observed trend.",
                     "culturalAndContextualSignificance": "Explain the cultural and contextual significance. Discuss how the term is culturally and contextually relevant or impactful.",
                     "exploreRegionalInterest": "Investigate reasons for regional interest. Explore why this term may be of particular interest in certain regions.",
                     "analyzeRelatedQueriesAndTopics": "Analyze related queries and topics. Discuss other queries and topics closely related to the term or trend.",
                     "forecastingAndPredictiveInsights": "Provide forecasts and predictive insights. Offer predictions or insights into potential future developments related to the term.",
                     "quantifyInterestAndMarketDemand": Quantify interest and market demand. Attempt to measure the level of interest and demand for the term or related concepts.",
                     "relatedTopicsAndQueries": "Discuss related topics and queries. Explore other topics and queries that are relevant to understanding the term.",
                     "insightsForMarketingAndStrategy": "Provide insights for marketing and strategy. Offer strategic insights or recommendations for marketing related to the term.",
                     "policyChangeSuggestions": "Offer suggestions for policy changes. Propose potential changes in policies or regulations based on insights gained."
                               
                """;

        String jsonResponse = chatClient.prompt()
                .system(systemMessage)
                .user(input.toString())
                .call()
                .content();


        return convertJsonToTermAnalysis(jsonResponse);
    }

    private TermAnalysis convertJsonToTermAnalysis(String jsonResponse) {
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, TermAnalysis.class);
    }
}