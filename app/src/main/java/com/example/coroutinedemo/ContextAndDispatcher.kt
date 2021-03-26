package com.example.coroutinedemo

import kotlinx.coroutines.*

/**
 * desc:协程上下文与调度器
 *
 * @author biaowen.yu
 * @date 2021/3/26 11:24
 *
 **/

fun main() = runBlocking {

    // 调度器与线程
    // 当调用 launch { …… } 时不传参数，它从启动了它的 CoroutineScope 中承袭了上下文（以及调度器）。
    // 在这个案例中，它从 main 线程中的 runBlocking 主协程承袭了上下文
    // 当协程在 GlobalScope 中启动时，使用的是由 Dispatchers.Default 代表的默认调度器
    /*launch { // 运行在父协程的上下文中，即 runBlocking 主协程
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // 将会获取默认调度器
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }*/


    // 非受限调度器 vs 受限调度器
    /*launch(Dispatchers.Unconfined) { // 非受限的——将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch { // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }*/


    // 调试协程与线程.协程可以在一个线程上挂起并在其它线程上恢复
    // IDE调试，日志调试
    // 使用 -Dkotlinx.coroutines.debug JVM 参数运行下面的代码
    /*val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")*/


    // 子协程。当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
    /*val request = launch {
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }

        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // 取消请求（request）的执行
    delay(1000) // 延迟一秒钟来看看发生了什么
    println("main: Who has survived request cancellation?")*/

//    父协程的职责 ,一个父协程总是等待所有的子协程执行结束。父协程并不显式的跟踪所有子协程的启动
    /* val request = launch {
         repeat(3){i->
             launch {
                 delay((i+1) * 200L)
                 println("Coroutine $i is done")
             }

         }
         println("request: I'm done and I don't explicitly join my children that are still active")
     }

     request.join()
     println("Now processing of the request is complete")*/

    // 组合上下文中的元素 ,有时我们需要在协程上下文中定义多个元素。我们可以使用 + 操作符来实现
    /*launch (Dispatchers.Default + CoroutineName("Test111")){
        println("I'm working in thread ${Thread.currentThread().name}")
    }*/

    // 协程作用域, CoroutineScope 实例来管理协程的生命周期，并使它与 activity 的生命周期相关联
    // viewModelScope 怎么实现的，viewModelScope 是ViewModel的扩展方法，通过 setTagIfAbsent 将协程放入到map中
    // 当viewModel调用 clear 方法的时候 调用 其实现的Closeable接口，从而取消协程
    // MainScope需要在android下使用
    /*val activityDemo = ActivityDemo()
    activityDemo.doSomeThing()
    println("Launched coroutines")
    delay(500L)
    println("Destroying activity")
    activityDemo.onDestroy()
    delay(1000L)*/


    val aaaaa = 3

}


class ActivityDemo {
    private val mainScope = MainScope()

    fun onDestroy() {
        mainScope.cancel()
    }


    fun doSomeThing() {
        repeat(8) { i ->

            mainScope.launch {
                delay((i + 1) * 200L)
                println("Coroutine $i done")
            }

        }
    }
}