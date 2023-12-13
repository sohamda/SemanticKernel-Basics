package soham.sksamples.util;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.ai.embeddings.EmbeddingGeneration;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.connectors.ai.openai.util.ClientType;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.connectors.memory.azurecognitivesearch.AzureCognitiveSearchMemoryStore;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.memory.MemoryQueryResult;
import com.microsoft.semantickernel.memory.VolatileMemoryStore;
import com.microsoft.semantickernel.textcompletion.TextCompletion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KernelUtils {

    public static OpenAIAsyncClient openAIAsyncClient() throws ConfigurationException {
        return OpenAIClientProvider.getWithAdditional(List.of(
                        new File("src/main/resources/conf.properties")), ClientType.AZURE_OPEN_AI);

    }
    public static Kernel kernel() throws ConfigurationException, IOException {
        OpenAIAsyncClient client = openAIAsyncClient();
        TextCompletion textCompletion = SKBuilders.chatCompletion()
                                .withOpenAIClient(client)
                                .withModelId(getProperty("client.azureopenai.deploymentname"))
                                .build();
        return SKBuilders.kernel().withDefaultAIService(textCompletion).build();
    }

    public static AzureCognitiveSearchMemoryStore azureCognitiveSearchMemory() throws IOException {
        AzureCognitiveSearchMemoryStore azureCognitiveSearchMemory;
        azureCognitiveSearchMemory = new AzureCognitiveSearchMemoryStore(getProperty("azure.congnitive.search.endpoint"),
                getProperty("azure.congnitive.search.key"));

        return azureCognitiveSearchMemory;
    }

    public static String getProperty(String key) throws IOException {
        Properties properties = new Properties();
        java.net.URL url = ClassLoader.getSystemResource("conf.properties");
        properties.load(url.openStream());
        return properties.getProperty(key);
    }

    public static Kernel kernelWithEmbedding(boolean withAzureCognitiveSearchMemory) throws ConfigurationException, IOException {
        OpenAIAsyncClient openAIAsyncClient = openAIAsyncClient();

        EmbeddingGeneration<String> textEmbeddingGenerationService =
                SKBuilders.textEmbeddingGeneration()
                        .withOpenAIClient(openAIAsyncClient)
                        .withModelId("embedding")
                        .build();
        if(!withAzureCognitiveSearchMemory) {
            return SKBuilders.kernel()
                    .withDefaultAIService(textEmbeddingGenerationService)
                    .withMemoryStorage(new VolatileMemoryStore.Builder().build())
                    .build();
        }
        return SKBuilders.kernel()
                .withDefaultAIService(textEmbeddingGenerationService)
                .withMemoryStorage(azureCognitiveSearchMemory())
                .build();

    }
    public static Mono<Void> storeInKernelMemory(Kernel kernel, Map<String, String> data, String collectionName) {
        return Flux.fromIterable(data.entrySet())
                .map(entry -> {
                            return kernel.getMemory().saveInformationAsync(
                                    collectionName,
                                    entry.getValue(),
                                    entry.getKey(),
                                    "skdocs",
                                    null);
                        }
                )
                .mapNotNull(Mono::block)
                .then();
    }

    public static Mono<List<MemoryQueryResult>> searchMemory(Kernel kernel, String query, String collectionName) {
        return kernel.getMemory()
                .searchAsync(collectionName,
                        query, 2, 0.7f, false);
    }
}

