# SemanticKernel-Basics
Code examples of Semantic Kernel with Java SDK (v1.0.1)

## Pre reqs to run the samples
1. Create [Azure Open AI](https://learn.microsoft.com/en-us/azure/ai-services/openai/how-to/create-resource?pivots=web-portal) resource in Azure portal and deploy 'gpt-x' models there OR use an OpenAI key.
2. Rename ````src/main/resources/conf.properties.example```` to ````src/main/resources/conf.properties```` and update it with the details. 

## Samples
1. ````Example01_InlineFunction.class```` > Demonstrates, how to initialize a Kernel and use an Inline function with Azure Open AI(AOAI).
2. ````Example02_SummarizerAsPrompt.class```` > Demonstrates, how to use a Plugin and Function from skills directory with a skprompt.txt and config.json.
3. ````Example03_KernelPluginAsClass.class```` > Demonstrates, how to use a Plugin defined as class.
4. ````Example04_PluginAutoInvocation.class```` > Demonstrates, how to let Kernel auto-invoke Plugins and Functions depending on the task given.
5. ````Example05_CustomObjectFromPlugin.class```` > Demonstrates, how to configure and use custom converters if a function returns or accepts custom object.

## Documentation/Further reading

1. [Semantic Kernel Official Docs](https://learn.microsoft.com/en-us/semantic-kernel/overview/)
2. [Semantic Kernel Github Repo](https://github.com/microsoft/semantic-kernel)
3. [In Depth Tutorials in Python & C#](https://learn.microsoft.com/en-us/semantic-kernel/get-started/tutorials)
4. [Samples in Java](https://github.com/microsoft/semantic-kernel/blob/java-v1/java/samples)
5. [Sample Chat App in Java](https://github.com/Azure-Samples/azure-search-openai-demo-java?tab=readme-ov-file)

