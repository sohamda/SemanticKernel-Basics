# SemanticKernel-Basics
Code examples of Semantic Kernel with Java SDK

## Pre reqs to run the samples
1. Check out [Semantic Kernel Java Branch](https://github.com/microsoft/semantic-kernel/tree/experimental-java/java) and run ```mvn install```.
   2. The samples are based on the checkout from 8-Aug-2023
2. Create [Azure Open AI](https://learn.microsoft.com/en-us/azure/ai-services/openai/how-to/create-resource?pivots=web-portal) resource in Azure portal and deploy 'gpt-35-turbo' and 'text-embedding-ada-002' models there.
3. Optionally, create [Azure Congnitive Search](https://learn.microsoft.com/en-us/azure/search/search-create-service-portal) resource, if you want to run the cognitive search sample (```Example06_SKwithCognitiveSearch.class```).

## Samples
1. ````Example01_InlineFunction.class```` > Demonstrates, how to initialize a Kernel and use an Inline function with Azure Open AI(AOAI).
2. ````Example02_SummarizerAsPrompt.class```` > Demonstrates, how to use a Skill and Function from skills directory with a skprompt.txt and config.json.
3. ````Example03_SummarizerAsPlugin.class```` > Demonstrates, how to use a Skill and Function defined as plugin.
4. ````Example04_SkillsPipeline.class```` > Demonstrates, how to use a Skill and Function in a pipeline or chain, passing output from one function to another as input.
5. ````Example05_SKwithMemory.class```` > Demonstrates, how to use a volatile memory (local simulation of a vector DB) with AOAI.
6. ````Example06_SKwithCognitiveSearch.class```` > Demonstrates, how to use a Azure Cognitive Search with AOAI.

## Bonus Sample
````Example07_DesignThinking.class````

Sample of Stanford University's approach to "Design Thinking" with phases: Empathize, Define, Ideate, Prototype, Test.

Inspired from [SKRecipes](https://github.com/johnmaeda/SK-Recipes/tree/main)


