package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.planner.actionplanner.ActionPlanner;
import com.microsoft.semantickernel.planner.actionplanner.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soham.sksamples.skill.SearchTool;

import java.io.IOException;

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
            kernel.importSkill(new SearchTool(), "Plugin to search policies related to flight booking update, cancellation");
            log.debug("== Create a Planner using the kernel ==");
            ActionPlanner planner = new ActionPlanner(kernel, null);
            noplanner(planner);
            log.debug("== Example 1 : use Summarizer and Translator skills ==");
          //  translate(planner);
            log.debug("== Example 2 : use Rewrite skill ==");
          //  rewriteInAStyle(planner);
            log.debug("== Example 3 : use DesignThinking skill and compose an email ==");
          //  designThinking(planner);
            } catch (ConfigurationException | IOException e) {
                log.error("Problem in paradise", e);
            }
        }

    private static void noplanner(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = "execute a task, if no proper functions found, don't do anything";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, "what is the flight cancellation policy");
    }

    private static void designThinking(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = "apply Design Thinking to above call transcript.";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, CallTranscript);
    }

    private static void rewriteInAStyle(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = "rewrite the above content in Yoda from Starwars style.";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, TextToSummarize);
    }

    private static void translate(ActionPlanner planner) {
        log.debug("== Run kernel with Planner ==");
        String goal = "translate it to Dutch.";
        Plan result = planner.createPlanAsync(goal).block();
        printResult(result, TextToSummarize);
    }

    private static void printResult(Plan result, String input) {
        log.debug("== Result ==" + result.getName());
        log.debug(result.invokeAsync(input).block().getResult());
    }
}
