# Result4K

[![Kotlin](https://img.shields.io/badge/kotlin-1.1.4-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/npryce/result4k.svg?branch=master)](https://travis-ci.org/npryce/result4k)
[![Maven Central](https://img.shields.io/maven-central/v/com.natpryce/result4k.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.natpryce%22%20AND%20a%3A%22result4k%22)


A Result monad for Kotlin

## Quick start

Kotlin does not type-check exceptions.  Result4k lets you type-check error handling.  

A `Result<T,E>` represents the result of a calculation of a _T_ value that might fail with an error of type _E_.

You can use a `when` expression to determine if a Result represents a success or a failure, but most of the time you don't need to.  Result4k type provides many useful operations handling success or failure without explicit conditionals.

