import java.net.ProtocolFamily
import javax.management.Descriptor

fun main() {
    /* 4.1.1 클래스 내부 구조
    어떤 사람에 대한 정보를 저장하는 클래스를 정의해보자.
    firstName,familyName,age 라는 프로퍼티와 fullName,showMe 라는 함수로 구성되어있다.
    * */
    class Person {
        var firstName: String = ""
        var familyName: String = ""
        var age: Int = 0

        fun fullName() = "$firstName $familyName"

        fun showMe() {
            println("${fullName()} : $age")
        }
    }

    /*
    여러가지 프로퍼티 유형중 가장 단순한것은 그냥 특정 틀래스와 연돤된 변수다. 자바의 클래스 필드와 비슷하게 생각될 수도 있으며, 더 일반적으로는 프로퍼티에 어떤 계산이 포함될 수 있다.
    이럴 경우에는 클래스 인스턴스 내부에 저장되는 대신 그때그때 계산되거나 지연 계산되거나 맵에서 값을 얻어오는 등의 방식으로 프로퍼티의 값을 제공할 수 있다.
    모든 프로퍼티에서 일반적으로 쓸 수있는 기능에는 다음과 같이 마치 변수처럼 프로퍼티를 제공하는 참조 구문이 있다.
    * */
    fun showAge(p: Person) = println(p.age)
    fun readAge(p: Person) {
        p.age = readLine()!!.toInt()
    }

    /*
    기본적으로 코틀린 클래스는 public이다. 최상위 함수와 마찬가지로 internal,private 설정이 가능하다.
    * */

    /* 4.1.2 생성자
    코틀린에서는 클래스 헤더의 파라미터 목록을 주생성자 선언이라고 부른다.
    초기화시 필요한 로직을 수행하기 위해 init이라는 키워드가 붙은 초기화 블록을 제공한다.
    클래스 안에 init블록이 여럿 들어갈 수 있다.
    초기화(init) 블록안에는 return문이 들어가지 못한다는 점을 유의해야한다.

    init블록을 통해 프로퍼티의 값을 초기화 할때 복잡한 로직이 필요한 경우 init블록 안에서 초기화 하는 것도 허용한다.
    * */
    class PersonOne(firstName: String, familyName: String) {
        val fullName = "$firstName $familyName"

        init {
            println("Create new Person instance: $fullName")
        }

        init {
            println("Two: $fullName")
        }
    }

    /*
    컴파일러는 모든 프로퍼티가 확실히 초기화 되는지 확인한다.
    * */
    class PersonTwo(fullName: String) {
        val firstName: String
        val familyName: String

        init {
            val names = fullName.split(" ")
            if (names.size != 2) {
                throw IllegalArgumentException("Invalud name : $fullName")
            }
            // 아래 코드 주석시 error : Property must be initialized or be abstract
            firstName = names[0]
            familyName = names[1]
        }
    }

    /*
    주생성자 파라미터는 프로퍼티에 초기화시 그리고 init 블록 안에서만 사용가능하다. (1)
    이에대한 해법은 생성자 파라미터값을 저장할 멤버 프로퍼티를 정의하는것이다. (2)
    * */
    class PersonThree(firstName: String, familyName: String) {
        val fullName = "$firstName $familyName"
        val firstName = firstName // (2)
        fun printFirstName() {
//              println(firstName) error: Unresolved reference: firstName (1)
        }
    }

    /*
    코틀린은 간단하게 생성자 파라미터를 멤버 프로퍼티로 만드는 방법을 제공한다.
    생성자 파라미터 앞에 val이나 var 키워드를 덧붙이면 자동으로 해당 생성자 파라미터로 초기화되는(생성자 파라미터와 이름이 같은) 프로퍼티를 정의한다.
    이때 firstName을 init이나 프로퍼티 초기화에 사용하는 경우 생성자 파라미터의 값을 호출하며, 다른위치에서 참조하면 프로퍼티에 값을 호출한다.
    * */
    class PersonFour(val firstName: String, val familyName: String) {
        val fullName = "$firstName $familyName"
        fun printFirstName() {
            println(firstName)
        }
    }

    /*
    여러 생성자를 통해 인스턴스를 다른 방법으로 초기화 하소 싶을 경우 디폴트 파라미터를 사용하는 주생성자로 해결 할 수 있으나.
    경우에 따라 주생성자만으로는 충분하지 않을 수도 있다. 코틀린에서는 이런 문제를 부생성자를 사용해 해결할 수 있다.
    부생성자 문법은 함수 이름 대신에 constructor 키워드를 사용한다.
    * */
    class PersonFive {
        val firstName: String
        val familyName: String

        constructor(firstName: String, familyName: String) {
            this.firstName = firstName
            this.familyName = familyName
        }

        constructor(fullName: String) {
            val names = fullName.split(" ")
            if (names.size != 2) {
                throw IllegalArgumentException("Invalud name : $fullName")
            }

            firstName = names[0]
            familyName = names[1]
        }
    }

    /*
    부생성자가 생성자 위임 호출을 사용해 다른 부생성자를 호출 할 수있다.
    생성자 파라미터 뒤에 콜론(:)을 넣고 함수이름 대신 this를 사용하면 생성자 위임 호출이 된다.
    부생성자 파라미터에는 val , var을 쓸 수 없다는 점을 주의하자.
    * */

    class PersonSix {
        val fullName: String

        constructor(firstName: String, familyName: String) : this("$firstName, familyName")


        constructor(fullName: String) {
            this.fullName = fullName
        }
    }

    /* 4.1.3 멤버 가시성
    가시성은 클래스 멤버마다 다르게 지정할 수 있다.
    가시성을 사용해 구현과 관련한 세부사항을 캡슐화함으로써 외부 코드로부터 구현 세부사항을 격리시킬 수 있으므로, 가시성 지정은 클래스 정의 시 아주 중요한 부분이다.
    public : 디폴트 가시성으로 멤버를 어디서나 볼 수 있다.
    internal : 멤버를 멤버가 속한 클래스가 포함된 컴파일 모듈 내부에서만 볼 수 있다.
    protected : 멤버를 멤버가 속한 클래스와 멤버가 속한 클래스의 모든 하위 클래스 안에서 볼 수 있다.
    private : 멤버를 멤버가 속한 클래스 내부에서만 볼 수 있다.
    * */
    class PersonSeven(private val firstName: String, private val familyName: String) {
        fun fullName() = "$firstName $familyName"
    }

    val personSeven = PersonSeven("John", "Doe")
    // println(personSeven.firstName) error : Cannot access 'firstName': it is private in 'PersonSeven
    println(personSeven.fullName())

    /*
    함수와 프로퍼티, 주생성자, 부생성자에 대해 가시성 변경자를 지원한다.
    주생성자 가시성을 지정하려면 constructor 키워드를 꼭 명시해야한다.
    * */
    class Empty private constructor() {
        fun showMe() = println("Empty")
    }
    // Empty().showMe() error : Cannot access '<init>': it is private in 'Empty'

    /* 4.1.4 내포된 클래스 -> 내포된 클래스와 내부클래스는 다르다 inner 키워드가 들어가면 내부클래스
    1.함수, 프로퍼티, 생성자 외에 코틀린 클래스는 다른 클래스도 멤버로 가질 수 있다. 이런 클래스를 내포된 클래스라고 부른다.
    2.자바와 달리, 바깥쪽 클래스는 자신에게 내포된 클래스의 비공개 멤버에 접근할 수 없다.
    3.내포된 클래스에 inner를 붙이면 자신을 둘러싼 외부클래스의 현재 인스턴스에 접근 할 수 있다. -> inner class Id로 변경시 오류수정
    즉, 내부 클래스는 외부클래스에 접근가능
    * */

//    class outerPerson(val id: Id, val age: Int) {
//        class Id(private val firstName: String, val familyName: String){
//            private fun InnerShowMe() = showMe() // (3) error : Unresolved reference: showMe
//        }
//        // fun showMe() = println(id.firstName) error : Cannot access 'firstName': it is private in 'Id' (2)
//        private fun showMe() = println("외부클래스 private 함수")
//    }

    /*
    내부 클래스를 생성하기 위해서는 외부클래스의 인스턴스를 생성하고
    외부클래스 인스턴스에서 내부클래스 생성자를 통해 초기화한다.

    내부 클래스 생성 -> Person().Id()
    내포된 클래스 생성 -> Person.Id()

    코틀린과 자바의 내포된 클래스는 매우 비슷하다. 주된 차이는 코틀린 내부클래스 앞에 inner 변경자가 붙는다는 점이다.
    자바클래스는 디폴트로 내부클래스이며 static을 붙여야 내부 클래스와 외부 클래스가 연관되지 않는다. (내포된 클래스)
    반면 코틀린은 inner가 없는 클래스는 내포된 클래스이며 외부 클래스 인스턴스와 연관되지 않는다.
    * */

    /* 4.1.5 지역 클래스
    자바처럼 코틀린에서도 함수 본문에서 클래스를 정의할 수 있다. 이런 지역클래스는 자신을 둘러싼 코드 블록 안에서만 쓰일 수 있다.
    * */

    fun test() {
        class Point(val x: Int, val y: Int) {
            fun shift(dx: Int, dy: Int): Point = Point(x + dx, y + dy)
            override fun toString(): String = "($x, $y)"
        }

        val p = Point(10, 10)
        println(p.shift(-1, 3)) // (9,13)
    }

    fun foo() {
        // println(Point(0,0)) error : Unresolved reference: Point
    }

    /*
    지역 함수와 비슷하게 코틀린 지역클래스도 자신을 둘러싼 코드의 선언에 접근할 수 있다.
    지역 클래스는 클래스 본문 안에서 자신이 접근할 수 있는 값을 포획하고 변경 할 수 있다.
    코틀린과 달리 자바에서는 포획한 변수의 값을 변경 할 수 없다.
    * */

    fun testOne() {
        var x = 1

        class Counter {
            fun increment() {
                x++
            }
        }
        Counter().increment()
        println(x) // 2
    }

    /* 4.2.1 널이 될 수 있는 타입
    코틀린 타입 시스템의 중요한 특징은 널 값을 포함하는 타입과 그렇지 않은 타입을 구분하는 능력이다.
    자바에서 모든 참조 타입은 널이 될 수 있는 타입으로 간주된다. 즉, 컴파일러는 어떤 참조 타입의 변수가 null이 아닌 값만 포함 한다는 사실을 보장하지 못한다.
    하지만 코틀린에서 기본적으로 모든 참조 타입은 널이 될 수 없는 타입이다

    아래의 isLetterString 함수의 매개변수는 String인 참조값을 매개변수로 받지만 null은 들어갈 수 없다.
    * */
    fun isLetterString(s: String): String {
        return s
    }

    println(isLetterString("ABC"))
    // println(isLetterString(null)) error : Null can not be a value of a non-null type

    /*
    코틀린에서 널이 될 수도 있는 값을 받는 함수를 작성하려면 어떻게 해야 할까?
    이런 경우 파라미터 타입뒤에 ? 를 붙여서 타입을 널이 될 수 있는 타입으로 지정해야한다.
    코틀린에서 String? 같은 타입은 널이 될 수 있는 타입이라고 불린다.
    타입시스템에서 널이 될 수 있는 타입은 원래타입(?가 붙지않은 타입)의 상위 타입이다. 원래타입 + null -> 널이 될 수 있는 타입
    이말은 널이 될 수 있는 타입 자리에 널이 될 수 없는 값이 들어 갈 수 있지만
    널이 될 수 없는 타입의 자리에 널이 될 수 있는 타입이 들어갈 수 는 없다.
    * */

    /*
    또한 원시 값은 널이 될 수 없는데 Int? 와 같이 사용한다면 당연히 박싱한 값을 표현한다.

    중요 -> 널이 될수 있는 타입은 원래 타입에 들어있는 어떤 프로퍼티나 메서드도 제공하지 않는다.
    허나 코틀린의 확장 메커니즘을 활용해 자체적인 메서드와 프로퍼티를 제공한다.
    * */

    /* 4.2.2 널 가능성과 스마트 캐스트
    널이 될 수 있는 값을 처리하는 가장 직접적인 방법은 해당 값을 조건문을 사용해 null과 비교하는 것이다.
    s타입 자체를 바꾸지는 않았지만 null에 대한 검사를 추가하면 코드가 어떤 이유에서인지 컴파일된다. 스마트 캐스트라고 불리는 코틀린 기능이 이런일을 가능하게 해준다.
    * */
    fun isLetterStr(s: String?): Boolean {
        if (s == null) return false
        // kotlin.String(으)로 스마트 형 변환
        if (s.isEmpty()) return false

        for (c in s) {
            if (!c.isLetter()) return false
        }
        return true
    }

    // 스마트캐스트는 when이나 루프같은 조건 검사가 들어가는 다른 문이나 식 안에서도 작동한다.
    fun descreibeNumber(n: Int?) = when (n) {
        null -> null
        // 밑에 가지에서는 null이 될 수 없다.
        in 0..10 -> "small"
        in 11..100 -> "large"
        else -> "out of range"
    }

    // || 이나 && 연산의 오른쪽에서도 같은 일이 벌어진다.
    fun isSingleChar(s: String?) = s != null && s.length == 1

    /*
    불변 지역 변수는 초기화후 변경이 없으므로 항상 스마트 캐스트를 사용할 수 있다.
    하지만 널 검사와 사용 지점 사이에서 값이 변경되는 경우 스마트 캐스트가 작동하지 않는다.
    * */

    var s = readLine() // String?
    if (s !== null) {
        // 변수 값이 바뀌므로 스마트 캐스트를 쓸 수 없음
        s = readLine()
        // println(s.length) error : Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type String
    }
}

