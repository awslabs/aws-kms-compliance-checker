## KMS Compliance Checker

A Java compiler plugin that proves, at compile-time, that Java code only requests 256-bit data keys from AWS KMS.

## What's it for?

A common compliance requirement (e.g. for SOX or PCI-DSS) is that data encrypted at rest must be encrypted with 256-bit keys. If you're using AWS KMS to generate data keys that encrypt your data, this checker can enforce that every data key you request from KMS is 256-bit. Unlike a traditional audit, running this checker is automatic and comprehensive: while it might not be feasible for an auditor to look at all of your code, the checker is able to scan an entire codebase in seconds and report any violations. If the check passes, you can be confident that you never request keys shorter than 256-bits from AWS KMS.

## How does it work?

The checker builds on the Checker Framework (www.checkerframework.org), an open-source tool for building extensions to the Java compiler's typechecker. A typechecker is perfect for checking a compliance rule, because typecheckers are *sound*, meaning that they never miss errors, but might report false positives. In other words, a typechecker over-approximates what your program might do at runtime, so if the checker reports that the code is safe, you can be confident that it is. If the checker issues an error, there are three possibilities:
1. there is a real error: you are calling AWS KMS and requesting a 128-bit key, OR
2. your code calls into a function that returns a deterministic result. To make it scale to production Java programs, the checker works method-by-method, and doesn't assume anything about other methods beyond what's written in the type. But, if your code calls a method that, for instance, always returns the integer `256`, and you use that value to call KMS, you might want to rely on that fact. To do so, you can add a type annotation (which the checker will ensure is true!) to the method's return type, OR
3. your code is too complicated for the typechecker to model, so it gives up and assumes the worst. You can either change your code so that it is clearer (which will probably help humans to understand it, too!), or you can suppress the warning to tell the typechecker to trust you

## Writing annotations

If running the check fails the first time, you may need to add annotations, because the checker only reasons inside methods. If constants are not passed to the KMS API directly, you can use `@StringVal` for methods that return constant strings, and `@IntVal` for methods that return constant integers, to allow the checker to reason about them. These annotations are defined in the `org.checkerframework:checker-qual` Maven artifact, which you can add a dependency if you need them (it only contains the annotation definitions, and has an MIT license). For example, suppose that you have the following call to the KMS API:

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

With these annotations, the compliance check now passes; the checker is able to reason through most code inside methods - so in this example it has no trouble with string concatenation or the implicit cast from `int` to `String`.

Note that these annotations were only required because of the indirection in the code. In most cases when a constant is passed to the KMS API directly no annotations will be necessary.

## License

This library is licensed under the Apache 2.0 License. 
