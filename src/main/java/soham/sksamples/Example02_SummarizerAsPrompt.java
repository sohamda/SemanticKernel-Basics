package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.builders.SKBuilders;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.skilldefinition.ReadOnlyFunctionCollection;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.*;

public class Example02_SummarizerAsPrompt {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Example02_SummarizerAsPrompt.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();
            ReadOnlyFunctionCollection skill = kernel.importSkillFromDirectory("SummarizeSkill", "src/main/resources/Skills", "SummarizeSkill");
            CompletionSKFunction summarizeFunction = skill.getFunction("Summarize", CompletionSKFunction.class);

            log.debug("== Set Summarize Skill ==");
            SKContext summarizeContext = SKBuilders.context().build();
            summarizeContext.setVariable("input", TextToSummarize);

            log.debug("== Run the Kernel ==");
            Mono<SKContext> result = summarizeFunction.invokeAsync(summarizeContext);

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (ConfigurationException | NullPointerException e) {
            log.error("Problem in paradise", e);
        }
    }
}