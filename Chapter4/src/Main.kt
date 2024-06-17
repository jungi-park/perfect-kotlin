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

    val personSeven = PersonSeven("John","Doe")
    // println(personSeven.firstName) error : Cannot access 'firstName': it is private in 'PersonSeven
    println(personSeven.fullName())

    /*
    함수와 프로퍼티, 주생성자, 부생성자에 대해 가시성 변경자를 지원한다.
    주생성자 가시성을 지정하려면 constructor 키워드를 꼭 명시해야한다.
    * */
    class Empty private constructor() {
        fun showMe()= println("Empty")
    }
    // Empty().showMe() error : Cannot access '<init>': it is private in 'Empty'
}