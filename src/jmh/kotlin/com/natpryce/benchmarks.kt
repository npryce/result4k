package com.natpryce

import com.natpryce.Result4kBenchmark.Error.DID_NOT_WORK
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class ExceptionsBenchmark {
    @Benchmark
    fun success(blackhole: Blackhole) {
        measure(blackhole) { succeed() }
    }
    
    @Benchmark
    fun failure(blackhole: Blackhole) {
        measure(blackhole) { fail() }
    }
    
    private inline fun measure(blackhole: Blackhole, action: () -> String) {
        try {
            blackhole.consume(action())
        }
        catch (e: Exception) {
            blackhole.consume(e)
        }
    }
    
    companion object {
        private fun succeed(): String = "ok"
        private fun fail(): Nothing = throw Exception()
    }
}

open class NullableBenchmark {
    @Benchmark
    fun success(blackhole: Blackhole) {
        measure(blackhole) { succeed() }
    }
    
    @Benchmark
    fun failure(blackhole: Blackhole) {
        measure(blackhole) { fail() }
    }
    
    private inline fun measure(blackhole: Blackhole, action: () -> String?) {
        val result = action()
        if (result != null) {
            blackhole.consume(result)
        }
        else {
            blackhole.consume("failure")
        }
    }
    
    companion object {
        private fun succeed(): String? = "ok"
        private fun fail(): String? = null
    }
}

open class Result4kBenchmark {
    enum class Error { DID_NOT_WORK }
    
    @Benchmark
    fun success(blackhole: Blackhole) {
        measure(blackhole) { succeed() }
    }
    
    @Benchmark
    fun failure(blackhole: Blackhole) {
        measure(blackhole) { fail() }
    }
    
    private inline fun measure(blackhole: Blackhole, action: () -> Result<String,Error>) {
        action()
            .peek { blackhole.consume(it) }
            .peekFailure { blackhole.consume("failure") }
    }
    
    companion object {
        private fun succeed() = Success("ok")
        private fun fail() = Failure(DID_NOT_WORK)
    }
}
