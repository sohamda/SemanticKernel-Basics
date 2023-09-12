package soham.sksamples.util;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.ai.embeddings.EmbeddingGeneration;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.connectors.ai.openai.util.AzureOpenAISettings;
import com.microsoft.semantickernel.connectors.ai.openai.util.SettingsMap;
import com.microsoft.semantickernel.connectors.memory.azurecognitivesearch.AzureCognitiveSearchMemory;
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
        // Create an Azure OpenAI client
        AzureOpenAISettings settings = settings();
        return new OpenAIClientBuilder().endpoint(settings.getEndpoint()).credential(new AzureKeyCredential(settings.getKey())).buildAsyncClient();
    }
    public static AzureOpenAISettings settings() throws ConfigurationException {
        return new AzureOpenAISettings(SettingsMap.
                getWithAdditional(List.of(
                        new File("src/main/resources/conf.properties"))));

    }

    public static Kernel kernel() throws ConfigurationException {
        OpenAIAsyncClient client = openAIAsyncClient();
        TextCompletion textCompletion = SKBuilders.chatCompletion()
                                .withOpenAIClient(client)
                                .withModelId(settings().getDeploymentName())
                                .build();
        return SKBuilders.kernel().withDefaultAIService(textCompletion).build();
    }

    public static AzureCognitiveSearchMemory azureCognitiveSearchMemory() throws IOException {
        AzureCognitiveSearchMemory azureCognitiveSearchMemory;
        Properties properties = new Properties();
        java.net.URL url = ClassLoader.getSystemResource("conf.properties");
        properties.load(url.openStream());
        azureCognitiveSearchMemory = new AzureCognitiveSearchMemory(properties.getProperty("azure.congnitive.search.endpoint"),
                properties.getProperty("azure.congnitive.search.key"));

        return azureCognitiveSearchMemory;
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
                .withMemory(azureCognitiveSearchMemory())
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

