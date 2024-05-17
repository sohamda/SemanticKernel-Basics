package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.*;

public class Example01_InlineFunction {

    private static final Logger log = LoggerFactory.getLogger(Example01_InlineFunction.class);

    public static void main(String[] args) {
        try {

            log.debug("== Instantiates the Kernel ==");
            Kernel kernel = chatCompletionKernel(null);
            log.debug("== Define inline function ==");
            String promptTemplate = """
                {{$input}}
                
                Summarize the content above in less than 250 characters.
                """;

            log.debug("== Configure the prompt execution settings ==");
            KernelFunction<Object> excuseFunction = KernelFunctionFromPrompt.builder()
                    .withTemplate(promptTemplate)
                    .withDefaultExecutionSettings(
                            PromptExecutionSettings.builder()
                                    .withTemperature(0.4)
                                    .withTopP(1)
                                    .withMaxTokens(1000)
                                    .build())
                    .build();

            log.debug("== Run the Kernel ==");

            FunctionResult<Object> result = kernel
                    .invokeAsync(excuseFunction)
                    .withArguments(
                            KernelFunctionArguments.builder()
                                    .withInput(TextToSummarize)
                                    .build())
                    .block();

            log.debug("== Result ==");
            System.out.println(result.getResult());

        } catch ( IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}
