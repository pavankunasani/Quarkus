# Quarkus
Quakus-info
Quarkus is a modern Java framework designed for building lightweight, fast, and cloud-native applications. One of its key features is its ability to optimize Java applications for containerized environments, such as Docker and Kubernetes. This is largely due to its support for both OpenJDK HotSpot and GraalVM, which enables developers to compile Java code into native executables, improving startup times and reducing memory consumption.

Let’s break this down in more detail:

1. JVM vs. Native Compilation
Traditionally, Java applications run on the Java Virtual Machine (JVM). The JVM provides a lot of flexibility and portability since it allows Java programs to run on any machine that has a JVM implementation. However, running a Java application on the JVM comes with some downsides, especially in cloud-native environments:

Memory Overhead: The JVM requires a significant amount of memory for itself (JVM heap space, garbage collection, etc.).
Startup Time: The JVM startup time can be relatively slow because it needs to load the entire Java runtime and JIT (Just-In-Time) compilation needs to optimize the code as it runs.
Quarkus addresses this by supporting native image compilation.

2. Native Image Compilation with GraalVM
GraalVM is a high-performance runtime that provides the ability to compile Java applications into native executables. This is a huge advantage when building cloud-native applications that need to be efficient in terms of resource usage.

When you compile a Quarkus application using GraalVM:

Native Compilation: The entire application, including the application code and the libraries it uses, is compiled ahead of time into a native binary (platform-specific, e.g., Linux x86_64). This eliminates the need for a JVM at runtime.
No JVM Required: The resulting native executable doesn’t require a JVM to run. It’s a self-contained, optimized binary, significantly reducing the amount of memory and CPU resources needed.
Faster Startup Time: Since the application is pre-compiled, it can start almost instantly, compared to the traditional JVM-based approach where startup time can take several seconds or even minutes in some cases.
GraalVM’s Native Image takes care of various optimizations:

Ahead-of-Time (AOT) Compilation: This compiles the application directly into machine code, allowing it to run much faster.
Dead Code Elimination: It removes unused code paths, reducing the binary size.
Reduced Memory Footprint: Since it only includes the necessary parts of the application, it consumes far less memory than the JVM-based equivalent.
3. Quarkus and OpenJDK HotSpot
Quarkus also supports OpenJDK HotSpot for environments where native compilation with GraalVM isn’t feasible or necessary. HotSpot is the default JVM used for most Java applications. While it offers advanced features like JIT compilation and garbage collection, it does have a higher resource footprint compared to native images.

Quarkus optimizes applications to work efficiently even on HotSpot, with:

Reduced Startup Time: By optimizing dependencies and applying other techniques, Quarkus can still improve the startup time of a HotSpot-based Java application.
Smaller Memory Footprint: While it won’t be as small as a GraalVM-native application, Quarkus still reduces memory usage compared to traditional Java frameworks.
4. Impact on Containers (Linux and Kubernetes)
The goal of using Quarkus with GraalVM and OpenJDK HotSpot is particularly beneficial in containerized environments like Docker and Kubernetes. Here’s how:

Native Executables in Containers: In containerized environments (e.g., Docker), instead of running a full JVM inside the container, Quarkus applications can run as native Linux executables. This eliminates the need for a JVM in the container image, leading to smaller container sizes and reduced resource consumption.
Lower Memory and CPU Usage: Native executables have a smaller memory footprint and are faster to start. This is crucial in Kubernetes environments where resource efficiency is critical. Fewer resources mean that more pods can be run on the same infrastructure, and it reduces costs.
Scalability: Containers with Quarkus applications compiled to native code can start very quickly and use far fewer resources, which is ideal in dynamic cloud environments that rely on scaling and auto-scaling.
Faster Cold Starts: In serverless environments (e.g., AWS Lambda, Azure Functions), the ability to have a native executable means faster cold starts, as there’s no JVM startup involved.
5. Benefits of Quarkus with Native Compilation in Containers
Faster boot times: The application can boot in a fraction of the time it would take on a traditional JVM, which is crucial for handling bursts of traffic or when deploying frequently.
Lower Memory Footprint: Without the overhead of the JVM, Quarkus applications require significantly less memory. This is particularly valuable when running in constrained environments like Kubernetes or when deploying large numbers of microservices.
Smaller Docker Images: Native executable Quarkus applications generate much smaller Docker images compared to traditional JVM-based Java applications. This reduces the time it takes to pull images, making CI/CD pipelines faster and reducing network overhead.
Better resource utilization: You can run more instances of your application within the same resource limits, improving efficiency and reducing operational costs.
Example: Quarkus Native Image in Docker
Let’s say you have a Quarkus application that you want to package as a Docker container. If you use native image compilation with GraalVM, the Dockerfile might look something like this:

FROM ghcr.io/graalvm/graalvm-ce:latest as builder
WORKDIR /app
COPY . .
RUN gu install native-image
RUN ./mvnw clean package -Pnative

FROM ubuntu:20.04
WORKDIR /app
COPY --from=builder /app/target/my-quarkus-app-runner /app/my-quarkus-app
CMD ["./my-quarkus-app"]
Here, the application is built using GraalVM's native image compiler in the builder stage, and then it’s copied over into a slim runtime container based on Ubuntu. The resulting image contains just the native executable, not a full JVM.

Conclusion
In summary, Quarkus' support for OpenJDK HotSpot and GraalVM allows Java applications to be optimized for modern cloud-native environments like Kubernetes and Docker. With GraalVM’s native image compilation, Quarkus applications can run as lightweight, fast native executables, drastically reducing memory usage, startup time, and overall resource consumption. This makes Quarkus ideal for microservices, serverless functions, and containerized applications where efficiency and scalability are key considerations.


-----

Quarkus with OpenJDK vs Native Executable
When you develop an application with Quarkus, you essentially have two options for deployment:

Native executable (using GraalVM's native-image).
JVM-based executable (using OpenJDK HotSpot).
1. When Native Executables Are Used

If your deployment environment supports native executables (e.g., when you compile with GraalVM), Quarkus compiles your application into a native Linux binary.
No JVM required for running the application. The application becomes a self-contained binary, which means it doesn't rely on having a Java Runtime Environment (JRE) installed.
This is great for performance because it significantly reduces startup time and memory consumption.
2. When Native Executables Are Not Supported (Fallback to JVM)

Now, if the deployment environment does not support native executables (e.g., your machine doesn’t have GraalVM installed, or you didn’t build the app with GraalVM), Quarkus will fall back to using OpenJDK (the standard JVM).

Here's how it works:

No Native Image: If you didn't build your Quarkus application with the GraalVM native image or the platform doesn’t support it, Quarkus will run as a normal Java application. This means it will use the JVM (specifically OpenJDK HotSpot) to run the application.
JVM Requirement: Yes, in this case, you do need a Java Runtime Environment (JRE) or Java Development Kit (JDK) installed on the machine where the application runs. This is because the Quarkus application needs a JVM to execute, and the JVM provides the necessary environment to interpret and run Java bytecode.
So, how does Quarkus make the transition between native and JVM execution seamless?

Seamless JVM Fallback with Quarkus
Quarkus Development Mode: During development, Quarkus runs in JVM mode by default. This means if you're building and testing your application locally and haven't built the native executable yet, you can run your Quarkus application using your regular JVM (with OpenJDK installed). In this case, you’d need a Java runtime to start the application in development mode.
JVM Mode vs Native Mode:
When you want to build a native executable, you can use GraalVM’s native-image tool to compile your Quarkus app into a native binary. Once compiled, this native executable can be deployed without the need for a JVM.
If you decide not to use GraalVM for native compilation, or if you're running in an environment that doesn't support native images, Quarkus will fall back to using the JVM, just like any other traditional Java application.
Running in JVM Mode:
If you're deploying in an environment where the native image can't be used (for example, you don't have GraalVM installed or can't use native images for any reason), you can run your application with a standard OpenJDK installation.
Quarkus will automatically use the JVM (OpenJDK or other JVM implementations) as the runtime to execute the application.
Example of Running Quarkus with OpenJDK
If you want to run a Quarkus application in a JVM-based environment, here’s how you do it:

First, make sure you have a JRE or JDK installed (such as OpenJDK).
You’d typically use the mvn command to build the application:
mvn clean install
Then, to run it in JVM mode:
mvn quarkus:dev
This command will start the application with the standard JVM runtime, which requires a JDK (OpenJDK or any other JVM).

In Summary:
Native Executable (via GraalVM): If you’ve compiled your application with GraalVM into a native executable, it runs without requiring any JVM. It’s a self-contained binary.
JVM-based Execution (via OpenJDK): If the native executable is not available or the environment doesn’t support it, Quarkus will run using OpenJDK or any compatible JVM. In this case, you need a JRE or JDK installed on the machine where you are running the application.
Quarkus makes it easy to develop and deploy in both cases, but if you're targeting environments where you can take advantage of native execution (like containers or serverless), you'll want to use GraalVM for the native image compilation. If not, Quarkus will default to using the JVM as expected, and you’ll need a standard Java runtime (like OpenJDK) to run the application.

