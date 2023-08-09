package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.coreskills.ConversationSummarySkill;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.orchestration.SKFunction;
import com.microsoft.semantickernel.skilldefinition.ReadOnlyFunctionCollection;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static soham.sksamples.util.Constants.ChatTranscript;
import static soham.sksamples.util.KernelUtils.*;

public class Example03_SummarizerAsPlugin {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Example03_SummarizerAsPlugin.class);

    public static void main(String[] args) {
        try {
            // util method to minimize repeating steps
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();

            log.debug("== Load Conversation Summarizer Skill from plugins ==");
            ReadOnlyFunctionCollection conversationSummarySkill =
                    kernel.importSkill(new ConversationSummarySkill(kernel), null);

            log.debug("== Run the Kernel ==");
            Mono<SKContext> summary = conversationSummarySkill
                    .getFunction("SummarizeConversation", SKFunction.class)
                    .invokeAsync(ChatTranscript);
            log.debug("== Result ==");
            log.debug(summary.block().getResult());
        } catch (ConfigurationException e) {
            log.error("Problem in paradise", e);
        }
    }
}
