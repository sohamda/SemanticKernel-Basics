package soham.sksamples;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

import static soham.sksamples.util.Constants.CallTranscript;
import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.*;

public class Example04_PluginAutoInvocation {

    private static final Logger log = LoggerFactory.getLogger(Example03_KernelPluginAsClass.class);

    public static void main(String[] args) throws IOException {

        log.debug("== Instantiates the Kernel ==");

        String modelName = getProperty(MODEL_NAME);
        OpenAIAsyncClient client = openAIAsyncClient();
        ChatCompletionService chatCompletion = OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(client)
                .withModelId(modelName)
                .build();

        KernelPlugin summarize = KernelPluginFactory
                .importPluginFromDirectory(Path.of("src/main/resources/Skills"),
                        "SummarizeSkill",null);

        KernelPlugin writer = KernelPluginFactory
                .importPluginFromDirectory(Path.of("src/main/resources/Skills"),
                        "WriterSkill",null);

        KernelPlugin designThinking = KernelPluginFactory
                .importPluginFromDirectory(Path.of("src/main/resources/Skills"),
                        "DesignThinkingSkill",null);

        Kernel kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletion)
                .withPlugin(summarize)
                .withPlugin(writer)
                .withPlugin(designThinking)
                .build();

        InvocationContext invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

       /* invokeFunctionsAutomatically("""
                Apply Design Thinking to the following call transcript >
                """ + CallTranscript, kernel, chatCompletion, invocationContext);

        invokeFunctionsAutomatically("""
                summarize the following text and then translate it to Dutch. >
                """ + TextToSummarize, kernel, chatCompletion, invocationContext);*/

        invokeFunctionsAutomatically("""
                rewrite the following content in Yoda from Starwars style. >
                """ + TextToSummarize, kernel, chatCompletion, invocationContext);
    }

    private static void invokeFunctionsAutomatically(String taskToPerform, Kernel kernel,
                     ChatCompletionService chatCompletion, InvocationContext invocationContext) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage(taskToPerform);
        ChatMessageContent<?> result = chatCompletion
                .getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block().get(0);

        log.debug("Result > " + result.getContent());
    }
}
