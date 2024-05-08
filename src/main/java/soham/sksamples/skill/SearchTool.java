package soham.sksamples.skill;

import com.microsoft.semantickernel.skilldefinition.annotations.DefineSKFunction;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionInputAttribute;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchTool {

    Logger log = LoggerFactory.getLogger(SearchTool.class);

    @DefineSKFunction(name = "findFlightPolicyDetails", description = "These Terms of Service for flight booking, change or update, cancellation")
    public String findFlightPolicyDetails(
            @SKFunctionParameters(name="input", description = "user query to find a specific flight policy") String input) {

        log.debug("Invoking find flight policy details for question : {}", input);
        return "this was the result";
    }
}
