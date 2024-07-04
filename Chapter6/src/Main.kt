import WeekDay.*

fun main() {
    /* 6.1 이넘 클래스
    이넘 클래스는 미리 정의된 상수들로 이뤄진 제한된 집합을 표현하는 특별한 클래스다.
    이넘은 상수를 정의한다는 점에서 객체 정의와도 약간 비슷하다. 객체와 마찬가지로 전역 상수로 사용할 수 없는 위치에서는 이넘을 정의할 수 없다.
    예를 들어 이넘을 내부클래스나 함수 본문에 정의할 수 없다.
    * */
    fun WeekDay.isWorking() = this == SATURDAY || this == SUNDAY

    println(MONDAY.isWorking()) // false
    println(SATURDAY.isWorking()) // true

    /* 6.1.1 빠뜨린 부분이 없는 when 식
    이넘을 통해서 when을 사용하면 장점이 있다. 바로 when에서 모든 이넘 상수를 다룬 경우에는 else 가지를 생략해도 된다.
    빠뜨린 부분이 없는 형태의 when식을 사용하면, 새 이넘 값을 추가하는 경우에 꺠질 수 있는 코드가 생기는 일을 방지할 수 있다.
    else가지 가 있다면 새로운 이넘 값이 생길때 else문으로 넘어가 예상치 못한 오류를 만날 수 있지만
    빠뜨린 부분이 없는 when을 사용하면 컴파일러가 경고하기 때문에 이런 오류를 쉽게 방지할 수 있다.
    * */
    fun findWeek(day: WeekDay) = when (day) {
        MONDAY -> "월요일"
        TUESDAY -> "화요일"
        WEDNESDAY -> "수요일"
        THURSDAY -> "목요일"
        FRIDAY -> "금요일"
        SATURDAY -> "토요일"
        SUNDAY -> "일요일"
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

}

enum class WeekDay {
    MONDAY{fun startWork() = println("일시작") }, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    val lowerCaseName get() = name.lowercase()
    fun isWorkDay() = this == SATURDAY || this == SUNDAY
}

enum class RainbowColor(val isCold: Boolean) {
    RED(false), ORANGE(false), YELLOW(false), GREEN(true), BLUE(true), INDIGO(true), VIOLET(true);

    val isWarm get() = !isCold
}
