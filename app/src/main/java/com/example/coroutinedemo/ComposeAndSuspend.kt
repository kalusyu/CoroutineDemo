package com.example.coroutinedemo

import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

/**
 * desc: 组合挂起函数
 *
 * @author biaowen.yu
 * @date 2021/3/26 10:55
 *
 **/
fun main() = runBlocking {

    // 默认调用顺序 2s左右
    /*val time = measureTimeMillis {
        val one = doSthUseful1()
        val two = doSthUseful2()
        println("The result is ($one + $two)")
    }*/

    // 使用 async 并发, 会返回一个future 结果需要通过await等待 1s左右
    // async 立即启动一个新的协程
    /*val time = measureTimeMillis {
        val one = async { doSthUseful1() }
        val two = async { doSthUseful2() }
        println("The result is (${one.await()} + ${two.await()})")
    }*/

    // 惰性启动的 async， 通过CoroutineStart.LAZY,只有在使用start启动
    // 没有调用start 则是默认行为，为2s
    // 调用了 start 则是异步行为 为1s
    /*val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSthUseful1() }
        val two = async(start = CoroutineStart.LAZY) { doSthUseful2() }
        one.start()
        two.start()
        println("The result is (${one.await()} + ${two.await()})")
    }*/

    // async 风格的函数,不是挂起函数,后缀最好加上Async
    /*val time = measureTimeMillis {
        val one = doSth1Async()
        val two = doSth2Async()

        // 但是等待结果必须调用其它的挂起或者阻塞
        // 当我们等待结果的时候，这里我们使用 `runBlocking { …… }` 来阻塞主线程
        runBlocking {
            println("The result is (${one.await()} + ${two.await()})")
        }
    }*/

    // 使用 async 的结构化并发
    val time = measureTimeMillis {
        println("concurrentSum = ${concurrentSum()}")
    }

    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }



    println("Consume time $time")
}

suspend fun doSthUseful1(): Int {
    delay(1000)
    return 13
}

suspend fun doSthUseful2(): Int {
    delay(1000)
    return 29
}


// async 风格的函数,不是挂起函数,后缀最好加上Async
fun doSth1Async() = GlobalScope.async {
    doSthUseful1()
}

fun doSth2Async() = GlobalScope.async {
    doSthUseful2()
}


//如果在 concurrentSum 函数内部发生了错误，并且它抛出了一个异常， 所有在作用域中启动的协程都会被取消。
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSthUseful1() }
    val two = async { doSthUseful2() }

    one.await() + two.await()
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE)
            13
        } finally {
            println("First child was cancelled")
        }

    }

    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }

    one.await() + two.await()
}