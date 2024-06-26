package soham.sksamples;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionInvocation;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

import static soham.sksamples.util.Constants.TextToSummarize;
import static soham.sksamples.util.KernelUtils.chatCompletionKernel;

public class Example02_SummarizerAsPrompt {

    private static final Logger log = LoggerFactory.getLogger(Example02_SummarizerAsPrompt.class);

    public static void main(String[] args) {
        try {
            log.debug("== Instantiates the Kernel ==");
            KernelPlugin summarize = KernelPluginFactory
                    .importPluginFromDirectory(Path.of("src/main/resources/Skills"),
                            "SummarizeSkill",null);
            Kernel kernel = chatCompletionKernel(summarize);

            log.debug("== Set Summarize Skill ==");
            KernelFunctionArguments arguments = KernelFunctionArguments
                    .builder()
                    .withVariable("input", TextToSummarize).build();

            log.debug("== Run the Kernel ==");
            FunctionInvocation<String> result = kernel.invokeAsync(summarize.<String>get("Summarize"))
                    .withArguments(arguments);

            log.debug("== Result ==");
            log.debug(result.block().getResult());

        } catch (IOException e) {
            log.error("Problem in paradise", e);
        }
    }
}