package soham.sksamples.skill;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.Locale;

public class TextPlugin {

    @DefineKernelFunction(description = "Change all string chars to uppercase.", name = "uppercase")
    public String uppercase(
            @KernelFunctionParameter(name = "text", description = "Text to uppercase") String text) {
        return text.toUpperCase(Locale.ROOT);
    }

    @DefineKernelFunction(description = "Change all string chars to lowercase", name = "lowercase", returnType = "String")
    public String lowercase(
            @KernelFunctionParameter(name = "text", description = "Text to lowercase") String text) {
        return text.toLowerCase(Locale.ROOT);
    }
}
