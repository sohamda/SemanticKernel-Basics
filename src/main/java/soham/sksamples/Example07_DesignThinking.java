package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static soham.sksamples.util.Constants.CallTranscript;
import static soham.sksamples.util.KernelUtils.kernel;

public class Example07_DesignThinking {

    private static final Logger log = LoggerFactory.getLogger(Example07_DesignThinking.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();

            log.debug("== Adding multiple skills to kernel ==");
            kernel.importSkillFromDirectory("DesignThinkingSkill", "src/main/resources/Skills", "DesignThinkingSkill");

            log.debug("== Run kernel to Empathize > Define > Ideate on the input ==");
            Mono<SKContext> result =
                    kernel.runAsync(
                            CallTranscript,
                            kernel.getSkill("DesignThinkingSkill").getFunction("Empathize", null),
                            kernel.getSkill("DesignThinkingSkill").getFunction("Define", null),
                            kernel.getSkill("DesignThinkingSkill").getFunction("Ideate", null));

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (ConfigurationException | IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}
