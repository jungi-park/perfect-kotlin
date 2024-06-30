fun main() {
    println("Hello, KotlinVerse!")

    fun testTTT(action: (num: Int) -> Unit) = action(9)

    testTTT { if (it > 10) println("10 보다 커요") else println("10보다 작아요") }

    fun testTwo(num: Int, action: (b: Int) -> Unit) = action(num)

    testTwo(11) { if (it > 10) println("10 보다 커요") else println("10보다 작아요") }

    fun testThree(forNum: Int, plusNum: Int, action: (a: Int, b: Int) -> Int) = action(forNum, plusNum)

    testThree(10, 2) { a, b ->
        var result = 0
        for (i in 1..a) {
            result += b
        }
        println("result = $result")
        result
    }

}