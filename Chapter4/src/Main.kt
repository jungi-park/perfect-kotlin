import java.io.File
import java.net.ProtocolFamily
import java.util.*
import javax.management.Descriptor
import kotlin.reflect.KProperty

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

    /* 4.2.3 널 아님 단언 연산자
    !! 연산자는 널 아님 단언 이라고도 부르는데 KotlinNullPointException 예외를 발생 시킬 수 있는 연산자이다.
    이 연산자가 붙은 식의 타입은 원래 타입의 널이 될 수 없는 버전이다.
    !! 연산자가 붙은 식은 null을 참조할때 예외를 던지는 동작을 부활시킨다.

    일반적으로 널 아님 단언문 사용은 사용하지 않는 것이 좋다.
    * */
    val n = readLine()!!.toInt()

    /* 4.2.4 안전한 호출 연산자
    널이 될수 있는 값은 널이 될수 없는 값의 메서드를 사용할수없다. 하지만 안전 호출 연산을 사용하면 이런 제약을 피할 수 있다.
    자바스크립트에서 제공하는 null처리 연산자처럼 왼쪽이 null이면 null을 반환하고 아니라면 오른쪽으로 진행된다.
    * */

    fun readInt() = readLine()?.toInt()

    /*
    안전한 호출 연산자가 널을 반환 할 수 있기 떄문에 이런 연산이 반환하는 값의 타입은 null을 고려해야한다.
    * */
    fun testrr() {
        val n = readInt()
        if (n != null) {
            println(n + 1)
        } else {
            println("no value")
        }
    }

    /* 4.2.5 엘비스 연산자
    널이 될 수 있는 값을 다룰 때 매우 유용한 연산자로 널 복합 연산자인 ?: 을 들 수 있다.
    이 연산자를 사용하면 널을 대신할 디폴트 값을 지정할 수 있다. 엘비스 프레슬리를 닮았기 떄문에 엘비스 연산자라고 부른다.
    * */

    val i = readLine()?.toInt() ?: 0

    /* 4.3.1 최상위 프로퍼티
    클래스나 함수와 마찬가지로 최상위 수준에 프로퍼티를 정의할 수도 있다.
    이런 경우 프로퍼티는 전역 변수나 상수와 비슷한 역할을 한다.
    * */

    /*
    // util.kt
    val prefix = "Hello"

    // main.kt

    import util.prefix

    fun main(){
    val name = prefix
    }
    * */

    /* 4.3.2 늦은 초기화
    클레스를 인스턴스화 할 때 프로퍼티를 초기화해야 한다는 요구 사항이 불필요하게 엄격할때가 있다.
    예를 들어 단위 테스트를 준비하는 코드나 의존 관계 주입에 의해 대입돼야 하는 프로퍼티가 이런 종류에 속한다.
    이런 경우 생성자에서는 초기화되지 않은 상태라는 사실을 의미하는 디폴트값을 대입하고 실제 값을 필요할 때 대입할 수도 있다.
    예를 들어 다음 코드를 생각해보자
    * */

    class Content {
        var text: String? = null

        fun loadFile(file: File) {
            text = file.readText()
        }
    }

    fun getContentSize(content: Content) = content.text?.length ?: 0

    /*
    위와 같은 예제의 단점은 실제 값이 항상 사용전에 초기화되므로 절대 널이 될 수 없는 값이라는 사실을 알고 있음에도 늘 널 가능성을 처리해야한다는 점이다.
    코틀린은 이런 패턴을 지원하는 lateinit 키워드를 제공한다.
    lateinit 키워드가 붙은 프로퍼티를 읽으려 시도할때 프로그램이 프로퍼티가 초기화됐는지 검사해서 초기화 되지 않은 경우 UninitializedPropertyAccessException 예외를
    던진다는 점을 제외하면 일반 프로퍼티와 똑같다.
    프로퍼티를 lateinit로 만들기 위해서는 몇가지 조건이 있다.
    1. 프로퍼티가 코드에서 변경될 수 있는 지점이 여러곳이므로 var로 지정해야한다.
    2. 프로퍼티 타입은 null이 아닌 타입이어야 하고 원시값이 아니어야한다. lateinit 프로퍼티는 내부적으로 초기화되지 않은 상태를 표현하기 위해 null을 사용하는 널이 될 수있는 값으로 표현되기 떄문이다.
    3. lateinit 프로퍼티를 정의하면서 초기화 식을 지정해 값을 바로 대입할 수 없다. 이런 대입을 허용하면 애초에 lateinit을 지정하는 의미가 없기 때문이다.
    * */

    class ContentLate {
        lateinit var text: String

        fun loadFile(file: File) {
            text = file.readText()
        }
    }

    fun getContentSizeLate(content: ContentLate) = content.text.length


    /* 4.3.3 커스텀 접근자 사용하기
    커스텀 접근자는 프로퍼티 값을 읽거나 쓸때 호출되는 특별한 함수다.
    게터는 정의 끝에 붙으며 기본적으로 이름 대신 get이라는 키워드가 붙은 함수처럼 보인다.
    하지만 이런 프로퍼티를 읽으면 프로그램이 자동으로 게터를 호출한다.

    추가적으로 fullName 값을 초기화 해주지 않았는데 그 이유는 뒷받침하는 필드가 없기 때문인데
    즉, fullName이 어떠한 값을 가지고 있는 것이아니라 매번 해당 값을 읽을때 firstName + familyName 값을 계산해서 반환하고 있기 떄문이다.
    이와 계산에 의해서 값을 돌려주는 프로퍼티의 경우 계산 프로퍼티라고 말하며 뒷받침 필드가 필요하지 않다.

    뒷받침하는 필드 즉, 여기서는   val name =  firstName + familyName 가 뒷받침 필드를 가지고 있으며
    커스텀 getter와 setter를 정의할 때는 field 키워드를 사용하여 이 뒷받침하는 필드에 접근할 수 있습니다.
    * */

    class Personfour(val firstName: String, val familyName: String) {
        val fullName: String
            get() = firstName + familyName
        val name = firstName + familyName
            get() {
                println(field)
                return field
            }
    }

    /*
    게터뿐만 아니라 setter도 존재한다.
    일반적으로 파라미터이름은 value로 하며 타입 추측에 의해서 반환 타입을 적지 않아도 된다.

    프로퍼티 접근자에 별도로 가시성 변경자를 붙일 수도 있다.
    바깥에서 볼때는 lastChange 값을 변경하지 못하고 불변인것으로 보이지만 아래와 같이 사용할 수 있다.
    lateinit 프로퍼티의 경우 할상 자동으로 접근자가 생성되기 때문에 커스텀 접근자를 정의할 수 없다
    주생성자에 들어있는 매개변수의 커스텀 접근자를 생성하고 싶은경우 val,var 키워드를 제거하여 자동으로 생성자를 만들지 말고
    프로퍼티에 직접 만들고 값을 넣어 사용해야한다.
    * */
    class Personfive(name:String) {
        var lastChange: Date? = null
            private set

        var name: String = name
            set(value) {
                lastChange = Date()
                field = value
            }
    }
    /* 4.3.4 지연 계산 프로퍼티와 위임
    lateinit 변경자를 사용해 지연 초기화를 구현하는 방법을 살펴봤다.
    하지만 어떤 프로퍼티를 처음 읽을 때까지 그값에 대한 계산을 미뤄두고 싶을 때가 자주 있다.
    코틀린에서는 lazy 프로퍼티를 통해 이를 달성할 수 있다.

    아래의 코드는 commend 값이 print data가 되는것이 아니면 text는 값을 계산하지않는다.
    계산된 이후에는 text 프로퍼티 값에 저장되고 이후에는 저장된 값을 읽게 된다.

    위임객체는 by 다음에 위치하며 다양한 위임객체가 있다. 지금 현재 위임 프로퍼티에 대해서는 스마트 캐스트를 사용할 수 없다.
    위임은 구현이 다 다를 수 있기 때문에 커스텀 접근자로 정의된 프로퍼티처럼 다뤄진다.
    그리고 이말은 위임을 사용한 지역변수의 경우에도 스마트 캐스트를 쓸 수 없다는 의미이다.
    * */

    val text by lazy {
        File("data.txt").readText()
    }

    fun textText(){
        while (true){
            when (val commend = readLine() ?: return){
                "print data" -> println(text)
                "exit" -> return
            }
        }
    }

    /*
    코틀린에서 스마트 캐스트를 사용할 수 없는 이유는 주로 위임 프로퍼티(delegate property)의 특성과 관련이 있습니다.
    위임 프로퍼티는 프로퍼티의 값 읽기와 쓰기 동작을 위임 객체(delegate object)에 맡기기 때문에
    컴파일러가 해당 프로퍼티의 실제 타입을 추론하고 보장하는 것이 어렵습니다.
    구체적인 이유는 다음과 같습니다:
    동적 동작: 위임 객체는 프로퍼티의 값을 동적으로 결정할 수 있습니다. 즉, 프로퍼티의 값을 읽을 때마다 다른 값을 반환할 수 있습니다. 이러한 동적 동작은 컴파일 시점에 프로퍼티의 타입을 고정하기 어렵게 만듭니다.
    프로퍼티의 변경 가능성: 위임 객체는 언제든지 프로퍼티의 값을 변경할 수 있습니다. 예를 들어, 다음과 같은 위임 객체가 있다고 가정해 봅시다:
    * */
    class Delegate {
        private var value: Any = "Initial Value"

        operator fun getValue(thisRef: Any?, property: KProperty<*>): Any {
            return value
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
            this.value = value
        }
    }

    class Example {
        var prop: Any by Delegate()
    }

    /* 4.4.1 객체 선언
    코틀린은 어떤 클래스에 인스턴스가 오직 하나만 존재하게 보장하는 싱글턴 패턴을 내장하고 있다.
    코틀린에서는 object라는 키워드를 통해 싱글턴을 선언한다.

    object Application{
        val name = "My Application"
     }

    이런 객체 선언은 클래스를 정의하는 통시에 클래스의 인스턴스를 정의하는 것이기도하다.
    예를 들어 다음코드와 같다

    fun description(app:Application) = app.name // Application은 타입임
    println(Application) // Application은 값임

    클래스와 마찬가지로 객체선언 멤버함수와 프로퍼티가 포함될 수 있고, 초기화 블록도 포함할 수 있다.
    하지만 객체에는 주생성자나 부생성자가 없다 -> 객체 인스턴스는 암묵적으로 만들어지기 때문에 생성자 호출이 의미가 없다.

    객체의 본문에 들어 있는 class는 inner를 붙일 수 없다.
    내부클래스는 항상 바깥 클래스와 연관 되어지는데 -> outter().inner()
    객체선언은 항상 인스턴스가 하나이므로 inner변경자가 불필요해진다. 그래서 객체의 본문에 들어 있는 class는 inner를 금지한다.

   자바에서는 유틸리티 클래스를 사용하는 경우가 있다. 유틸리티 클래스는 기본적으로 아무 인스턴스를 가지지 않고.(private 생성자)
   정적메서드만을 모아두고 사용한다.
   코틀린에서는 이럴 필요 없이 최상위선언(파일에 클래스와 함수를 만드는것)을 패키지 안에 모아 둘 수 있으므로 불필요하게 유틸리티 클래스를 선언할 필요가없다.

   코틀린의 파일이란
    물리적 단위: 코틀린 파일은 실제로 코드가 저장되는 물리적 파일입니다. 예를 들어, Utils.kt라는 파일이 있을 수 있습니다.
    최상위 선언: 코틀린 파일 내에서는 클래스, 함수, 프로퍼티 등을 최상위 수준에서 선언할 수 있습니다. 즉, 특정 클래스나 객체 내부에 속하지 않고 파일 자체에 속해 있습니다.
    패키지: 코틀린 파일은 특정 패키지에 속할 수 있습니다. 파일의 상단에 package 선언을 사용하여 패키지를 지정할 수 있습니다.
    * */

    /* 4.4.2 동반 객체
    내포된 클래스와 마찬가지로 내포 객체도 인스턴스가 생기면 자신을 둘러싼 클래스인 비공개 멤버에 접근할 수있다.
    이런 특성은 예를 들어 팩토리 디자인 패턴을 쉽게 구현하는 경우 유용하게 활용할 수 있다.
    생성자를 직접 사용하고 싶지 않을때가 있다. 예를 들어 생성자를 사용하면 어떤 사전 검사 결과에 따라 널을 반환하거나
    (같은 상위 타입에 속하는) 다른 타입의 객체를 반환할 수 없다. 생성자는 항상 자신이 정의퇸 클래스의 객체를 반환하거나 예외를 던질 수만 있기
    때문이다.

    이를 해결하는 방법은 생성자를 비공개로 지정해 클래스 외부에서 사용할 수 없게 한 다음, 내포된 객체에 팯토리 메서드 역할을 수행하는
    함수를 정의하고 그 함수 안에서 필요에 따라 객체의 생성자를 호출하는 것이다.

     class APP private constructor(val name:String){
        object Factory(){
            fun create(arg:Array<String>):APP?{
                val name = arg.firstOrNull() ?: return null
                return APP(name)
            }
        }
    }

    fun main(arg:Array<String>){
        val app = APP.Factory.create(arg) ?: return
        println(App Start ${app.name})
    }

    이런 경우 별도로 import APP.Factory.create 로 팩토리 메서드를 임포트 하지 않는 한 매번 내포된 객체의 이름을 지정해야한다.
    코틀린에서는 Factory 메서드를 동반객체로 정의함으로써 이런 문제를 해결할 수 있다.
    동반 객체는 companion 이라는 키워드를 덧붙인 내포객체이다.
    동반객체는 내포객체와 동일하지만 한가지 예외가 있다. 동반객체의 멤버에 접근할 떄는 동반객체의 이름을 사용하지 않고 동반 객체가 들어있는
    외부클래스의 이름을 사용할 수 있다. 위 코드를 아래와 같이 간결하게 수정할 수 있다.


     class APP private constructor(val name:String){
        companion object Factory(){
            fun create(arg:Array<String>):APP?{
                val name = arg.firstOrNull() ?: return null
                return APP(name)
            }
        }
    }

    fun main(arg:Array<String>){
        val app = APP.create(arg) ?: return
        println(App Start ${app.name})
    }

    동반객체의 경우 정의에서 아예 이름을 생략할 수 있다.

     class APP private constructor(val name:String){
        companion object{
            fun create(arg:Array<String>):APP?{
                val name = arg.firstOrNull() ?: return null
                return APP(name)
            }
        }
    }

    동반객체의 이름을 생략한 경우 동반 객체의 디폴트 이름은 Companion으로 가정한다.
    import APP.Companion.create

    companion 변경자는 최상위 객체 앞이나 다른객체 안에 내포된 객체 안에 붙이는것이 금지된다.
    최상위 객체에 붙이는 것은 동반객체를 연결할 클래스 정의가 없고
    객체안에 객체의 경우 companion를 붙이는 것은 불필요한 중복이기 때문이다.

    코틀린의 동반 객체를 자바의 정적 문맥과 대응하는 것 처럼 생각할 수도 있다.
    자바 정적멤버와 마찬가지로 동반 객체의 멤버도 외부 클래스와 똑같은 전역 상태를 공유하며 외부 클래스의 모든 멤버에 멤버 가시성과 무관하게 접근할 수 있다.
    하지만 중요한 차이는 코틀린 동반객체는 인스턴스라는 점이다. 이로인해 자바의 정적 멤버보다 코틀린 동반 객체가 더 유연하다
    -> 타입상속이 가능하고 일반 객체처럼 여기저기에 전달될 수 있기 때문이다.
    또한 동반 객체 안에서 init블럭을 사용할 수 있다는 것도 알아두자
    * */





}

