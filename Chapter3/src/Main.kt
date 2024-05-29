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
    fun rectangleArea(width:Double, height:Double):Double{
        return width * height
    }
    // 아래 방식 모두 사용가능
    rectangleArea(10.toDouble(),10.toDouble())
    rectangleArea(width = 10.toDouble(), height = 10.toDouble())
    rectangleArea(height = 10.toDouble(), width = 10.toDouble())
}
