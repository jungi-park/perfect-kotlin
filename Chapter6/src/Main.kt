import java.net.ProtocolFamily

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

    class Person(val firstName:String,val familyName:String,val age:Int)

    val person1 = Person("John","Doe",25)
    val person2 = Person("John","Doe",25)
    val person3 = person1

    println(person1 == person2) // false 주소가 다름
    println(person1 == person3) // true 주소가 같음

    /*
    이제 컴파일러가 주생성자에 정의된 프로퍼티의 값을 서로 비교하는 동등성 비교 연산을 자동으로 생성해주기 떄문에 두 비교 모두 true를 반환한다.
    * */

    data class PersonData(val firstName:String,val familyName:String,val age:Int)

    val person4 = PersonData("John","Doe",25)
    val person5 = PersonData("John","Doe",25)
    val person6 = person4

    println(person4 == person5) // true
    println(person4 == person6) // true
}

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    val lowerCaseName get() = name.lowercase()
    fun isWorkDay() = this == SATURDAY || this == SUNDAY
}

val weekDays = Direction.values();

val WeekDay.nextDay  get() = weekDays[(ordinal+1)/weekDays.size]

enum class RainbowColor(val isCold: Boolean) {
    RED(false), ORANGE(false), YELLOW(false), GREEN(true), BLUE(true), INDIGO(true), VIOLET(true);

    val isWarm get() = !isCold
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST;
}
