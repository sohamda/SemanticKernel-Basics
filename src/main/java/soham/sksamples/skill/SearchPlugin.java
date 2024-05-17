package soham.sksamples.skill;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SearchPlugin {

    private static final Logger log = LoggerFactory.getLogger(SearchPlugin.class);
    @DefineKernelFunction(
            name = "SearchFromQuestion",
            description = "Search information relevant to answering a given query",
            returnType = "string")
    public String searchFromQuestion(
            @KernelFunctionParameter(
                    description = "the query to answer",
                    name = "query")
            String query) {

        log.info("Just returning empty string for {}", query);

        return "something interesting for " + query;
    }


    @DefineKernelFunction(
            name = "FindPerson",
            description = "find a person based on name",
            returnType = "soham.sksamples.skill.PersonEntity")
    public Mono<PersonEntity> findPerson(
            @KernelFunctionParameter(
                    description = "name of the person",
                    name = "name")
            String name) {

        log.info("Just returning empty Person for {}", name);

        return Mono.just(new PersonEntity(name, "Dasgupta", 41));
    }
}
