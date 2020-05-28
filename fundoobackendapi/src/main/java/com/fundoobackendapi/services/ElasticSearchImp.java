package com.fundoobackendapi.services;
import com.fundoobackendapi.model.Note;
import com.fundoobackendapi.serviceint.IElasticSearch;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.stereotype.Service;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticSearchImp implements IElasticSearch {
    private static final String Index = "fundoodb";

    private static final String TYPE = "note";

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void createNote(Note noteDocument) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, Object> documentMapper = objectMapper.convertValue(noteDocument, Map.class);
        IndexRequest indexRequest = new IndexRequest(Index, TYPE, String.valueOf(noteDocument.getNoteId()))
                .source(documentMapper);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public void Update(Note noteDocument) throws Exception {

        Note resultDocument = findById(noteDocument.getNoteId());
        UpdateRequest updateRequest = new UpdateRequest(Index, TYPE, String.valueOf(resultDocument.getNoteId()));
        @SuppressWarnings("unchecked")
        Map<String, Object> documentMapper = objectMapper.convertValue(noteDocument, Map.class);
        updateRequest.doc(documentMapper);
        client.update(updateRequest, RequestOptions.DEFAULT);

    }

    @Override
    public Note findById(long id) throws Exception {
        String noteId = String.valueOf(id);
        GetRequest getRequest = new GetRequest(Index, TYPE, noteId);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();
        return objectMapper.convertValue(resultMap, Note.class);
    }

    @Override
    public void deleteNote(long noteId) throws Exception {
        String Id = String.valueOf(noteId);
        DeleteRequest deleteRequest = new DeleteRequest(Index, TYPE, Id);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public List<Note> findAll(long userId) throws IOException {
        List<Note> users = new ArrayList<>();
        String id = String.valueOf(userId);
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("userId", id)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(2)
                .maxExpansions(10);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        for(SearchHit searchHit : searchResponse.getHits().getHits()){
            Note user = new ObjectMapper().readValue(searchHit.getSourceAsString(),Note.class);
            users.add(user);
        }
        return users;
        /* List<Note> noteList = new ArrayList<>();
        String Id = String.valueOf(userId);
        System.out.println(noteList.size());
        SearchRequest searchRequest = new SearchRequest(Index,TYPE);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("userId",Id));
        SearchSourceBuilder searchBuild = new SearchSourceBuilder();
        searchBuild.query(QueryBuilders.nestedQuery("noteList", queryBuilder, ScoreMode.Avg));
        searchRequest.source(searchBuild);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        System.out.println(searchHit.length);
        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> noteList.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
        }
        System.out.println(noteList.size());
        return noteList;*/
    }

    @Override
    public List<Note> findByKeyword(String title) throws Exception {
       /* List<Note> noteList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(Index, TYPE,title);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", title));
        SearchSourceBuilder searchBuild = new SearchSourceBuilder();
        searchBuild.query(QueryBuilders.nestedQuery("noteList", queryBuilder, ScoreMode.Avg));
        searchRequest.source(searchBuild);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();

        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> noteList.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
        }*/
        return null;
    }
}
