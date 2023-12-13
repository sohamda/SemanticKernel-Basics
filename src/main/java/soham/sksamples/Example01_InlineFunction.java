package soham.sksamples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.semanticfunctions.PromptTemplateConfig;
import com.microsoft.semantickernel.textcompletion.CompletionRequestSettings;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;
import com.microsoft.semantickernel.textcompletion.TextCompletion;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.*;

public class Example01_InlineFunction {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Example01_InlineFunction.class);

    public static void main(String[] args) {
        try {
            log.debug("== Create Azure OpenAI Client ==");
            OpenAIAsyncClient client = openAIAsyncClient();

            log.debug("== Create an instance of the TextCompletion service and register it for the Kernel configuration ==");
            TextCompletion textCompletion = SKBuilders.chatCompletion()
                    .withOpenAIClient(client)
                    .withModelId(getProperty("client.azureopenai.deploymentname"))
                    .build();

            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = SKBuilders.kernel().withDefaultAIService(textCompletion).build();
            log.debug("== Define inline function ==");
            String semanticFunctionInline = """
                {{$input}}
                
                Summarize the content above in less than 140 characters.
                """;
            CompletionSKFunction summarizeFunction = SKBuilders
                    .completionFunctions()
                    .withKernel(kernel)
                    .withPromptTemplate(semanticFunctionInline)
                    .withRequestSettings(
                            new PromptTemplateConfig(new CompletionRequestSettings()).getCompletionRequestSettings()).build();

            log.debug("== Run the Kernel ==");
            Mono<SKContext> result = summarizeFunction.invokeAsync(TextToSummarize);

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (ConfigurationException | NullPointerException | IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}
