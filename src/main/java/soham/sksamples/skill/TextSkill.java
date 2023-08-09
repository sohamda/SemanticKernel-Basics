package soham.sksamples.skill;

import com.microsoft.semantickernel.skilldefinition.annotations.DefineSKFunction;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionInputAttribute;

import java.util.Locale;

public class TextSkill {

    @DefineSKFunction(description = "Change all string chars to uppercase.", name = "Uppercase")
    public String uppercase(
            @SKFunctionInputAttribute(description = "Text to uppercase") String text) {
        return text.toUpperCase(Locale.ROOT);
    }

    @DefineSKFunction(description = "Change all string chars to lowercase", name = "lowercase")
    public String lowercase(
            @SKFunctionInputAttribute(description = "Text to lowercase") String input) {
        return input.toLowerCase(Locale.ROOT);
    }
}
