package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.coreskills.ConversationSummarySkill;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.memory.MemoryQueryResult;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.orchestration.SKFunction;
import com.microsoft.semantickernel.skilldefinition.ReadOnlyFunctionCollection;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static soham.sksamples.util.Constants.data;
import static soham.sksamples.util.KernelUtils.*;

public class Example05_SKwithMemory {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Example05_SKwithMemory.class);
    private static final String MEMORY_COLLECTION_NAME = "skdocs";

    public static void main(String[] args) {
        try {

            log.debug("== Instantiates the Embedding Kernel ==");
            Kernel kernelWithEmbedding = kernelWithEmbedding(false);
            log.debug("== Stores some data in-memory and Search for a query ==");
            Mono<List<MemoryQueryResult>> relevantMemory = storeInKernelMemory(kernelWithEmbedding, data(), MEMORY_COLLECTION_NAME)
                    .then(searchMemory(kernelWithEmbedding, "How do I get started?", MEMORY_COLLECTION_NAME));
            log.debug("== Extracts the results from the indexed search ==");
            List<MemoryQueryResult> relevantMems = relevantMemory.block();
            StringBuilder memory = new StringBuilder();
            relevantMems.forEach(relevantMem -> memory.append("URL: ").append(relevantMem.getMetadata().getId())
                    .append("Title: ").append(relevantMem.getMetadata().getDescription()));
            log.debug("== Instantiates another kernel with Summarizer Skills ==");
            Kernel kernel = kernel();
            ReadOnlyFunctionCollection conversationSummarySkill =
                    kernel.importSkill(new ConversationSummarySkill(kernel), null);
            log.debug("== Summarizes results from search ==");
            invokeKernelWithRelevantMemory(conversationSummarySkill, memory.toString());
        } catch (ConfigurationException | IOException e) {
            log.error("Problem in paradise", e);
        }
    }

    private static void invokeKernelWithRelevantMemory(ReadOnlyFunctionCollection conversationSummarySkill, String relevantMemory) {
        log.debug("=== Relevant Mem: {}", relevantMemory);

        log.debug("== Run the Kernel ==");
        Mono<SKContext> summary = conversationSummarySkill
                .getFunction("SummarizeConversation", SKFunction.class)
                .invokeAsync(relevantMemory);
        log.debug("== Result ==");
        log.debug(summary.block().getResult());
    }
}
