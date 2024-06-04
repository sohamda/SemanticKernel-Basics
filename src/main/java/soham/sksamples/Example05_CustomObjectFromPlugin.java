package soham.sksamples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import soham.sksamples.skill.PersonEntity;
import soham.sksamples.skill.SearchPlugin;

import java.io.IOException;
import java.util.List;

import static soham.sksamples.util.KernelUtils.chatCompletionKernel;

public class Example05_CustomObjectFromPlugin {

    private static final Logger log = LoggerFactory.getLogger(Example05_CustomObjectFromPlugin.class);

    public static void main(String[] args) throws IOException, ServiceNotFoundException {
        KernelPlugin searchPlugin = KernelPluginFactory
                .createFromObject(new SearchPlugin(), "SearchPlugin");
        Kernel kernel = chatCompletionKernel(searchPlugin);

        ObjectMapper objectMapper = new ObjectMapper();
        ContextVariableTypeConverter<PersonEntity> personEntityCVT = new ContextVariableTypeConverter<>(
                PersonEntity.class,
                objectToObject -> (PersonEntity) objectToObject,
                objectToString -> //objectToString.toString(),
                    // THE IDEAL implementation would be proper deserializable type, example to JSON below

                    {
                        try {
                            String json = objectMapper.writeValueAsString(objectToString);
                            log.debug("converting from object to json {}", json);
                            return json;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    },
                stringToObject -> //null
                // THE IDEAL implementation would be proper serialization, example from JSON below

                {
                    try {
                        log.debug("converting from json to object {}", stringToObject);
                        return objectMapper.readValue(stringToObject, PersonEntity.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        ContextVariableTypes.addGlobalConverter(personEntityCVT);

        InvocationContext invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

        ChatCompletionService chatCompletionService = kernel.getService(ChatCompletionService.class);

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage("can you find a person whose name is Soham");
        runchat(chatHistory, chatCompletionService, kernel, invocationContext);
        chatHistory.addUserMessage("what is I say in my previous message");
        runchat(chatHistory, chatCompletionService, kernel, invocationContext);
    }

    private static void runchat(ChatHistory chatHistory, ChatCompletionService chat,
                                Kernel kernel, InvocationContext invocationContext) {
        log.debug("User > {}", chatHistory.getLastMessage().get());
        List<ChatMessageContent<?>> messageContentsAsync = chat
                .getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block();
        ChatMessageContent<?> result = messageContentsAsync.get(messageContentsAsync.size() -1);
        chatHistory.addAssistantMessage(result.getContent());

        log.debug("Assistant > {}", result);

    }
}
