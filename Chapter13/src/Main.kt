import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


//1. 코루틴과 일시 중단 함수
//
//
//
//코루틴 라이브러리의 기본 요소는 일시 중단 함수(suspend function) 이다.
//
//이 함수는 원하는 지점에서 함수의 실행을 중단하거나 다시 실행을 계속 진행하게 할 수 있다.


//suspend fun main() {
//    foo()
//}

suspend fun foo() {
    println("Task started")
    delay(100)
    println("Task finished")
}

/*
2. 코루틴 빌더



코루틴 빌더에는 자주 사용하는 launch(), async(), runBlocking() 이 있다.



launch() 함수는 코루틴을 시작하고, 코루틴을 실행 중인 작업에서 상태를 추적하고 변경할 수 있는 Job 객체를 돌려준다.

이 함수는 CoroutineScope.() -> Unit 타입의 일시 중단 람다를 받는다. 이 람다는 새 코루틴의 본문에 해당한다.

launch() 빌더는 동시성 작업이 결과를 만들어내지 않는 경우 적합하다. 그래서 이 빌더는 Unit 타입을 반환하는 람다를 인자로 받는다.
*/

//fun main() {
//    val time = System.currentTimeMillis()
//
//    GlobalScope.launch {
//        delay(100)
//        println("Task 1 finished in ${System.currentTimeMillis() - time} ms")
//    }
//
//    GlobalScope.launch {
//        delay(100)
//        println("Task 2 finished in ${System.currentTimeMillis() - time} ms")
//    }
//
//    Thread.sleep(200)
//}
/*
   Task 2 finished in 149 ms
   Task 1 finished in 149 ms
 */

/*
* 이 코드를 실행하면 다음과 같이 두 작업이 프로그램이 시작한 시점을 기준으로 거의 동시에 끝났다.
이를 통해 두 작업이 실제로 병렬적으로 실행되었지만 어느 한쪽이 더 먼저 표시될 수도 있는 것을 알 수 있다. 코루틴 라이브러리는 필요할 때 실행 순서를 강제할 수 있는 도구도 제공한다. 코루틴을 처리하는 스레드는 데몬 모드(daemon mode)로 실행되기 때문에 main() 스레드가 이 스레드보다 빨리 끝나버리면 자동으로 실행이 중단된다.
* */


/*
반대로 결과가 필요한 경우에는 async() 빌더 함수를 사용해야 한다.
이 함수는 Deferred의 인스턴스를 돌려주고, 이 인스턴스는 Job의 하위 타입으로 await() 메서드를 통해 계산 결과에 접근할 수 있게 해준다. await() 메서드를 호출하면 await()는 계산이 완료되거나 계산 작업이 취소될 때 까지 현재 코루틴을 일시 중단시킨다. 작업이 취소되는 경우 await()는 예외를 발생시키면서 실패한다.
* */

//suspend fun main() {
//    val message = GlobalScope.async {
//        delay(100)
//        "abc"
//    }
//
//    val count = GlobalScope.async {
//        delay(100)
//        1 + 2
//    }
//
//    delay(200)
//
//    val result = message.await().repeat(count.await())
//    println(result)
//}
/*
    abcabcabc

    이 경우 main()을 suspend로 표시해서 두 Deferred 작업에 대해 직접 await() 메서드를 호출했다.

    launch()와 async() 빌더의 경우 스레드 호출을 블럭시키지는 않지만, 백그라운드 스레드를 공유하는 풀을 통해 작업을 실행한다.

    runBlocking() 빌더는 디폴트로 현재 스레드에서 실행되는 코루틴을 만들고 코루틴이 완료될 때까지 현재 스레드의 실행을 블럭시킨다.
    코루틴이 성공적으로 끝나면 일시 중단 람다의 결과가 runBlocking() 호출의 결괏값이 된다. 코루틴이 취소되면 runBlocking()은 예외를 던진다
 */

//fun main() {
//    GlobalScope.launch {
//        delay(100)
//        println("Background task: ${Thread.currentThread().name}")
//    }
//    runBlocking {
//        println("Primary task: ${Thread.currentThread().name}")
//        delay(200)
//    }
//}
/*
    Primary task: main
    Background task: DefaultDispatcher-worker-1
 */


//runBlocking() 내부의 코루틴은 메인 스레드에서 실행되는 반면, launch()로 시작한 코루틴은 공유 풀에서 백그라운드 스레드를 할당 받았음을 알 수 있다


fun main() = runBlocking {
    // API 호출의 실행 시간을 측정
    val totalTime = measureTimeMillis {
        // 현재 스코프에서 async를 사용하여 결과를 반환하는 코루틴 생성
        val resultA = async {
            함수A()
        }

        // 현재 스코프에서 async를 사용하여 결과를 반환하는 코루틴 생성
        val resultB = async {
            함수B()
        }

        // 결과를 기다림
        println(resultA.await())
        println(resultB.await())
    }

    println("총 실행 시간: $totalTime ms")
}

/* suspend 함수로 정의
함수 A의 결과
함수 B의 결과
총 실행 시간: 1013 ms*/
suspend fun 함수A(): String {
    delay(1000) // 가상의 API 호출 대기 시간
    return "함수 A의 결과"
}

suspend fun 함수B(): String {
    delay(1000) // 가상의 API 호출 대기 시간
    return "함수 B의 결과"
}

/* 일반 함수로 정의 (suspend 없음)
함수 A의 결과
함수 B의 결과
총 실행 시간: 2021 ms*/
//fun 함수A(): String {
//    Thread.sleep(1000) // 가상의 API 호출 대기 시간
//    return "함수 A의 결과"
//}
//
//fun 함수B(): String {
//    Thread.sleep(1000) // 가상의 API 호출 대기 시간
//    return "함수 B의 결과"
//}