## KMS Compliance Checker

A Java compiler plugin that proves, at compile-time, that Java code only requests 256-bit data keys from [AWS KMS](https://aws.amazon.com/kms/).

## What's it for?

A common compliance requirement (e.g. for SOC or PCI-DSS) is that data encrypted at rest must be encrypted with 256-bit keys. If you're using AWS KMS to generate data keys that encrypt your data, this checker can enforce that every data key you request from KMS is 256-bit. In a traditional manual audit, an auditor may not be able to examine every line of your code. By contrast, running this checker is automatic and comprehensive: the checker scans an entire codebase in seconds and reports any violations. If the check passes, you can be confident that you never request keys shorter than 256 bits from AWS KMS.

## Usage

The Maven coordinates for the latest version are

```software.amazon.checkerframework:aws-kms-compliance-checker:1.0.2```

The (https://github.com/awslabs/aws-kms-compliance-checker/tree/master/examples)[examples] directory
has examples of how to use the checker with some popular Java build systems.

### Gradle

To use Gradle add the Checker Framework and KMS key length checker to the dependencies
as shown in (https://github.com/awslabs/aws-kms-compliance-checker/blob/master/examples/build.gradle)[build.gradle].

```
dependencies {
  ...
  annotationProcessor "software.amazon.checkerframework:aws-kms-compliance-checker:1.0.2"
  checkerFrameworkAnnotatedJDK "org.checkerframework:jdk8:3.2.0"
  implementation "org.checkerframework:checker-qual:3.2.0"
}

configurations {
  checkerFrameworkAnnotatedJDK
}
```

and add this checker to the list of processors

```
   options.compilerArgs = [
        '-processor', 'com.amazon.checkerframework.compliance.kms.ComplianceChecker, ...,
        '-Xbootclasspath/p:${configurations.checkerFrameworkAnnotatedJDK.asPath}'
   ]
```

Now when you run `gradle build` the build will fail if you are using 128-bit keys.

### Maven

For an example project using Maven, see the
https://github.com/awslabs/aws-kms-compliance-checker/blob/master/examples/pom.xml)[example pom.xml].
Running `mvn compile` will fail if you are using 128-bit keys.

### Ant

For an example project using Ant and Ivy for dependency resolution, see the
(https://github.com/awslabs/aws-kms-compliance-checker/blob/master/examples/build.xml)[example build.xml].
Running `ant compile` will fail if you are using 128-bit keys.

## How does it work?

The compliance checker builds on the [Checker Framework](https://checkerframework.org/), an open-source tool licensed under the GPL 2.0 with Classpath Exception, for building extensions to the Java compiler's typechecker. A typechecker is perfect for checking a compliance rule, because typecheckers are *sound*, meaning that they never miss errors. In other words, a typechecker over-approximates what your program might do at runtime, so if the checker reports that the code is safe, you can be confident that it is.

The checker might report false positives.
If the checker issues an error, there are three possibilities:

1. There is a real error: you are calling AWS KMS and requesting a 128-bit key.
2. Your code does not pass a literal such as the string `"AES_256"` to AWS KMS; for example, you pass to AWS KMS the result of a method call. You need to write a type annotation on the method return type (which the checker will ensure is true!); see immediately below.
3. Your code is too complicated for the typechecker to model, so it gives up and assumes the worst. You can either change your code so that it is clearer (which will probably help humans to understand it, too!), or you can suppress the warning to tell the typechecker to trust you, by writing `@SuppressWarnings("kws-compliance")`.

## Writing annotations

If running the check fails the first time, you may need to add annotations, because the checker only reasons inside methods. If constants are not passed to the KMS API directly, you can use [`@StringVal`](https://checkerframework.org/api/org/checkerframework/common/value/qual/StringVal.html) for methods that return constant strings, and [`@IntVal`](https://checkerframework.org/api/org/checkerframework/common/value/qual/IntVal.html) for methods that return constant integers, to allow the checker to reason about them. These annotations are defined in the [`org.checkerframework:checker-qual` Maven artifact](https://mvnrepository.com/artifact/org.checkerframework/checker-qual), which you can add as a dependency if you need them (it only contains the annotation definitions, and has an MIT license). For example, suppose that you have the following call to the KMS API:

```
GenerateDataKeyRequest keyGenReq = new GenerateDataKeyRequest().withKeySpec(contentCryptoScheme.getKeySpec());
```

This call is not obviously compliant: the checker does not know what `contentCryptoScheme.getKeySpec()` returns. Suppose that `contentCrpytoScheme`'s definition of `getKeySpec()` is the following:

```
String getKeySpec() { return getKeyGeneratorAlgorithm() + "_" + getKeyLengthInBits(); }
```

and the definitions of `getKeyGeneratorAlgorithm` and `getKeyLengthInBits` are the following:

```
String getKeyGeneratorAlgorithm() { return "AES"; }
```
and

```
int getKeyLengthInBits() { return 256; }
```

The `getKeySpec()` method always returns the string `"AES_256"`, which is the compliance requirement. But, the checker only works within each method unless it has a little help. This is where annotations come in: we can annotate the results of these methods so that the checker knows that they always return constants. These return types will be checked against the bodies of the methods. Since `getKeyLengthInBits()` always returns `256`, we can add an `@IntVal(256)` annotation to it:

```
@IntVal(256) int getKeyLengthInBits() { return 256; }
```

Similarly, since `getKeyGeneratorAlgorithm()` always returns `"AES"`, we can add a `@StringVal("AES")` annotation to it:

```
@StringVal("AES") String getKeyGeneratorAlgorithm() { return "AES"; }
```

These annotations are checked, not trusted. Changing the return value of either method will cause the checker to fail. Together, the annotations inform the checker that `getKeySpec()` will always return the string `"AES_256"`. For that annotation to propagate up to the original call to KMS, we just add that annotation to `getKeySpec()` that expresses the fact we need:

```
@StringVal("AES_256") String getKeySpec() { return getKeyGeneratorAlgorithm() + "_" + getKeyLengthInBits(); }
```

With these annotations, the compliance check now passes; the checker is able to reason through most code inside methods -- so in this example it has no trouble with string concatenation or the implicit cast from `int` to `String`.

Note that these annotations were only required because of the indirection in the code. In most cases when a constant is passed to the KMS API directly no annotations will be necessary.

## License

This library is licensed under the Apache 2.0 License.
