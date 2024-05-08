package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.planner.actionplanner.Plan;
import com.microsoft.semantickernel.planner.sequentialplanner.SequentialPlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soham.sksamples.skill.MyAppSkill;

import java.io.IOException;

import static soham.sksamples.util.KernelUtils.kernel;

public class Example10_Announcement {

    private static final Logger log = LoggerFactory.getLogger(Example10_Announcement.class);

    public static void main(String[] args) {
        log.debug("== Instantiates the Kernel ==");
        Kernel kernel = null;
        try {
            kernel = kernel();
            kernel.importSkill(new MyAppSkill(), "MyAppSkills");

            SequentialPlanner planner = new SequentialPlanner(kernel, null, null);
            Plan plan = planner.createPlanAsync(
                            "For any input with passwords, redact the passwords")
                    .block();

            log.debug(plan.toPlanString());

            String message = "how to cancel my booking";
            SKContext context = plan.invokeAsync(message).block();
            log.debug(context.getSkills().asMap().keySet().toString());
            String result = context.getResult();

            log.debug(" === Result of the plan === ");
            log.debug(result);

        } catch (ConfigurationException | IOException e) {
            log.error("Problem in paradise", e);
        }


    }
}
