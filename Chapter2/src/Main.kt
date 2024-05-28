fun main() {
//    한 줄짜리 주석

    /*
    * 여러줄 주석
    /*
    자바는 코틀린과 다르게 여러줄 주석에 여러 줄 주석을 여러번 내포 할 수 있다.
    */
     */

    /**
     * KDoc
     * */

    /*
     !!는 널 아님 단언(not-null assertion)으로 readLine()가 null인 경우 예외를 발생시킨다.
     readLine()가 null을 반환하지 않을 것이 확실 하므로 !!을 통해 null이 될 수 있는 가능성을 무시하게 만든다.
    */

    val a = readLine()!!.toInt()
    val b = readLine()!!.toInt()
    println(a+b)

    /*
    리터럴에 _ 를 넣어서 가독성 향상
    * */

    val n = 34_721_189

    /*
    각 정수 타입에는 최소값과 최대값을 포함하는 상수가 들어있다.
    * */

    Short.MAX_VALUE // 32767
    Short.MIN_VALUE // -32768

    /*
    코틀린은 과학적 표기법 리터럴을 허용한다.
    * */

    val pi = 0.314E1 // 3.14 = 0.314*10
    val pi100 = 0.314E3 // 314.0 = 0.314 * 10의 3승

    /*
    디폴트로 부동소수점 리터럴은 Double 타입이다. F나 f를 리터럴 뒤에 붙이면 Float 타입이된다.
    또한 Float 리터럴이 자동으로 Double 타입으로 변환되지 않는다.

    val pi:Double = 3.14f // Error
    * */


    /*
    Float와 Double도 몇가지 상수를 제공한다.
    * */

    Float.MIN_VALUE
    Double.MAX_VALUE
    Double.POSITIVE_INFINITY // 양의 무한대
    Double.NEGATIVE_INFINITY // 음의 무한대
    Float.NaN // NaN

    /*
    몫을 계선할떄는 floorDiv() 메서드를 사용한다.
    * */

    val q = 10
    val w = 25

    print(w.floorDiv(q))

    /*
    비트연산 (정수형에 적용 가능)
    * */
//    왼쪽 시프트
    q shl 2
//    오른쪽 시프트
    q shr 2
//    부호 없는 오른쪽 시프트
    q ushr 2
//    비트곱(and)
    q and 2
//    비트 합(or)
    q or 2
//    비트 배타합(xor)
    q xor 2
//    비트 반전
    q.inv()

    /*
    수변환
    toByte(), toShort(), toInt(), toLong(), toFloat(), toDouble(), toChar()
    주의 할점으로 자바와 달리 코틀린은 범위가 더작은 값은 Int 값을 Long 변수에 대입할 수 없다.

    val n = 100 // Int
    val l : Long = n // Error: can't Int to Long

    이렇게 된 이유는 암시적인 박싱때문이다. 즉 일반적인 Int 값이 꼭 원시타입으로 표현된다는 보장이 없기 떄문이다.

    위의 코드를 인정하면 아래의 연산이 false를 출력한다. 즉, 동등성이 만족되지 못한다.

    print(l == n)

    자바에서도 같은 오류가 발생한다.
    Integer n = 100;
    Long l = n; // Error: can't Integer to Long

    * */

    /*
    불(Boolean)이 지원하는 연산은 다음과 같다.
    ! : 논리 부정
    or, and, xor: 즉시 계산 방식의 논리합, 논리곱, 논리배타합
    ||, &&: 지연 계산 방식의 논리합, 논리곱

    자바와 달리 코틀린은 &와 | 를 제공하지 않는다. and 와 or이 각각을 대신한다
    * */

    val k = 10
    val j = 20

    println(k == 1 || j == 1)
    println((k == 1) or (j == 1))

    /*
    기본적으로 코틀린은 두인자가 모두 같은 타입일때만 ==, != 를 허용한다.

    val a = 1
    val b = 2L

    println(a == b) // Error: comparing Int and Long

    하지만 모든 수타입은 <,<=,>,>= 를 사용해서 비교할 수 있다.
    * */

    /*
    자바는 equals()를 통해 문자열 내용을 비교하지만 코틀린에서는 == 가 equals()를 가르키는 편의문법(syntatic sugar)이기
    때문에 == 를 사용하고 참조 동등성을 확인해고 싶다면 ===, !== 을 사용한다.
    * */
    val s1 = "jungi"
    val s2 = "Jungi"

    print(s1 == s2)
    print(s1 === s2)

    /*
    다은은 문자열이 제공하는 유용한 함수들이다.
    isEmpty(), isNotEmpty(), subString(), endsWith(), startsWith(), indexOf()
    * */

    // 배열 정의하기
    var arr = emptyArray<String>() // 원소 0개
    arr = arrayOf("hello","world") // 원소 2개
    val arr2 = arrayOf(1,4,9) // 원소 3개

    /*
    코틀린은 더 효율적인 ByteArray, ShortArray, IntArray, LongArray, FloatArray, DoubleArray
    CharArray, BooleanArray 라는 특화된 배열타입을 제공한다. 이런 특화된 배열에도 arrayOf()나 Array()에
    해당하는 함수가 따라온다.
    * */

    val operations = charArrayOf('+','-','*','/','%')
    val squares = IntArray(10){(it + 1)*(it + 1)}


    /*
    문자열과 달리 배열은 원고를 변경 할수 있다.
    * */
    squares[2] = 1

    // 원본을 별도로 배열을 만들고 싶다면 copyOf() 함수를 사용해야한다.
    val numbers = squares.copyOf()

    /*
    자바에서는 상위타입의 배열에 하위타입의 배열을 대입 할 수 있었다. 배열이 가변 데이터 구조이므로 이런
    대입은 런타입시 문제를 발생 시킬 수 있다.

    Object[] object = new String[] {"one","two","three"}
    object[0] = new Object() //ArrayStoreException 예외가 발생함

    이런 이유로 코틀린은 모든 다른 배열 타입과 서로 하위타입 관계가 성립되지 않는다고 간주하며, 앞서 본 대입도 금지된다.
    코틀린에서 String은 Any의 하위타입이지만 Array<String>은 Array<Any>의 하위타입이 아니다.

    val strings = arrayOf("one","two","three")
    val objects : Array<Any> = strings // 예외(오류)
    * */

    // 배열 자체는 생성하고 나면 길이를 바꿀 수 없지만 + 연산자를 이용해서 원소를 추가한 배열을 새로 만들 수 있다.
    val p = intArrayOf(1,2,3) + 4 + intArrayOf(7,8,9)

    // 문자열과 달리 배열에 대한 ==, != 연산자는 원소자체를 비교하지 않고 참조값을 비교한다.
    intArrayOf(1,2,3) == intArrayOf(1,2,3) // false

    // 배열 내용을 비교하고 싶으면 contentEquals() 함수를 사용하라
    intArrayOf(1,2,3).contentEquals(intArrayOf(1,2,3)) // true
}