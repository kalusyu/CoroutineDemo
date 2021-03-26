package com.example.coroutinedemo

import kotlinx.coroutines.*

/**
 * desc:
 *
 * @author biaowen.yu
 * @date 2021/3/25 11:08
 *
 * runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起，
 * 会释放底层线程用于其他用途。 由于存在这点差异，runBlocking 是常规函数，
 * 而 coroutineScope 是挂起函数。
 *
 **/
fun main() = runBlocking {
    val job = GlobalScope.launch {
        delay(1000)
        println("World!!!")
    }

    print("Hello,")
//    delay(2000)
    job.join()

    // 由于是 runBlocking 所以会等待job执行完才执行testWhy
    testWhy()
    testField()
    testDamon()
}

/**
 * 如果我们忘记保持对新启动的协程的引用，它还会继续运行。
 * 如果协程中的代码挂起了会怎么样（例如，我们错误地延迟了太长时间），
 * 如果我们启动了太多的协程并导致内存不足会怎么样？
 *
 * 答：
 * 我们可以在代码中使用结构化并发。 我们可以在执行操作所在的指定作用域内启动协程，
 * 而不是像通常使用线程（线程总是全局的）那样在 GlobalScope 中启动
 *
 * 因为外部协程直到在其作用域中启动的所有协程都执行完毕后才会结束
 */
suspend fun testWhy() = runBlocking {
    // GlobalScope.launch 时，我们会创建一个顶层协程。虽然它很轻量，但它运行时仍会消耗一些内存资源
    launch {
        delay(1000)
        println("World!")
    }

    print("Hello,")

}

suspend fun testField() = runBlocking { // this: CoroutineScope
    launch {
        delay(200L)
        println("Task from runBlocking")
    }

    coroutineScope { // 创建一个协程作用域
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
    }

    println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
}

/**
 * 计算任务协程无法取消？
 */
suspend fun testDamon() {
    val startTime = System.currentTimeMillis()
    val job = GlobalScope.launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}