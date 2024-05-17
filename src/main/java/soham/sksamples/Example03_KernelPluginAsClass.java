package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionInvocation;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soham.sksamples.skill.TextPlugin;

import java.io.IOException;

import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.chatCompletionKernel;

public class Example03_KernelPluginAsClass {
    private static final Logger log = LoggerFactory.getLogger(Example03_KernelPluginAsClass.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            KernelPlugin textPlugin = KernelPluginFactory
                    .createFromObject(new TextPlugin(), "TextPlugin");
            Kernel kernel = chatCompletionKernel(textPlugin);

            log.debug("== Set Summarize Skill ==");
            KernelFunctionArguments arguments = KernelFunctionArguments
                    .builder()
                    .withVariable("text", TextToSummarize).build();

            log.debug("== Run the Kernel ==");
            FunctionInvocation<String> result1 = kernel.invokeAsync(textPlugin.<String>get("uppercase"))
                    .withArguments(arguments);

            log.debug("== Result 1 ==");
            log.debug(result1.block().getResult());

            FunctionInvocation<String> result2 = kernel.invokeAsync(textPlugin.<String>get("lowercase"))
                    .withArguments(arguments);

            log.debug("== Result 2 ==");
            log.debug(result2.block().getResult());

        } catch (IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}
