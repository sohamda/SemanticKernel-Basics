package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.textcompletion.CompletionSKFunction;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import soham.sksamples.skill.TextSkill;

import java.io.IOException;

import static soham.sksamples.util.Constants.ChatTranscript;
import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.kernel;

public class Example04_SkillsPipeline {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Example03_SummarizerAsPlugin.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();

            log.debug("== Adding multiple skills to kernel ==");
            kernel.importSkill(new TextSkill(), null);
            kernel.importSkillFromDirectory("SummarizeSkill", "src/main/resources/Skills", "SummarizeSkill");
            kernel.importSkillFromDirectory("WriterSkill", "src/main/resources/Skills", "WriterSkill");

            SKContext summarizeContext = SKBuilders.context().build();
            summarizeContext.setVariable("input", ChatTranscript);
            summarizeContext.setVariable("language", "dutch");
            Mono<SKContext> result =
                    kernel.runAsync(
                            summarizeContext.getVariables(),
                            kernel.getSkill("SummarizeSkill").getFunction("Summarize"),
                            kernel.getSkill("WriterSkill").getFunction("Translate"));

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (ConfigurationException | IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}
