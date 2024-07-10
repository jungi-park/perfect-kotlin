fun main() {
    /* 8.1.1 하위 클래스 선언
    코틀린에서 상속을 위해 open 키워드를 사용해야한다.
    이 변경자는 상속에 대해 열려있다는 뜻이다. 즉, 해당 클래스를 상위 클래스로 지정할 수 있다는 말이다.

    상속이 제공하는 강력한 기능은 임의 다형성이다.
    open이 붙은 start 함수는 override 할 수 있지만 붙지 않은 stop 함수는 override 할 수없다.
    * */

    open class Vehicle{
        open fun start(){
            println("start")
        }
        fun stop(){
            println("stop")
        }
    }

    class car(): Vehicle() {
        override fun start(){}

    }

    /* 8.1.3 타입 검사와 캐스팅
    코틀린은 타입검사에 사용 할 수 있는 is 연산자를 제공한다.
    is 연산자킄 지난 값은 스마트 캐스팅 된다.
    * */

    val objects = arrayOf(1,2,3,"4",5)

    for(i in objects){
        println(i is Int)
    }

    /* 8.1.4 공통 메서드
    코틀린 Any의 equals() 메서드는 기본적으로 참조 동등성을 비교한다.
    내용 동등성을 비교하려면 equals()를 오버라이드 해야한다. ==,!= 연산자는 equals() 메서드를 사용한다.
    equals 메서드를 오버라이드한 경우 참조 동등성 비교는 === 연산자를 통해서 한다.
    * */

    class Person(val name: String, val age: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Person) return false
            return name == other.name && age == other.age
        }

        override fun hashCode(): Int {
            return name.hashCode() * 31 + age
        }
    }

}