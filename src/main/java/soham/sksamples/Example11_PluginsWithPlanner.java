package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.planner.actionplanner.ActionPlanner;
import com.microsoft.semantickernel.planner.actionplanner.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soham.sksamples.skill.BookingHelper;
import soham.sksamples.skill.SearchTool;

import java.io.IOException;

import static soham.sksamples.util.KernelUtils.kernel;

public class Example11_PluginsWithPlanner {

    private static final Logger log = LoggerFactory.getLogger(Example11_PluginsWithPlanner.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = kernel();

            log.debug("== Adding multiple skills to kernel ==");
            kernel.importSkill(new BookingHelper(), "Plugin to get booking details or to update or delete bookings based on parameters such as {{$bookingNumber}}, {{$firstName}} and {{$lastName}}, {{$date}}, {{$from}} and {{$to}}");
            kernel.importSkill(new SearchTool(), "find the Terms of Service for flight booking, change or update, cancellation for {{$input}}");
            log.debug("== Create a Planner using the kernel ==");
            ActionPlanner planner = new ActionPlanner(kernel, null);

            log.debug("== Run kernel with Planner ==");
            //invokePlanner(planner, "what is the flight cancellation policy");
            //invokePlanner(planner, "my name is John Doe and my booking number is 101, please cancel my booking");
            //invokePlanner(planner, "my name is John Doe and my booking number is 101, flying from Amsterdam to Paris on 7.3.2024, please update my booking");
            invokePlanner(planner, "my name is John Doe and my booking number is 101, please get my booking details");
        } catch (ConfigurationException | IOException e) {
            log.error("Problem in paradise", e);
        }
    }
    private static void invokePlanner(ActionPlanner planner, String goal) {
        Plan result = planner.createPlanAsync(goal).block();
        log.debug("== Result ==" + result.getName());
        log.debug("Returned > {}", result.invokeAsync(goal).block());
    }
}


