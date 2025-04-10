package soham.sksamples.util;

import com.azure.ai.contentsafety.BlocklistClient;
import com.azure.ai.contentsafety.BlocklistClientBuilder;
import com.azure.ai.contentsafety.ContentSafetyClient;
import com.azure.ai.contentsafety.ContentSafetyClientBuilder;
import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.textcompletion.TextGenerationService;

import java.io.IOException;
import java.util.Properties;

public class KernelUtils {

    public static String ENDPOINT = "client.azureopenai.endpoint";
    public static String API_KEY = "client.azureopenai.key";
    public static String MODEL_NAME = "client.azureopenai.deploymentname";
    public static String CONTENT_SAFETY_ENDPOINT = "client.azure.content.safety.endpoint";
    public static String CONTENT_SAFETY_KEY = "client.azure.content.safety.key";

    public static OpenAIAsyncClient openAIAsyncClient() throws IOException {
        String endpoint = getProperty(ENDPOINT);
        String apiKey = getProperty(API_KEY);

        if(endpoint != null && !endpoint.isEmpty()) {
            return new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(new AzureKeyCredential(apiKey))
                    .buildAsyncClient();
        }
        return new OpenAIClientBuilder()
                .credential(new KeyCredential(apiKey))
                .buildAsyncClient();
    }
    public static Kernel textGenerationKernel() throws IOException {
        String modelName = getProperty(MODEL_NAME);
        OpenAIAsyncClient client = openAIAsyncClient();
        TextGenerationService textGenerationService = TextGenerationService.builder()
                .withOpenAIAsyncClient(client)
                .withModelId(modelName)
                .build();

        return Kernel.builder()
                .withAIService(TextGenerationService.class, textGenerationService)
                .build();
    }

    public static Kernel chatCompletionKernel(KernelPlugin kernelPlugin) throws IOException {
        String modelName = getProperty(MODEL_NAME);
        OpenAIAsyncClient client = openAIAsyncClient();
        ChatCompletionService chatCompletion = OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(client)
                .withModelId(modelName)
                .build();
        if(kernelPlugin == null) {
            return Kernel.builder()
                    .withAIService(ChatCompletionService.class, chatCompletion)
                    .build();
        }
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletion)
                .withPlugin(kernelPlugin)
                .build();
    }

    public static ContentSafetyClient getContentSafetyClient() throws IOException {
        String endpoint = getProperty(CONTENT_SAFETY_ENDPOINT);
        String key = getProperty(CONTENT_SAFETY_KEY);

        // Create a Content Safety client
        return new ContentSafetyClientBuilder()
                .credential(new KeyCredential(key))
                .endpoint(endpoint).buildClient();
    }

    public static BlocklistClient getBlocklistClient() throws IOException {
        String endpoint = getProperty(CONTENT_SAFETY_ENDPOINT);
        String key = getProperty(CONTENT_SAFETY_KEY);

        // Create a Content Safety client
        return new BlocklistClientBuilder()
                .credential(new KeyCredential(key))
                .endpoint(endpoint).buildClient();
    }



    public static String getProperty(String key) throws IOException {
        Properties properties = new Properties();
        java.net.URL url = ClassLoader.getSystemResource("conf.properties");
        properties.load(url.openStream());
        return properties.getProperty(key);
    }
}

