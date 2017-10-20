package com.natpryce

import org.junit.Test
import kotlin.test.assertEquals

class AllValuesTests {
    @Test
    fun `returns values of an iterable of results, if all are ok`() {
        assertEquals(Ok(listOf(1,2,3)), listOf(Ok(1), Ok(2), Ok(3)).allValues())
    }
    
    @Test
    fun `returns first error encountered, if not all are ok`() {
        assertEquals(Err("bad"), listOf(Ok(1), Err("bad"), Ok(3)).allValues())
    }
}

class AnyValuesTests {
    @Test
    fun `returns any values of an iterable of results, dropping errors`() {
        assertEquals(listOf(1,2,3), listOf(Ok(1), Ok(2), Ok(3)).anyValues())
        assertEquals(listOf(1,3), listOf(Ok(1), Err("bad"), Ok(3)).anyValues())
        assertEquals(emptyList(), listOf(Err("bad")).anyValues())
    }
}

class PartitionTests {
    @Test
    fun `returns values and errors in separate lists`() {
        assertEquals(Pair(listOf(1,3), listOf("bad", "also bad")),
            listOf(Ok(1), Err("bad"), Ok(3), Err("also bad")).partition())
        assertEquals(Pair(listOf(1,2,3), emptyList()),
            listOf(Ok(1), Ok(2), Ok(3)).partition())
        assertEquals(Pair(emptyList(), listOf("bad", "also bad")),
            listOf(Err("bad"), Err("also bad")).partition())
    }
}
