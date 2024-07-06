import java.net.ProtocolFamily
import kotlin.random.Random

fun main() {
    /* 6.1 이넘 클래스
    이넘 클래스는 미리 정의된 상수들로 이뤄진 제한된 집합을 표현하는 특별한 클래스다.
    이넘은 상수를 정의한다는 점에서 객체 정의와도 약간 비슷하다. 객체와 마찬가지로 전역 상수로 사용할 수 없는 위치에서는 이넘을 정의할 수 없다.
    예를 들어 이넘을 내부클래스나 함수 본문에 정의할 수 없다.
    * */
    fun WeekDay.isWorking() = this == WeekDay.SATURDAY || this == WeekDay.SUNDAY

    println(WeekDay.MONDAY.isWorking()) // false
    println(WeekDay.SATURDAY.isWorking()) // true

    /* 6.1.1 빠뜨린 부분이 없는 when 식
    이넘을 통해서 when을 사용하면 장점이 있다. 바로 when에서 모든 이넘 상수를 다룬 경우에는 else 가지를 생략해도 된다.
    빠뜨린 부분이 없는 형태의 when식을 사용하면, 새 이넘 값을 추가하는 경우에 꺠질 수 있는 코드가 생기는 일을 방지할 수 있다.
    else가지 가 있다면 새로운 이넘 값이 생길때 else문으로 넘어가 예상치 못한 오류를 만날 수 있지만
    빠뜨린 부분이 없는 when을 사용하면 컴파일러가 경고하기 때문에 이런 오류를 쉽게 방지할 수 있다.
    * */
    fun findWeek(day: WeekDay) = when (day) {
        WeekDay.MONDAY -> "월요일"
        WeekDay.TUESDAY -> "화요일"
        WeekDay.WEDNESDAY -> "수요일"
        WeekDay.THURSDAY -> "목요일"
        WeekDay.FRIDAY -> "금요일"
        WeekDay.SATURDAY -> "토요일"
        WeekDay.SUNDAY -> "일요일"
    }

    /* 6.1.2 커스텀 멤버가 있는 이넘 정의하기
    다른 클래스와 마찬가지로 이넘 클래스도 멤버를 포함할 수 있다. 그 외에도 원한다면, 이넘에도 확장 함수나 프로퍼티를 붙일 수 있다.
    이넘 클래스도 일반 클래스에 허용되는 정의도 포함할 수 있다.(함수,프로퍼티,주생성자와 부생성자, 초기화 블록, 내부클래스,/내포된 클래스,객체 등)
    이넘 클래스에 정의된 이런 요소들은 반드시 이넘 상수 목록 뒤에 와야 한다. 이런 경우에는 상수목록과 다른 부분을 구분하기위해 상수목록을 세미콜론으로
    끝내야 한다.
    * */

    println(WeekDay.MONDAY.isWorking()) // false
    println(WeekDay.WEDNESDAY.lowerCaseName) // wednesday

    /*
    이넘 클래스에 생성자가 있으면 각 이넘 상수의 정의 뒤에도 적절한 생성자 호출을 추가해야 한다.
    * */

    println(RainbowColor.BLUE.isCold) // true
    println(RainbowColor.RED.isCold) // true

    /* 6.1.3 이넘 클래스의 공통 멤버 사용하기
    코틀린의 모든 이넘 클래스는 암시적으로 kotlin.Enum 클래스의 하위 타입이다.
    kotlin.Enum 클래스는 몇가지 공통 함수와 프로퍼티를 제공한다.
    모든 이넘 값에는 ordinal과 name이라는 한 쌍의 프로퍼티가 들어있다. ordinal은 이넘 클래스 안에 정의된 이넘 값의 순서에 따른 인덱스고
    name은 이넘 값의 이름이다.
    * */
    println(Direction.WEST.name) // WEST
    println(Direction.WEST.ordinal) // 2

    /*
    각 이넘 클래스는 마치 동반 객체의 멤버처럼 호출할 수 있는 암시적인 메서드들을 제공한다.
    valueOf() 메서드는 이넘 값의 이름은 문자열로 넘기면 그에 해당하는 이넘 값을 돌려주거나 이름이 잘못된 경우 예외를 던진다.

    value() 메서드는 정의된 순서대로 모든 이넘 값이 들어있는 배열을 돌려준다.
    이 메서드를 호출 할 때마다 배열이 새로 생긴다는 점에 유의하라. 따라서 이 배열을 바꾸더라도 나머지 배열(원본포함)에는 영향이 없다.

    * 확장 함수는 최상위 수준이나 클래스 내부에서만 선언할 수 있습니다
    확장 함수는 컴파일 시점에 정적으로 바인딩되는데 해당 코드를 함수 안에 넣어 놓면 컴파일러가 해당 확장 함수를 찾지 못하기 때문이다.
    정적 바인딩은 런타임에 추가적인 함수 호출 결정 과정을 줄여주기 때문에 성능이 더 효율적입니다.

    정적 바인딩 (Static Binding)
    정적 바인딩은 컴파일 시점에 어떤 메서드가 호출될지 결정되는 방식입니다.
    컴파일러가 메서드 호출을 미리 결정하여 실행 시간 동안 추가적인 결정 과정을 거치지 않습니다.
    예를 들어, Java에서 오버로딩된 메서드는 정적 바인딩의 예입니다.

    동적 바인딩 (Dynamic Binding)
    동적 바인딩은 런타임 시점에 어떤 메서드가 호출될지 결정되는 방식입니다.
    주로 다형성(polymorphism)과 관련이 있으며, 런타임 동안 객체의 실제 타입에 따라 호출되는 메서드가 결정됩니다.
    * */
    println(Direction.valueOf("NORTH")) // NORTH

//    val weekDays = Direction.values();
//
//    val WeekDay.nextDay  get() = weekDays[(ordinal+1)/weekDays.size]

    /* 6.2 데이터 클래스
    코틀린은 데이터를 저장하기 위한 목적으로 주로 쓰이는 클래스를 선언하는 유용한 기능을 제공한다. 이런 기능을 데이터 클래스라고 부르며
    이 기능을 사용하면 컴파일러가 동등성을 비교하거나 String으로 변환하는 등의 기본 연산에 대한 구현을 자동으로 생성해준다.
    그리고 구조 분해 선언을 활용할 수도 있다. 구조 분해를 사용하면 클래스의 프로퍼티를 간단한 한 가지 언어 구성 요소를 사용해 여러 변수에
    나눠 넣을 수 있다.
    * */

    /* 6.2.1 데이터 클래스와 데이터 클래스에 대한 연산
    클래스에 커스텀 동등성을 비교해야하는 경우 보통 equals() 메서드와 이와 연관된 hashCode() 메서드를 구현한다.
    데이터 클래스는 프로퍼티 목록을 기반으로 이런 메서드를 자동으로 생성해준다.
    * */

    class Person(val firstName: String, val familyName: String, val age: Int)

    val person1 = Person("John", "Doe", 25)
    val person2 = Person("John", "Doe", 25)
    val person3 = person1

    println(person1 == person2) // false 주소가 다름
    println(person1 == person3) // true 주소가 같음

    /*
    이제 컴파일러가 주생성자에 정의된 프로퍼티의 값을 서로 비교하는 동등성 비교 연산을 자동으로 생성해주기 떄문에 두 비교 모두 true를 반환한다.
    * */

    data class PersonData(val firstName: String, val familyName: String, val age: Int)

    val person4 = PersonData("John", "Doe", 25)
    val person5 = PersonData("John", "Doe", 25)
    val person6 = person4

    println(person4 == person5) // true
    println(person4 == person6) // true

    /*
    String, PersonData, Mailbox는 모두 내용을 바탕으로 동등성을 구현하기 때문에
    Mailbox 인스턴스 비교는 address 프로퍼티 비교와 person에 들어있는 PersonData 클래스의 동등성 비교에 따라 결정된다.
    하지만 데이터 클래스가 아니면, Person 프로퍼티 비교가 객체의 정체성에 따라 결정되기 때문에 결과가 바뀐다.
    * */
    data class Mailbox(val address: String, val person: PersonData)

    val box1 = Mailbox("Unknown", PersonData("John", "Doe", 25))
    val box2 = Mailbox("Unknown", PersonData("John", "Doe", 25))

    println(box1 == box2) // true

    data class MailboxOne(val address: String, val person: Person)

    val box3 = MailboxOne("Unknown", Person("John", "Doe", 25))
    val box4 = MailboxOne("Unknown", Person("John", "Doe", 25))

    println(box3 == box4) // false: 두 Person 인스턴스의 정체성이 다름

    /*
    주 생성자의 파라미터에서 선언한 프로퍼티만 equals() / hashCode() / toString() 메서드 구현에 쓰인다.
    다른 프로퍼티들은 이런 함수들의 생성에 영향을 못미친다.
    모든 데이터 클래스는 copy() 함수를 제공한다. 이 함수를 사용하면 인스턴스를 복사하면서 몇몇 프로퍼티를 변경할 수 있다.
    copy()는 불변 데이터 구조를 더 쉽게 사용하도록 해준다.
    * */

    /*
    코틀린은 두 가지 범용 데이터 클래스가 있다. 두 값(한 쌍: pair)이나 세 값(트리플렛: triple)을 저장할 수 있는 데이터 클래스이다.
    대부분의 경우 이 같은 범용 데이터 클래스보다는 커스텀 클래스를 사용하는 편이 더 낫다.
    커스텀 클래스를 정의하면 클래스의 프로퍼티에 의미가 있는 이름을 부여할 수 있어서 코드 가독성을 향상시킬 수 있다.
    * */

    val pair = Pair(1, "two")

    println(pair.first + 1)  // 2
    println("${pair.second}!") // two!

    val triple = Triple("one", 2, false)

    println("${triple.first}!") // one!
    println(triple.second - 1)  // 1
    println(!triple.third)      // true

    /* 6.2.2 구조 분해 선언
    아래 코드는 PersonData 인스턴스의 각 프로퍼티를 추출한 다음 계산에 황용하고 있다.
    하지만 PersonData가 데이터 클래스이므로 이를 각각의 프로퍼티에 대응하는 지역 변수를 정의하는 좀 더 간결한 구문으로 대신할 수 있다.
    * */

    fun newPerson() = PersonData(readLine()!!, readLine()!!, Random.nextInt())

    val new = newPerson()
    val firstName = new.firstName
    val familyNmae = new.familyName
    val age = new.age
    if (age < 18) {
        println("$firstName $familyNmae is under-age")
    }


    /*
    아래와 같은 선언을 구조 분해 선언이라고 하며,
    변수 이름을 하나만 사용하는 대신 괄호로 감싼 식별자 목록으로 여러 변수를 한꺼번에 정의할 수 있게 해주는 일반화된 지역 변수 선언 구문이다.
    여기서 각 변수에 어떤 프로퍼티가 매핑되는지 데이터 클래스의 생성자에 있는 각 프로퍼티의 위치에 따라 결정되며, 선언하는 변수의 이름에 의해
    결정되지 않는다는 점에 유의하라
    * */

    val (first, family, ages) = PersonData("John", "Doe", 25)


    /*
    구조 분해 선언 전체는 타입이 없다. 하지만 필요하면 구조 분해를 이루는 각 컴포넌트 변수에 타입을 표기할 수는 있다.
    * */
    val (firstone, familyone: String, agess) = PersonData("John", "Doe", 25)

    /*
    구조 분해 선언에 데이터 클래스의 프로퍼티 수보다 적은 수의 변수가 들어갈 수도 있다.
    이런 경우 생성자의 뒷부분에 선언된 프로퍼티는 추출되지 않는다.
    * */

    val (firsttwo, familytwo) = PersonData("John", "Doe", 25)
    println("$firsttwo $familytwo") // John Doe

    /*
    시작 부분이나 중간 부분에서 몇 가지 프로퍼티를 생략해야 한다면 어떻게 해야 할까?
    람다의 사용하지 않는 파라미터와 비슷하게 구조 분해에서 사용하지 않는 부분을 _로 대신할 수 있다.
    * */

    val (_, fam) = PersonData("John", "Doe", 25)
    println("$fam") // Doe

    /*
    val을 var로 바꾸면 변경할 수 있는 변수들을 얻을 수 있다.
    val/var 지정은 모든 변수를 가변 변수로 정의하거나 모든 변수를 불변 변수로 정의해야만 하며, 둘을 섞어서 정의할 수는 없다.
    * */

    /*
    구조 분해 할당은 for루프에서도 사용할 수 있다
    * */

    val pairs = arrayOf(Pair(1, 2), Pair(1, 2), Pair(1, 2), Pair(1, 2))

    for ((one, two) in pairs) {
    }

    /*
    데이터 클래스는 선언하기만 하면 자동으로 구조 분해를 지원하지만, 일반적으로 아무 코틀린 타입이나 구조 분해를 사용할 수 있게 구현할 수 있다.
    * */

    /* 6.3 인라인 클래스(값 클래스)
    실무에서는 래퍼 클래스를 만드는 일이 꽤 흔하다. 무엇보다 래퍼 클래스는 잘 알려진 어댑터 디자인 패턴의 핵심이기도 하다.
    예를 들어 프로그램에서 돈이라는 개념을 처리하고 싶다고 하자. 돈의 수량은 근본적으로 수에 불과하지만,
    의미가 다른 돈을 구분없이 섞어서 사용하고 싶지는 않다. 이럴때 여러 통화(돈)를 서로 다른 클래스로 정의할 수 있다면
    타입시스템의 도움을 받아 오류를 줄일 수 있다. 따라서 래퍼 클래스와 유틸리티 함수를 도입한다.
    * */

    class Dollar(val amount: Int) // amount의 단위는 센트
    class Euro(val amount: Int) // amount의 단위는 센트
    // fun Dollar.toEuro() = ...
    // fun Euro.toDollar = ...

    /*
    이 접근 방법의 문제점은 런타임 부가 비용이 든다는 것이다.
    새로운 종류의 통화 수량을 만들 때마다 래퍼클래스를 생성해야 하므로 이런 부가비용이 생긴다.
    방금 본 예에서처럼 감싸야 하는 대상이 원시 타입의 값이면 부가 비용 문제가 더 커진다.
    수 값을 직접 다룰 때는 전혀 객체를 생성할 필요가 없기 떄문이다. 원시 타입 대신 래퍼 클래스를 사용하면 프로그램 성능이라는 대가를 지불해야한다.
    이런 문제를 해결하기위해 코틀린에서는 인라인 클래스라는 새로운 종류의 클래스를 도입했다. 일반적인 원시 타입의 값과 마찬가지로 부가 비용 없이
    쓸 수 있기 때문에 인라인 클래스를 값 클래스라고 부르기도 한다.
    * */
}

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    val lowerCaseName get() = name.lowercase()
    fun isWorkDay() = this == SATURDAY || this == SUNDAY
}

val weekDays = Direction.values();

val WeekDay.nextDay get() = weekDays[(ordinal + 1) / weekDays.size]

enum class RainbowColor(val isCold: Boolean) {
    RED(false), ORANGE(false), YELLOW(false), GREEN(true), BLUE(true), INDIGO(true), VIOLET(true);

    val isWarm get() = !isCold
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST;
}
