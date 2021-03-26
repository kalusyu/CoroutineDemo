package com.example.coroutinedemo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * desc:
 *
 * @author biaowen.yu
 * @date 2021/3/25 11:16
 *
 **/
class TestCoroutine {

    /**
     * 等待一个作业
     */
    @Test
    fun testWait() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000)
            println("World!")
        }
        print("Hello,")
        // 等待job协程执行完再结束
        job.join()
    }
}