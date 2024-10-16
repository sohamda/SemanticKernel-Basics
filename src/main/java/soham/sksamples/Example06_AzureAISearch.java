package soham.sksamples;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.ClientOptions;
import com.azure.core.util.MetricsOptions;
import com.azure.core.util.TracingOptions;
import com.azure.search.documents.indexes.SearchIndexAsyncClient;
import com.azure.search.documents.indexes.SearchIndexClientBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.connectors.data.azureaisearch.AzureAISearchVectorStore;
import com.microsoft.semantickernel.connectors.data.azureaisearch.AzureAISearchVectorStoreOptions;
import com.microsoft.semantickernel.connectors.data.azureaisearch.AzureAISearchVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResult;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordDataAttribute;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordKeyAttribute;
import com.microsoft.semantickernel.data.vectorstorage.attributes.VectorStoreRecordVectorAttribute;
import com.microsoft.semantickernel.data.vectorstorage.definition.DistanceFunction;
import com.microsoft.semantickernel.services.textembedding.Embedding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static soham.sksamples.util.KernelUtils.getProperty;
import static soham.sksamples.util.KernelUtils.openAIAsyncClient;

public class Example06_AzureAISearch {

    private static final Logger log = LoggerFactory.getLogger(Example06_AzureAISearch.class);

    private static final String EMBEDDING_MODEL = "client.azureopenai.embedding.model.name";
    private static final String EMBEDDING_DIMENSIONS = "client.azureopenai.embedding.model.dimension";
    private static final String AI_SEARCH_INDEX = "client.azure.ai.search.index.name";
    private static final String AI_SEARCH_ENDPOINT = "client.azure.ai.search.endpoint";
    private static final String AI_SEARCH_KEY = "client.azure.ai.search.key";


    public static void main(String[] args) throws IOException {

        OpenAITextEmbeddingGenerationService embeddingGenerationService =
                OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient())
                .withDeploymentName(getProperty(EMBEDDING_MODEL))
                        .withModelId(getProperty(EMBEDDING_MODEL))
                .withDimensions(Integer.parseInt(getProperty(EMBEDDING_DIMENSIONS))).build();

        SearchIndexAsyncClient searchClient = new SearchIndexClientBuilder()
                .endpoint(getProperty(AI_SEARCH_ENDPOINT))
                .credential(new AzureKeyCredential(getProperty(AI_SEARCH_KEY)))
                .clientOptions(new ClientOptions()
                        .setTracingOptions(new TracingOptions())
                        .setMetricsOptions(new MetricsOptions())
                        .setApplicationId("SemanticKernel-Basics"))
                .buildAsyncClient();

        AzureAISearchVectorStore azureAISearchVectorStore = AzureAISearchVectorStore
                .builder().withSearchIndexAsyncClient(searchClient)
                .withOptions(new AzureAISearchVectorStoreOptions())
                .build();

        String indexName = getProperty(AI_SEARCH_INDEX);
        VectorStoreRecordCollection<String, Resume> collection = azureAISearchVectorStore
                .getCollection(indexName, AzureAISearchVectorStoreRecordCollectionOptions.<Resume>builder()
                        .withRecordClass(Resume.class)
                        .build());


        Mono<List<Embedding>> searchTextEmbeddings = embeddingGenerationService.generateEmbeddingsAsync(Collections.singletonList("J2EE Developer"));
        List<VectorSearchResult<Resume>> searchResults = searchTextEmbeddings
                .flatMap(r -> collection.searchAsync(r.get(0).getVector(), null)).block();


        log.debug("Single Vector Search Results:");
        for (VectorSearchResult<Resume> searchResult : searchResults) {

            log.debug("{}: {}", searchResult.getRecord().title, searchResult.getRecord().content);
        }
        log.debug("Total number of search results: " + searchResults.stream().count());
    }


    static class Resume {
        @VectorStoreRecordDataAttribute
        private String content;
        @VectorStoreRecordDataAttribute
        private String filepath;
        @VectorStoreRecordDataAttribute
        private String title;
        @VectorStoreRecordDataAttribute
        private String url;
        @VectorStoreRecordKeyAttribute
        private String id;
        @VectorStoreRecordDataAttribute
        private String chunk_id;
        @VectorStoreRecordDataAttribute
        private String last_updated;
        @VectorStoreRecordVectorAttribute(dimensions = 1536, indexKind = "Hnsw", distanceFunction = DistanceFunction.COSINE_DISTANCE)
        private List<Float> contentVector;

        public Resume() {
            this(null, null, null, null, null, null, null, Collections.emptyList());
        }

        public Resume(@JsonProperty String content, @JsonProperty String filepath, @JsonProperty String title,
                      @JsonProperty String url, @JsonProperty String id, @JsonProperty String chunk_id,
                      @JsonProperty String last_updated, @JsonProperty List<Float> contentVector) {
            this.content = content;
            this.filepath = filepath;
            this.title = title;
            this.url = url;
            this.id = id;
            this.chunk_id = chunk_id;
            this.last_updated = last_updated;
            this.contentVector = contentVector;
        }

        static String encodeId(String realId) {
            byte[] bytes = Base64.getUrlEncoder().encode(realId.getBytes(StandardCharsets.UTF_8));
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
