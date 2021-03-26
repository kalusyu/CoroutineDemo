package com.example.coroutinedemo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * desc: 异步流
 *
 * @author biaowen.yu
 * @date 2021/3/26 14:54
 *
 **/

fun simple(): List<Int> = listOf(1, 2, 3)

/*fun main() {
//    simple().forEach { println(it) }
}*/

fun main() = runBlocking {
    /*launch {
        for (k in 1..3){
            println("I'm not blocked $k")
            delay(100)
        }
    }
    simple2().collect {
        println(it)
    }*/

    // 流取消基础
    /*withTimeoutOrNull(250){
        simple2().collect {
            println("$it")
        }
    }

    println("Done")*/

    // 流构建器
    /*(1..3).asFlow().collect { println(it) }*/

    // 过渡流操作符
    /*(1..3).asFlow().map { request ->
        performRequest(request)
    }.collect { response -> println(response) }*/


    // 转换操作符
    /*(1..3).asFlow().transform { request ->
        emit("Making request $request")
        emit(performRequest(request))
    }.collect { response -> println(response) }*/

    // 限长操作符
    /*numbers().take(2).collect {
        println(it)
    }*/

    // 末端流操作符
    /*val sum = (1..5).asFlow().map { it * it }.reduce { accumulator, value ->
        accumulator + value
    }
    println("Sum = $sum")*/

    // 流是连续的
    /*(1..5).asFlow().filter {
        println("Filter $it")
        it % 2 == 0
    }.map {
        println("Map $it")
        "string $it"
    }.collect {
        println("Collect $it")
    }*/

    // flowOn 操作符,函数用于更改流发射的上下文
    /*emit().collect { value->
        println("Collected $value")
    }*/


    // 缓冲
    /*// 无缓冲的情况下
    val time = measureTimeMillis {
        simple2().collect { value ->
            delay(300)
            println(value)
        }
    }
    println("Time $time")

    // 有缓冲的时间是多少呢?
    val buffTime = measureTimeMillis {
        simple2().buffer().collect { value ->
            delay(300)
            println(value)
        }
    }
    println("BuffTime $buffTime")*/

    //合并
    //当流代表部分操作结果或操作状态更新时，可能没有必要处理每个值，而是只处理最新的那个
    /*val time = measureTimeMillis {
        simple2().conflate().collect {
            delay(300)
            println(it)
        }
    }
    println("time = $time")*/

//    处理最新值
    /*val time = measureTimeMillis {
        simple2().collectLatest {
            println(it)
            delay(300)
            println("collected $it")
        }
    }
    println("time = $time")*/


    // zip
    /*val nums = (1..3).asFlow()
    val strs = flowOf("one", "two", "three")
    nums.zip(strs){a, b -> "$a -> $b"}
        .collect {
            println(it)
        }*/


    // 展平流,连接模式由 flatMapConcat 与 flattenConcat 操作符实现


    val aaaa = 100
}

fun emit(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100)
        println("Emitting $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}


// 流
fun simple2(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100L)
        println("Emitting $i")
        emit(i)
    }
}

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}