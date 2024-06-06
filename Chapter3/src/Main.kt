import kotlin.math.PI

fun circleArea(radius: Double): Double {
    return PI * radius * radius
}

fun main() {
    // 3.1 함수
    print("Enter radius: ")
    val radius = readLine()!!.toDouble()
    println("Circle area: ${circleArea(radius)}")

    /*
    함수를 만들때 파라미터가 없더라도 무조건 ()가 필요하다
    fun circleArea(){}

    자바에서는 파라미터의 값이 가변이라 함수내부에서 변경하게 하지 못하게 하려면 final을 지정해 불변 값으로 바꿔야 하는데
    코틀린 함수는 무조건 불변이다. 즉, 함수 본문에서 파라미터 값을 변경하면 컴파일 오류가 발생한다.
    fun increment(n:Int):Int{
        return n++ //Error: can't change immutable variable
    }

    그리고 파라미터 앞에는 val,var을 표시할 수 없다. 이렇게 강제하는 이유는 파라미터를 대입하는 중에 실수할 가능성이
    높을 뿐 아니라 불변 값으로 강제하는 것이 더 깔끔하고 이해하기 편한 코드를 만들어내기 떄문이다.
    * */

    /*
    함수의 파라미터는 변수와 달리 항상 타입을 지정해야하며 리턴의 타입도 지정해주어야한다.
    컴파일러는 함수의 파라미터의 타입을 추론하지 못하기 때문에 파라미터에 타입을 지정해주어야하며
    반환값의 타입 또한 함수내부의 모든 return(반환지점)을 확인하기 여렵기 떄문에 필요하다.
    함수의 반환타입은 일종의 문서화 역활을 한다.
    circleArea(radius: Double): Double
    * */

    /*
    코틀린은 값에 의한 호출 의미론을 사용한다.
    파라미터의 값은 호출한 쪽에 값을 복사해서 사용한다는 의미이다.
    호출 인자로 전달한 변수를 변경해도 함수 내부의 파라미터 값은 영향이 없다는 의미이다.
    하지만 파라미터가 참조 값일 경우 참조 주소를 복사해서 사용 하기 때문에 참조 주소는 변하지 않지만
    참조 주소가 가르키는 값은 변할 수 있다.
    fun increment(a:IntArray):Int{
        return ++a[0]
    }

    fun main(){
        val a = intArrayOf(1,2,3)
        println(increment(a)) // 2
        println(a) // [2,2,3]
    }
    * */

    /*
    경우에 따라 반환타입을 생략할 수 있는 예외적인 두가지 경우가 있다.
    1. 유닛(unit)타입을 반환하는 경우 -> unit은 자바의 void에 해당하는 코틀린 타입 즉, 의미있는 반환값을 돌려주지 않는다. 는 의미
    아래의 두코드는 같다.
    fun prompt(n:Int){
        println($Int)
    }

    fun prompt(n:Int):Unit{
        println($Int)
    }

    2. 식이 본문인 경우
    fun fun circleArea(radius: Double) = PI * radius * radius // 반환값이 Double로 추론됨
    * */


    /* 3.1.2 위치기반 인자와 이름 붙은 인자
    자바나 다른 언어는 위치 기반 인자를 널리 쓰고 있다. 반면 코틀린은 이름 붙은 인자라고 불리는 방식도 제공한다.
    * */
    fun rectangleArea(width: Double, height: Double): Double {
        return width * height
    }
    // 아래 방식 모두 사용가능
    rectangleArea(10.toDouble(), 10.toDouble())
    rectangleArea(width = 10.toDouble(), height = 10.toDouble())
    rectangleArea(height = 10.toDouble(), width = 10.toDouble())


    /* 3.1.3 오버로딩과 디폴트 값
    자바 메서드와 마찬가지로 코틀린 함수도 오버로딩 할 수 있다.
    반환값만 다른 경우 컴파일 오류가 밣생한다.
    fun plus(a:String,b:String) = a+b
    fun plus(a:String,b:String) = a.toInt()+b.toInt() // error: conflicting overloads : (생략)

    실제 호출할 함수를 결정할때 컴파일러는 자바 오버로딩 해소규칙과 비슷한 다음 규칙을 따른다.
    1. 파라미터의 개수와 타입을 기준으로 호출 할 수 있는 모든 함수를 찾는다.
    2. 덜구체적인 함수를 제외시킨다. -> 함수의 파라미터 타입이 상위 타입인 경우 ,이경우는 다른 함수보다 덜 구체적인 함수다.
    3. 후보가 하나로 압축되면 호출하고 둘 이상이면 컴파일 오류가 발생
    fun mul(a:Int, b:Int) = a*b // 1
    fun mul(a:Int, b:Int, c:Int) = a*b*c // 2
    fun mul(s:String, b:Int) = s.repeat(n) // 3
    fun mul(o:Any, n:Int) = Array(n){o} // 4

    mal(1,2) 호출시 Int가 Any의 하위 타입이므로 1과 4중 1을 선택
    mal(1,2L) // error: none of the following functions can be called with the arguments supplied
              // 해당 파라미터 타입에 맞는 함수가 없음
    mal(1L,2) // Long,Int 타입을 받을 수 있는 함수는 4번 뿐이므로 4를 선택
    mal("0",3) // String 이 Any의 하위 타입이기 때문에 3과 4중 3을 선택

    코틀린은 아래와 같이 함수를 정의할 수 있다. 함수를 통해 새로운 함수 생성
    fun readInt() = readInt(10)

    코틀린에서는 경우에 따라 함수 오버로딩을 쓰지 않아도 된다. 더 우아한 해법인 디폴트 파라미터가 있기 떄문이다.

    fun readInt(radix:Int = 10) = readLine!!.toInt(radix)

    파라미터가 여러게 있는 함수의 경우 디폴트 값이 존재하는 파라미터를 뒤쪽에 배치하는 것이 더 좋은 코딩스타일이다.
    디폴트 값이 있으면 인자 개수가 가변적이어서 오버로드 해소가 복잡해진다.
    fun mul(a:Int, b:Int = 1) = a*b // 1
    fun mul(a:Int, b:Long = 1L) = a*b // 2
    fun mul(a:Int, b:Int, c:Int = 1) = a*b*c // 3

    mul(10) // 오류: 1과 2중 무엇을 호출해야할지 결정할 수 없음
    mul(10,20) // 인자가 더 적기 때문에 1과 3중에 1을 결정
    mul(10,20,30) // 적용가능한 함수가 3번이므로 3을 선택

    인자가 두개인 mul(10,20)을 호출할 경우 3번이 덜 구체적인 함수다. 3번의 함수는 세번쨰 파라미터로 디폹트 값이 있는
    c를 추가해서 인자가 두개인 함수를 확장한 것이기 때문이다.

    만약 1번 함수를 아래와 같이 바꾸면
    fun mul(a:Number, b:Int = 1) = a*b
    Number는 Int의 상위타입이므로 1번 함수가 3번 함수보다 덜 구체적인 함수로 간주돼 3번 함수가 호출된다.

    구체적 함수 중요도 -> 갯수 > 타입 > 확장(디폴트)
    * */

    /* 3.1.4 vararg
    앞에서 인자의 개수가 정해지지 않은 arrayOf() 같은 함수를 봤다. 우리가 직접 작성한 함수에서도 이런 기능을 쓸 수 있다.
    필요한 과정은 파라미터 정의앞에 vararg를 붙이는것 뿐이다.
    함수내부에서는 파라미터를 적절한 배열타입으로 사용할 수 있다.
    아래의 함수의 items는 내부에서 IntArray 이다.
    * */
    fun printSorted(vararg items: Int) {
        items.sort()
        println(items.contentToString())
    }
    printSorted(6, 2, 10, 1)

    /*
    또한 스프레드 연산자인 *을 사용하면 배열을 가변 인자 대신 넘길 수 있다.
    스프레드 문법을 사용하면 배열을 복사해서 사용하는 것이므로 원본 데이터를 바꾸지 않는다.
    * */
    val numbers = intArrayOf(6, 2, 10, 1)
    printSorted(*numbers)

    /*
    둘이상의 vararg 파라미터를 선언하는 것은 금지된다.
    다만 여러인자와 스프레드를 섞어서 호출하는 것은 가능하다.
    * */
    printSorted(1, 2, *numbers)


    // 가변인자에 이름있는 인자로 전달 할 수는 없으나 배열을 전달하는 것은 가능하다.
    // printSorted(items = 1,2,3)
    printSorted(items = intArrayOf(3, 10, 7))

    /*
    디폴트값이 있는 파라미터와 vararg를 같이 쓰는 것은 조금 신경을 써야한다.
    아래코드의 1번의 경우 가변인자로 주고싶었던 6이라는 인트값이 첫번쨰 위치값인 prefix값으로 들어가 오류가 난다.
    이것을 피하기 위해서는 이름있는 매개변수를 사용하면된다.
    * */
    fun printSorted1(prefix: String = "", vararg items: Int) {}
    // printSorted1(6,2,3,4,5) // 1
    printSorted1(items = intArrayOf(6, 2, 3, 4, 5)) //2

    // 가변인자가 앞에 있는 경우 디폴트 값이 있는 파라미터를 호출하고 싶을 경우 이름붙은 인자로 호출해야 한다.
    fun printSorted2(vararg items: Int, prefix: String = "") {}
    printSorted2(6,2,3,4,5)
    printSorted2(6,2,3,4,5, prefix = "!")

    /* 3.1.5 함수의 영역과 가시성
    코클린 함수는 정의된 위치에 따라 세 가지로 구분할 수 있다.
    1. 파일에 직접 선언된 최상위함수
    2. 어떤 타입 내부에 선언된 멤버 함수
    3. 다은 함수 안에 선언된 지역 함수

    여기서는 1.3번만 다루고 2번은 4장 클래스와 객체 다루기에서 설명
    * */
    // util.kt에서 불러온 readInt() 함수
    print(readInt()+readInt())

    /* 경우에 따라 함수정의 앞에 private, internal 이라는 키워드를 붙일 수 있다. 이런 키워드를 가시성 변경자라 한다.
    private로 정의하면 함수가 정의된 파일 안에서만 해당 함수를 볼 수 있다.
    internal로 정의하면 모듈내부에서만 볼 수 있다.
    public이라는 변경자도 있지만 최상위 함수는 디폴트로 public가 적용된다.
    코틀린의 모듈이란 기본적으로 함께 컴파일되는 파일 전부를 의미한다.
    */

    /*
    지역 함수는 함수안에 함수로써 블록안에서만 접근가능하다.
    fun main(){
        fun readInt() = readLine()!!.toInt()
        readInt()
    }

    fun readIntPair = intArray(readInt(),readInt()) // 오류 발생

    또한 지역 함수는 자신을 둘러싼 함수, 블록에 선언된 변수나 함수에 접근가능하다.
    주의 할 점으로 지역함수에는 가시성 변경자를 붙일 수 없다.
    * */

    /* 3.2.1 패키지와 임포트
    자바와 마찬가지로 코틀린 파일에서도 맨앞에 패키지 이름을 지정하면 파일에 있는 모든 최상위선언을 지정한 패키지 내부에 넣을 수 있다.
    디폴트 최상위 패키지는 이름이 없다. -> 패키지를 지정하지 않으면 컴파일러는 파일이 최상위 패키지에 있다고 가정한다.

    패키지 디렉티브는 package 키워드로 시작하고 . 으로 구별된 식별자들로 이뤄진 패키지 전체 이름이 뒤에 온다.
    예를 들어 다음파일은
    package foo.bar.util
    fun readInt(radix: Int = 10) = readLine()!!.toInt(radix)

    util이라는 패키지에 속하고, util은 다시 bar라는 패키지에 속하며, bar는 다시 foo라는 패키지에 속하고, foo패키지는 루트패키지에 속한다.

    위의 readInt 함수가 같은 패키지에 속한다면 따로 import가 필요하지 않으나 다른 패키지라면
    foo.bar.util.readInt(8) 이와 같이 사용 할 수 있지만 너무 길어지기 때문에

    import foo.bar.util.readInt 로 불러와
    readInt(8) 로 사용하는 것이 보편적이다.

    !!! 패키지 계층구조는 소스파일에 있는 패키지 디렉티브로부터 구성된 별도의 구조라는 점에 유의하라. 소스 파일 트리와 패키지 계층 구조가 일치 할 수 있지만 꼭 그럴 필요는 없다.
    !!! 예를 들어 소스파일은 모두 한 디렉터리 아래 있지만 각각이 서로 다른 패키지에 포함 될 수도 있고, 한 패키지에 포함된 소스 파일들이 모두 서로 다른 디렉터리에 들어갈 수도 있다.
    !!! 하지만 프로젝트의 여러부분을 이동할때 좀 더 편하기 때문에 디렉터리와 패키지 구조를 동일하게 하는편을 권장한다.
    * */

    /* 3.2.2 임포트 디렉티브 사용하기
    지금까지 예제에서 봤던 가장 단순한 임포트 형태는 전체 이름을 지정해 어떤 선언을 임포트하는 것이다.
    import java.lang.math // JDK 클래스
    import foo.bar.util.readInt // 최상위 함수

    임포트 디렉티브가 위와같이 클래스나 함수등 최상위 선언만 임포트할 수 있는 것은 아니다.
    클래스 안에 클래스나 enum 상수 등도 임포트할 수 있다.
    import kotlin.Int.Companion.MIN_VALUE
    fun fromMin(step: Int) = MIN_VALUE + step

    만약 readInt() 함수가 foo 패키지와 bar패키지 양쪽에 존재할때 해당 함수를 사용하기 위해 as(alias)를 사용할 수 있다.
    import foo.readInt as fooReadInt
    import bar.readInt as barReadInt

    패키지 내의 모든 기능을 임포트하고 싶다면 * 와일드카드를 사용하면된다.
    import kotlin.math.*

    이런 형태의 필요시 임포트 기능은 구체적인 선언을 지정하는 임포트보다 우선 순위가 낮다. readInt()함수가 bar와 foo 패키지 양쪽에 있을떄 아래의 경우
    import foo.readInt
    import bar.*

    readInt()를 호출하게되면 foo패키지의 readInt를 사용하게 된다.
    * */


}