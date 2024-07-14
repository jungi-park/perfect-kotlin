fun main() {

    /* 9.1.1 제네릭 선언
    선언을 사용할 때는 파라미터 실제 타입을 지정해줘야한다.
    인자 타입을 추론할 수 있으면 생략할 수 있다.
    * */
    val map = HashMap<Int, String>()
    val list = arrayListOf<String>()

    val mapo: Map<Int, String> = HashMap() // 타입을 명시했기 때문에 추론 가능
    val listo = arrayListOf("abc", "def")  // 전달된 인자 타입으로 추론 가능


    /*
    다음은 주어진 타입의 값을 저장할 수 있는 트리를 표현하는 클래스이다.
    예제처럼 클래스의 타입 인자를 T로 받아 클래스 안에서 변수나 프로퍼티, 함수 타입으로 쓸 수 있다.
    * */
    class TreeNode<T>(val data: T) {
        private val _children = arrayListOf<TreeNode<T>>()
        var parent: TreeNode<T>? = null
        val children: List<TreeNode<T>> get() = _children

        fun addChild(data: T) = TreeNode(data).also {
            _children += it
            it.parent = this
        }

        override fun toString() = _children.joinToString(prefix = "$data{", postfix = "}")
    }

    val root = TreeNode<String>("Hello").apply {
        addChild("World")
        addChild("!!!")
    }

    val root2 = TreeNode(123).apply { // Int 타입 추론 가능
        addChild(456)
        addChild(789)
    }
    println(root)  // Hello{World{}, !!!{}}
    println(root2) // 123{456{}, 789{}}

    /*
    클래스 멤버 또는 객체가 아닌 확장 프로퍼티나 확장 함수를 제네릭으로 만들 수 있다. 제네릭 클래스와 달리 타입 파라미터를 fun / val / var 뒤에 위치시킨다.
    확장함수 제네릭은 해당 클래스가 제네릭을 사용하는 경우에 주로 사용된다.
    * */

    fun <T> TreeNode<T>.addChildren(vararg data: T) {
        data.forEach { addChild(it) }
    }

    fun <T> TreeNode<T>.walkDepthFirst(action: (T) -> Unit) {
        children.forEach { it.walkDepthFirst(action) }
        action(data)
    }

    val rootOne = TreeNode("Hello").apply {
        addChildren<String>("World", "!!!")
    }

    /* 9.1.2 바운드와 제약
    트리 원소가 Number 타입 혹은 그 하위 타입의 값으로 한정할 수 있는 상위 바운드로 선언할 수 있다.
    상위 바운드는 Any?가 디폴트이며, 컴파일러는 타입 인자의 타입이 상위 바운드의 하위 타입인지 검사한다.
    * */
    fun <T : Number> TreeNode<T>.average(): Double { // String 트리에 호출하면 컴파일 오류가 발생한다.
        var count = 0
        var sum = 0.0
        walkDepthFirst { // 깊이 우선으로 노드를 방문하면서 함수 수행
            count++
            sum += it.toDouble()
        }
        return sum / count
    }

    /*
    바운드는 흔하게 널이 아닌 타입으로 제한하는 경우에 사용한다. 이 경우 상위 바운드로 널이 될 수 없는 타입을 지정해야 한다.
    * */
    fun <T : Any> notNullTree(data: T) = TreeNode(data)

    /*
    타입 파라미터 구문을 사용하면 타입 파라미터에 제약을 가할 수 있다. where 절을 클래스 선언 본문 앞에 추가하고 바운드할 타입 목록을 표시한다.
    T : Named, T : Identified -> T 타입에 들어올수 있는 제약사항은 해당 T 타입이 Named,Identified 인테페이스 둘다 구현한 타입일 경우만 가능
    * */
    class Registry<T> where T : Named, T : Identified {
        val items = ArrayList<T>()
    }

}


interface Named {
    val name: String
}

interface Identified {
    val id: Int
}