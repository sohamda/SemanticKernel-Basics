package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.coreskills.ConversationSummarySkill;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.memory.MemoryQueryResult;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.orchestration.SKFunction;
import com.microsoft.semantickernel.skilldefinition.ReadOnlyFunctionCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static soham.sksamples.util.KernelUtils.*;

public class Example11_ResumeSearch {

    private static final Logger log = LoggerFactory.getLogger(Example06_SKwithCognitiveSearch.class);

    private static final String MEMORY_COLLECTION_NAME = "resume-index";

    public static void main(String[] args) {
        try {
            String resumeSearch = "find me a candidate for tech team " +
                    "with atleast 2 years of experience developer with java, springboot, microservices";
            log.debug("== Instantiates the Kernel with Azure Cognitive Search ==");
            Kernel kernelWithACS = kernelWithEmbedding(true);

            log.debug("== Search for resumes  ==");
            Mono<List<MemoryQueryResult>> relevantMemory = searchMemory(kernelWithACS,
                    resumeSearch, MEMORY_COLLECTION_NAME);

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
