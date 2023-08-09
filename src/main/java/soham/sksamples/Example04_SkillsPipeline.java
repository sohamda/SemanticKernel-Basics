package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import soham.sksamples.skill.TextSkill;

import static soham.sksamples.util.Constants.ChatTranscript;
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

            log.debug("== Run kernel with Summarize first and then Uppercase on the results ==");
            Mono<SKContext> result =
                    kernel.runAsync(
                            ChatTranscript,
                            kernel.getSkill("SummarizeSkill").getFunction("Summarize", null),
                            kernel.getSkills().getFunction("Uppercase", null));

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (ConfigurationException e) {
            log.error("Problem in paradise", e);
        }
    }
}
