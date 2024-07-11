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

    /* 8.2.4 위임
    기존 클래스를 확장하거나 변경해야 하는데, 이클래스를 상속 할 수 없다면 어떻게 해야할까.
    이런 경우 기존 클래스를 재사용하는 잘 알려진 패턴인 위임 패턴을 사용할 수 있다.
    인터페이스의 구현을 만들고 싶다면 이미 있는 구현의 인스턴스를 가져와서 우리가 만드는 클래스로 감싸고, 필요할 떄 인터페이스 메서드 구현을 이 기존 구현 인스턴스에 위임할 수 있다.

    상속을 사용할 수 없는 상황

    1.클래스가 final로 선언된 경우:
    자바에서는 final 키워드를 사용하여 클래스를 상속할 수 없도록 만들 수 있습니다. 예를 들어, java.lang.String 클래스는 final로 선언되어 있어 이를 상속할 수 없습니다.

    2.라이브러리 클래스 변경이 불가능한 경우:
    외부 라이브러리나 프레임워크에서 제공하는 클래스를 변경할 수 없는 경우가 있습니다. 이러한 클래스는 상속을 통해 확장할 수 없을 수 있습니다.

    3.상속이 설계 원칙에 맞지 않는 경우:
    상속은 강한 결합을 초래할 수 있으며, 잘못 사용하면 객체 지향 설계 원칙을 위반할 수 있습니다. 예를 들어, 상속을 통해 불필요한 메서드나 필드를 상속받게 되어 클래스가 불필요하게 복잡해질 수 있습니다.

    4,다중 상속이 필요한 경우:
    자바는 클래스의 다중 상속을 지원하지 않기 때문에, 여러 클래스의 기능을 조합하려면 상속만으로는 불가능합니다.
    * */

    open class Persons(
        override val name: String,
        override val age: Int
    ) : PersonData


    class Alias(
        private val realIdentity: PersonData,
        private val newIdentity: PersonData
    ) : PersonData by newIdentity {
        override val age: Int
            get() = realIdentity.age
        // newIdentity를 위임받아 this로 받을 수 있다.
        override fun toString() = "${this.name} ${this.age}"
    }

    val valWatts = Persons("Val Watts", 30)
    val johnDoe = Alias(valWatts, Persons("John Doe", 25))
    println(johnDoe.age) // 30
    println(johnDoe) // John Doe 25


}

interface PersonData {
    val name: String
    val age: Int
}