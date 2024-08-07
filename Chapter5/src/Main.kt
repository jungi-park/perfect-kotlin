fun main() {

    /* 5.1.1 고차 함수
    앞에서 이미 람다를 통해 계산을 수행하는 몇가지 예제를 살펴봤다.
    파라미터로 함수타입을 받아 사용하는 경우를 살펴보자
    아래코드는 곱셈이나 최댓갑을 구하는 함수를 일반화해서 사용하고 있다.
    * */
    val squares = IntArray(5) { n -> n * n }

    fun aggregate(numbers: IntArray, op: (Int, Int) -> Int): Int {
        var result = numbers.firstOrNull() ?: throw IllegalArgumentException("Empty array")

        for (i in 1..numbers.lastIndex) result = op(result, numbers[i])

        return result
    }

    fun sum(numbers: IntArray) = aggregate(numbers) { result, op -> result + op }

    fun max(numbers: IntArray) = aggregate(numbers) { result, op -> if (result > op) op else result }

    sum(intArrayOf(1, 2, 3)) // 6
    max(intArrayOf(1, 2, 3)) // 3

    /* 5.1.2 함수 타입
    함수 타입은 함수처럼 쓰일 수 있는 값들을 표시하는 타입이다.
    문법적으로 함수 시그니처와 비슷하며, 다음과 같이 두가지 부분으로 구성된다.
    1. 괄호로 둘러싸인 파라미터 타입 목록은 함숫값에 전달될 데이터의 종류와 수를 정의한다.
    2. 반환 타입은 함수 타입의 함숫값을 호출하면 될려받게 되는 타입을 명시한다.
    반환 타입을 반드시 명시해야한다  () -> Unit
    ex) (Int,Int) -> Boolean

    코틀린 1.4부터는 코틀린 인터페이스 앞에 fun을 붙이면 자바의 SAM(Single Abstract Method) 인터페이스와 같이 사용할 수 있다.

    fun interface StringConsumer(){
        fun accept(s:String)
    }

    val consume = StringConsumer{s -> print(s)}

    함수타입의 값을 함수의 파라미터에만 사용할 수 있는 것이 아니라 모든 곳에서 사용가능하다.
    * */

    val lessThan: (Int, Int) -> Boolean = { a, b -> a < b }

    /*
    함수 타입도 널이 될 수 있는 타입으로 지정할 수 있다. 이럴 때는 함수 타입 전체를 괄호로 둘러싼 다음에 물음표를 붙인다.
    괄호를 붙이지 않으면 의미가 완전히 달라지니 주의해야한다 (a:Int,b:Int)->Int? (null을 포함한 Int라는 의미가됨)
    * */

    fun measure(op: ((a: Int, b: Int) -> Int)?) {
    }

    /*
    함수 타입을 다른 함수타입에 내포시켜서 고차함수를 정의할 수 있다.

    타입 (i) -> R
    구현 { i -> R }
    * */

    val high: (Int) -> (Int) -> Int = { n -> { i -> i + n } }


    /* 5.1.3 람다와 익명 함수
    {result, op -> result+op} 라는식을 람다라고 부른다.
    람다는 result, op 와같은 파라미터 목록과  result+op 의 본문으로 이루어져 있다.
    함수 정의와 달리 반환 타입을 지정할 필요가 없으며, 반환타입지 자동으로 추론된다. 그리고 람다 본문에서 맨 마지막에 있는 식이 람다의 결괏값이 된다.

    코틀린은 인자가 하나밖에 없는 람다를 특별히 단순화해 사용할 수 있는 문법을 제공한다.
    람다인자가 하나인 경우에는 파라미터 목록과 화살표 기호를 생략하고, 유일한 파라미터는 미리 정해진 it이라는 이름으로 사용할 수 있다.
    * */
    fun check(s: String, condition: (Char) -> Boolean): Boolean {
        for (c in s) {
            if (condition(c)) return false
        }
        return true
    }

    println(check("Hello") { c -> c.isLetter() }) // true
    println(check("Hello") { it.isLowerCase() }) // false

    /*
    함숫값을 만드는 다른 방법은 익명함수를 사용하는것이다.
    람다와 달리 익명 함수는 반환타입을 적을수 있으며 람다와 달리 익명함수를 인자 목록 밖으로 보낼 수 없다.
    * */

//    fun sum(numbers : IntArray) = aggregate(numbers){result, op -> result+op}

    fun sumFun(numbers: IntArray) = aggregate(numbers, fun(result, op): Int { return result + op })

    /*
    지역 함수와 마찬가지로 람다나 익명함수는 자신을 포함하는 외부에 정의된 변수에 접근할 수 있다.
    * */

    fun foreach(a: IntArray, action: (Int) -> Unit) {
        for (n in a) {
            action(n)
        }
    }

    fun out() {
        var sum = 0
        foreach(intArrayOf(1, 2, 3, 4)) {
            sum += it
        }
        println(sum) // 10
    }
    /* 5.1.4 호출 가능 참조

    앞에서 람다와 익명함수를 이용해 함수값을 만드는 방법을 살펴봤다.

    함수 값이란 ->
    함수 값(Function Value)은 변수를 함수로 사용할 수 있는 개념을 의미합니다.(함숫값을 만드는 방법은 람다,익명함수를 사용하는것이다.)
    코틀린과 같은 현대 프로그래밍 언어에서는 함수도 일종의 값으로 취급되어, 변수에 저장하거나 다른 함수의 인자로 전달할 수 있습니다.
    이러한 기능은 함수형 프로그래밍의 중요한 특징 중 하나입니다.

    하지만 이미 함수 정의가 있고, 이 함수 정의를 함숫값처럼 고차함수에 넘기고 싶다면 어떻게 해야할까?
    바로 호출 가능 참조(::)를 사용하는것이다.
    ::isCapitalLetter라는 식은 이 식이 가리키는 isCapitalLetter() 함수와 같은 동작을 하는 함숫값을 표현해준다.
    * */

    fun isCapitalLetter(c: Char) = c.isUpperCase() && c.isLetter()

    println(check("Hello") { c -> isCapitalLetter(c) })
    println(check("Hello", ::isCapitalLetter))

    /* 5.1.5 인라인 함수와 프로퍼티
    고차 함수와 함숫값을 사용하면 함수가 객체로 표현되기 때문에 성능 차원에서 부가 비용이 발생한다.
    더 나아가 익명 함수나 람다가 외부 영역의 변수를 참조하면 고차 함수에 함숫값을 넘길 때마다 이런 외부 영역의 변수를 포획할 수 있는 구조도 만들어서 넘겨야 한다.
    코틀린은 함숫값을 사용할 때 발생하는 런타임 비용을 줄일 수 있는 해법을 제공한다.

    기본적인 아이디어는 함숫값을 사용하는 고차 함수를 호출하는 부분을 해당 함수의 본문으로 대체하는 인라인 기법을 쓰는 것이다.

     inline fun indexOf(numbers: IntArray, condition: (Int) -> Boolean): Int {
        for (i in numbers.indices) {
            if (condition(numbers[i])) return i
        }

        return -1
    }

    fun main() {
    println(indexOf(intArrayOf(4, 3, 2, 1)) { it < 3 }) // 2
    }

    inline을 붙인 함수는 실행시에 아래와 같이 컴파일이 된다.

    fun main() {
      val numbers = intArrayOf(4, 3, 2, 1)
      var index = -1

      for (i in numbers.indices) {
        if (numbers[i] < 3) {
          index = i
          break
        }
      }

      println(index)
    }

    즉 객체를 생성해서 함수값을 처리하는것이 아니라 그냥 본문에 해당 함수를 넣어버린다.
    인라인 함수를 쓰면 컴파일된 코드의 크기가 커지지만, 지혜롭게 사용하면 성능을 크게 높일 수 있다. 특히, 대상 함수가 상대적으로 작은 경우 성능이 크게 향상된다.
    앞의 예제는 inline 변경자가 붙은 함수뿐 아니라 이 함수의 파라미터로 전달되는 함숫값도 인라인된다는 사실을 보여준다.

    Inline Function의 제약
    1. inline함수에 인자로 전달받은 함수는 다른 함수로 전달될수 없음
    inline 함수의 특징을 다시 보면 알수 있습니다. 위에 말했던것처럼 인라이닝을 통해 람다를 객체로 만들지 않고 본문에 바로 삽입시키기 때문이다. 둘다 본문에 그대로 쓰여져 나와 전달 할 수가 없다.

    2.inline함수에 인자로 전달받은 함수는 다른 함수로 참조될수 없음
    참조 -> https://munseong.dev/kotlin/inlinefunction/
    * */

    /* 5.1.6 비지역적 제어 흐름
  람다를 사용하면 return 문 등과 같이 일반적인 제어 흐름을 깨는 명령을 사용할 때 문제가 생긴다.

   return문은 디폴트로 자신을 둘러싸고 있는 fun, get, set으로 정의된 가장 안쪽 함수로부터 제어 흐름을 반환시킨다.
   따라서 아래의 예제는 실제로는 main 함수로부터 반환을 시도하는 코드가 된다. 이런 문을 비지역적 return이라고 부른다.
  * */

    fun forEach(a: IntArray, action: (Int) -> Unit) {
        for (n in a) action(n)
    }


    fun main() {
        forEach(intArrayOf(1, 2, 3, 4)) {
            // if (it < 2 || it > 3) return // error: 'return' is not allowed hereㅍ
            println(it)
        }
    }

    /*
    이런 경우를 해결하는 방법은 람다 대신 익명 함수를 사용하는 것이다.
    * */
    fun ok() {
        forEach(intArrayOf(1, 2, 3, 4), fun(it: Int) {
            if (it < 2 || it > 3) return
            println(it)
        })
    }

    /*
    람다 자체로부터 제어 흐름을 반환하고 싶다면 break나 continue에 대해 레이블을 사용했던 것처럼,
    return 문에 문맥 이름을 추가해야 한다. 일반적으로 함수 리터럴 식에 이름을 붙여서 문맥 이름을 만들 수 있다.
    아래 코드는 myFun이라는 레이블을 action 변수 초기화 앞부분에 붙인다
    * */

    val action: (Int) -> Unit = myFun@{
        if (it < 2 || it > 3) return@myFun
        println(it)
    }

    /*
    하지만 람다를 고차 함수의 인자로 넘기는 경우에는 레이블을 명시적으로 선언하지 않아도 함수 이름 문맥으로 사용할 수 있다.

    고차 함수란?
    1. 함수를 인자로 받는 함수
    2. 함수를 반환하는 함수
    * */
    forEach(intArrayOf(1, 2, 3, 4)) {
        if (it < 2 || it > 3) return@forEach
        println(it)
    }

    /* 5.2.1 확장 함수
    확장 함수는 어떤 클래스의 멤버인 것 처럼 호출할 수 있는(그러나 실제 멤버가 아닌) 함수를 뜻한다.
    이런 함수를 정의할떄 함수를 호춯라떄 사용할 수신 객체의 클래스 이름을 먼저 표시하고 . 을 추가한 다음에 함수 이름을 표시한다.
    아래 함수는 String 타입에 길이를 일정 이하로 제한하는 확장함수이다.
    * */

    fun String.truncate(maxLangth: Int) = if (length <= maxLangth) this else substring(0, maxLangth)

    println("Hello".truncate(10)) // Hello
    println("Hello".truncate(3)) // Hel

    /*
    일반 멤버와 비슷하게 확장 함수의 본문 안에서는 수신 객체에 this로 접근 할 수있다.
    본문안에서 substring을 호출할때와 마찬가지로 this 명시하지 않아도 암시적으로 this를 사용해 수신객체의 멤버나 확장 함수에 접근할 수 있다.

    주의할점은 확장 함수 자체는 수신객체에 속한 타입의 캡슐화를 깰 수 없다. 예를 들어 확장 함수는 클래스 밖에 정의된 함수이므로 수신객체가 속한
    클래스의 비공개 멤버에 접근 할 수 없다.

    하지만 클래스 안에서 확장 함수를 구현하는 경우 당현히 해당 함수는 클래스의 멤버로 해당 클래스의 캡슐화를 깨지 않고 접근 가능하다
    * */

    class Person(private val age: Int, private val name: String)

    //fun Person.showInfo() = println("$age,$name") Cannot access 'name': it is private in 'Person'

    class PersonOne(private val age: Int, private val name: String) {
        fun PersonOne.showInfo() = println("$age,$name")
    }

    /*
    클래스 멤버안의 함수와 확장 함수의 매개변수 타입과 수가 같다면 컴파일러는 클래스 안에 있는 멤버 함수를 우선적으로 선택한다.

    확장함수를 JVM에서 표현할때 String.truncate(maxLangth : Int) 의 확장함수는 아래와 같이 매개변수에 수신객체를 넣는 식으로 컴파일된다.
    public static truncate(String s,int maxLangth){
    ...

    이는 비확장 코틀린함수로 생각한다면 아래와 같다.
    fun truncate(s,String maxLangth : Int)
    }

    즉, 함장 함수는 근본적으로 (특정 클래스 타입의 객체를 첫 번째 인자로 받는)
    일반함수를 마치 클래스 멤버인것 처럼 쓸 수 있게 해주는 편의 문법일 뿐이다.

    멤버 함수나 프로퍼티와 달리 널이 될 수 있는 타입에 대해서도 확장을 정의할 수 있다는 점을 알면 좋다.
    ex)
    원래 String? 타입에서는 isEmpty() 함수를 직접 호출할 수 없습니다.
    지만 확장 함수를 사용하면 널 가능성 있는 타입에서도 해당 함수를 호출할 수 있도록 만들 수 있습니다.
    예를 들어, String? 타입에서 isEmpty() 함수를 사용하고 싶다면 확장 함수를 정의할 수 있습니다
    fun String?.isNullOrEmpty(): Boolean {
        return this == null || this.isEmpty()
    }
    * */

    /* 5.3 확장 프로퍼티
    코틀린에서는 확장 함수와 비슷하게 확장 프로퍼티를 정의할 수도 있다.
    확장 프로퍼티의 정의 방법은 프로퍼티의 이름 앞에 수신객체의 타입을 지정하면된다.

    val IntRange.leftHalf: IntRange
    get() = start..(start+endInclusive)/2

    println((1..3).leftHalf) // 1..2
    println((3..6).leftHalf) // 3..4
    */

    /*
    멤버와 확장 프로퍼티의 결정적차이는
    어떤 클래스의 인스턴스에 안정적으로 상태를 추가할 방법이 없기 떄문에 확장 프로퍼티에 뒷받침하는 필드를 쓸 수 없다는 점이다.
    이 말은 확장프로퍼티를 초기화 할 수도 없고, 접근자 안에서 field를 사용할 수도 없다는 것이다.
    그리고 lateinit으로 확장 프로퍼티를 정의할 수도 없다. lateinit은 뒷받침하는 필드에 의존하는 기능이기 떄문이다.

    확장 프로퍼티 정의에서는 항상 명시적인 게터를 정의해야한고, 가변 프로퍼티인 경우에는 세터도 명시해야한다.

    결론적으로, 확장 프로퍼티는 클래스 인스턴스의 상태를 추가하거나 저장할 방법이 없으므로, -> 클래스 안에 실제로 프로퍼티가 없기때문에
    항상 명시적인 게터와 세터를 통해 계산된 값을 반환하거나 설정해야 합니다. 이는 확장 프로퍼티의 주요 제한점이자 멤버 프로퍼티와의 큰 차이점입니다.

    val IntRange.midIndex: IntRange
    get() = lastIndex/2

    var IntRange.midValue: IntRange
    get() = this[midIndex]
    set(value){
        this[midIndex] = value
    }
    * */

    /* 5.4 동반 확장
    동반객체는 이 클래스에 내포된 객체 중에서 바깥 클래스의 이름을 통해 객체 멤버에 접근할 수 있는 특별한 객체다.
    이런 유용한 성질이 확장의 경우에도 성립한다.
    * */
    fun IntRange.Companion.singletonRange(n: Int) = n..n

    println(IntRange.singletonRange(5)) // 5..5
    println(IntRange.Companion.singletonRange(3)) // 3..3

    /*
    물론 IntRange.Companion.singletonRange(3)처럼 완전한 동반 객체 이름을 사용해 이함수를 호출할 수도 있다. 확장 프로퍼티도 마찬가지다.
     val String.Companion.HELLO: String
    get() = "Hello"
    * */

    /* 5.4.1 람다와 수신 객체 지정 함수 타입
    함수나 프로퍼티와 마찬가지로 코틀린에서는 람다와 익명 함수에 대해서도 확장 수신 객체를 활용할 수 있다.
    이런 함숫값들은 수신 객체 지정 함수 타입이라는 특별한 타입으로 표현된다.
    * */

    fun aggregateOne(numbers: IntArray, op: Int.(Int) -> Int): Int {
        var result = numbers.firstOrNull() ?: throw IllegalArgumentException("Empty array")

        for (i in 1..numbers.lastIndex) result = result.op(numbers[i])

        return result
    }

    fun sumOne(numbers: IntArray) = aggregateOne(numbers) { op ->
        this + op
    }

    /*
    파라미터 타입 목록 앞에 타입 이름과 점을 추가해서 수신 객체의 타입을 정의한다.
    Int.(Int) -> Int
    이렇게 정의된 경우 이 함수에 전달된 람다는 암시적으로 수신 객체를 가진다. this를 사용해 이 객체에 접근할 수 있다.

    수신객체란?
    수신 객체(Receiver Object)는 확장 함수나 확장 프로퍼티를 정의할 때 사용되는 개념입니다.
    확장 함수나 확장 프로퍼티를 정의할 때, 그 함수나 프로퍼티가 어떤 클래스의 멤버인 것처럼 사용할 수 있도록 해주는 것이 수신 객체입니다.

    마찬가지로 익명 함수도 확장 함수 문법을 사용할 수 있다.
    * */

    fun sumTwo(numbers: IntArray) = aggregateOne(numbers, fun Int.(op: Int) = this + op)

    /* 5.5.5 수신 객체가 있는 호출 가능 참조
    코틀린에서는 수신 객체가 있는 함숫값을 정의하는 호출 가능 참조를 만들 수 있다.
    문법적으로 이들은 바인딩된 호출 가능 참조와 비슷하지만, 수신 객체를 계산하는 식 대신 수신 객체 타입이 앞에 붙는다는 점이 다르다.
    * */

    fun Int.max(other: Int) = if (this > other) this else other

    aggregateOne(intArrayOf(1, 2, 3, 4), Int::plus) // 10 plus는 내장함수다
    aggregateOne(intArrayOf(1, 2, 3, 4), Int::max) // 4

    /* 5.5.1 영역 함수
    코틀린 표준 라이브러리에는 어떤 식을 계산한 값을 문맥 내부에서 임시로 사용할 수 있도록 해주는 몇가지 함수가 들어있다.
    때로는 지역 변수를 명시적으로 선언하지않고, 식의 값이 들어있는 암시적인 영역을 정의해서 코드를 단순화할 수 있는 경우가 있다.
    이런 함수를 보통 영역 함수라고 부른다.

    영역 함수가 기본적으로 하는 일은 여러분이 인자로 제공한 람다를 간단하게 실행해주는 것이다.
    그 차이는 다음과 같은 몇 가지 관점의 조합에 있다.
    1. 문맥식을 계산한 값을 영역 함수로 전달할 때 수신 객체로 전달하는가?(영역 함수가 확장 함수인 경우)
    일반적인 함수 인자로 전달하는가?(영역 함수가 일반 함수인 경우)
    2.영역 함수의 람다 파라미터가 수신 객체 지정 람다(확장 람다)인가? 아닌가?
    3.영역 함수가 반환하는 값이 람다의 결괏값인가, 컨텍스트 식을 계산한 값인가?

    전체적으로는 run, let, with, apply, also 라는 다섯가지 표준 영역 함수가 있다.
    모든 영역함수는 인라인 함수이기 때문에 런타임 부가 비용이 없다.

    영역함수는 조심히 사용해야하며, 남용하면 오히려 코드 가독성이 나빠지고 실수하기도 쉬워진다는 점을 명심하라.
    일반적으로 여러 영역 함수를 내포시키면 this나 it이 어떤 대상을 가리키는지 구분하기 어려워지므로 영역 함수를 여러 겹으로 내포시키지 않는것이 좋다.
    * */

    /* run과 with 함수
    run()함수는 확장 람다는 받는 함장함수이며 람다의 결과를 돌려준다.
    기본적인 사용 패턴은 객체 상태를 설정한 다음 이 객체를 대상으로 어떤 결과를 만들어내는 람다를 호출하는 것이다.
    * */

    class Address {
        var zipCode: Int = 0
        var city: String = ""
        var street: String = ""
        var house: String = ""

        fun post(message: String): Boolean {
            "Message for {$zipCode,$city,$street,$house} : $message"
            return readLine() == "OK"
        }
    }

    val isReceived = Address().run {
        // Address 인스턴스를 this로 사용할 수 있다.
        zipCode = 123456
        city = "London"
        street = "Baker Street"
        house = "22lb"
        post("Hello")
    }

    if (!isReceived) {
        println("Message is not delivered")
    }

    /*
    with 함수는 run과 비슷하다 유일한 차이는 with함수가 확장함수가 아니므로 문맥식을 with 첫번째 인자로 전달해야 한다는 점뿐이다.
    일반적으로 with를 사용하는 경우는 문맥 식의 멤버 함수와 프로퍼티에 대한 호출을 묶어 동일한 영역 내에서 실행하는 경우다.
    * */

    val message = with(Address()) {
        "Address: $city $street $house"
    }

    println(message)

    /*
    영역 함수를 사용하지 않으면 다음과 같이 코드를 작성해야 할 것이다.
    즉, 영역 함수가 없으면 변수를 추가적으로 도입하고 멤버를 호출할 때마다 추가한 변수를 매번 명시해야 했을 것이다 (여기서는 addr)
    * */
    val addr = Address()
    val messageOne = "Address ${addr.city} ${addr.street} ${addr.house}"

    /* 문맥이 없는 run
    코틀린 표준 라이브러리는 run을 오버로딩한 함수도 제공한다.
    이 함수는 문맥 식이 없고 람다의 값을 반환하기만 한다. 람다 자체에는 수신 객체도 없고 파라미터도 없다.

    주로 이 함수를 사용하는 경우는 어떤 식이 필요한 부분에서 블록을 사용하는 것이다.
    아래와 같이 어떤 인스턴스만을 위한 영역이 필요할때 사용한다.

     val addressOne = run {
        val city = readLine() ?: return
        val street = readLine() ?: return
        val house = readLine() ?: return
        Address(city,street,house)
    }

    run은 인라인 함수이므로 람다 내부에서 바깥쪽 함수의 제어를 반환시키기 위해 return을 사용해도 된다.

    만약 run 없이 블록을 사용하면 블록을 람다로 취급하기 떄문에 제대로 작동하지 않는다.

    val addressOne = {
        val city = readLine() ?: return // Error : return is not allowed
        val street = readLine() ?: return // Error : return is not allowed
        val house = readLine() ?: return // Error : return is not allowed
        Address(city,street,house)
    }
    * */


    /* let 함수
    let 함수는 run과 비슷하지만 확장 함수 타입의 람다를 받지 않고 인자가 하나뿐인 함수타입 람다를 받는다는 점이 다르다.
    따라서 문맥 식의 값은 람다의 인자로 전달된다. let의 반환값은 람다가 반환하는 값과 같다.
    외부 영역에 새로운 변수를 도입하는 일을 피하고 싶을 때 주로 이 함수를 사용한다.

    확장 함수 람다 (Receiver Type Lambda)
    val sum: Int.(Int) -> Int = { other -> this + other }
    확장 함수 람다는 특정 타입의 인스턴스를 수신 객체로 사용하여 마치 해당 타입의 멤버 함수인 것처럼 동작합니다.
    이는 수신 객체를 this 키워드를 사용해 접근할 수 있게 해줍니다.
    일반 람다 (Regular Lambda)
    val sum: (Int, Int) -> Int = { a, b -> a + b }
    일반 람다는 특정 수신 객체 없이 독립적으로 동작합니다. 이를 통해 전달된 매개변수만을 사용해 작업을 수행합니다.
    * */

    Address().let {
        // 이 안에서는 it을 통해 Address 인스턴스에 접근할 수 있음
        println(it)
        it.post("Hello")
    }

    /*
    let의 일반적인 사용법 중에는 넣이 될 수 있는값을 안전성 검사를 거쳐서 널이 될수 없는 함수에 전달하는 용법이 있다.
    * */
    val args: Array<String> = arrayOf("AA", "BB", "CC")
    val index = readLine()?.toInt()
    val arg = if (index != null) args.getOrNull(index) else null
    val argOne = index?.let { args.getOrNull(index) }

    /* apply/also 함수
    apply 함수는 확장 람다를 받는 확장 함수이며 자신의 수신객체를 반환한다.
    이 함수는 일반적으로 run과 달리 반환값을 만들어내지 않고 객체의 상태를 설정하는 경우에 사용한다.

    수신객체의 this가 생략가능한 이유에 대해서 생각해봤는데 어차피 확장함수는 해당 수신객체의 멤버인 것처럼 사용되는데
    멤버라면 같은 위치에 있다고 생각되는 함수나 프로퍼티를 호출할때 this를 굳이 붙이지 않아도 불러지는게 맞다고 생각됨
    * */

    Address().apply {
        city = "London"
        street = "Baker Street"
        house = "22lb"
    }.post("Hello")

    /*
    비슷한 함수로는 also가 있다. 이함수는 apply와 달리 인자가 하나 있는 람다를 파라미터로 받는다.
    * */

    Address().also {
        it.city = "London"
        it.street = "Baker Street"
        it.house = "22lb"
    }.post("Hello")

    /* 5.5.2 클래스 멤버인 확장
    앞 절에서는 확장함수를 클래스 멤버로 선언할 수 있다고 설명했다. 이런 확장을 더 자세히 살펴보자
    클래스 안에서 확장 함수나 프로퍼티를 선언하면 일반적인 멤버나 최상위 확장과 달리 이런 함수나 프로퍼티에는 수신객체가 두개 있다.
    이때 확장 정의의 수신 객체 타입의 인스턴스를 확장 수체라 부르고, 확장이 포함괸 클래스 타입의 인스턴스를 디스패치 수신 객체라 부른다.
    두 수신객체를 가리킬 떄는 this 앞에 클래스 이름이나 확장이름을 붙여서 한정시킨다.
    * */


}


