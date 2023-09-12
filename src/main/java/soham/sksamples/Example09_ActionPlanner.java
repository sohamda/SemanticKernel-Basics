package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.planner.actionplanner.ActionPlanner;
import com.microsoft.semantickernel.planner.actionplanner.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static soham.sksamples.util.Constants.*;
import static soham.sksamples.util.KernelUtils.kernel;

public class Example09_ActionPlanner {

    private static final Logger log = LoggerFactory.getLogger(Example09_ActionPlanner.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();

            log.debug("== Adding multiple skills to kernel ==");
            kernel.importSkillFromDirectory("WriterSkill", "src/main/resources/Skills", "WriterSkill");
            kernel.importSkillFromDirectory("SummarizeSkill", "src/main/resources/Skills", "SummarizeSkill");
            kernel.importSkillFromDirectory("DesignThinkingSkill", "src/main/resources/Skills", "DesignThinkingSkill");

            log.debug("== Create a Planner using the kernel ==");
            ActionPlanner planner = new ActionPlanner(kernel, null);

            log.debug("== Example 1 : use Summarizer and Translator skills ==");
            translate(planner);
            log.debug("== Example 2 : use Rewrite skill ==");
            rewriteInAStyle(planner);
            log.debug("== Example 3 : use DesignThinking skill and compose an email ==");
            designThinking(planner);
        } catch (ConfigurationException e) {
            log.error("Problem in paradise", e);
        }
    }

    private static void designThinking(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = CallTranscript + """
                    =====
                    apply Design Thinking to above call transcript.
                    =====""";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, goal);
    }

    private static void rewriteInAStyle(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = TextToSummarize + """
                    =====
                    rewrite the above content in Yoda from Starwars style.
                    =====""";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, goal);
    }

    private static void translate(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = TextToSummarize + """
                        =====
                        translate it to Dutch.
                        =====""";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, goal);
    }

    private static void printResult(Plan result, String goal) {
        log.debug("== Result ==" + result.getName());
        log.debug(result.invokeAsync(goal).block().getResult());
    }
}
