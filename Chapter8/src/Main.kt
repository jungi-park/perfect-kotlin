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


}