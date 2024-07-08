import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {

    /* 7.1 컬렉션
    컬렉션은 엘리먼트들로 이뤄진 그룹을 저장하기 위해 설계된 객체다.
    코틀린 표준 라이브러리는 훨씬 풍부한 컬렉션 기능을 제공한다.
    코틀린에서 컬렉션을 조작하는 모든 연산이 인라인 함수라는 점에 주목하자.
    따라서 이런 연산을 사용해도 함수 호출이나 람다 호출에 따른 부가 비용이 들지 않는다.
    * */

    /* 7.1.1 컬렉션 타입
    코틀린 컬렉센 타입은 기본적으로 4가지로 분류 할 수 있다.
    배열,이털러블,시퀀스,맵이다.
    배열과 비슷하게 컬렉션 타입도 제네릭 타입이다.
    예를 들어 List<String>은 문자열로 이뤄진 리스트를 뜻하고
    Set<Int>는 Int타입의 값들로 이뤄진 집합을 뜻한다.
    * */

    /* 이터러블
    이터러블은 Iterable<T> 타입으로 표현돠며, 일반적으로 즉시 계산되는 상태가있는 컬렉션을 표현한다.
    상태가 있다는 말은 컬렉션이 원소를 필요할 떄 생성하는 제너레이터 함수를 유지하지 않고 원소를 저장한다는 뜻이다.
    즉시 계산이라는 말은 나중에 어느 필요한 시점에 원소가 초기화되지 않고 컬렉션을 최초로 생성할 때 초기화된다는 뜻이다.

    이터러블 타입은 원소를 순회할 수 있는 iterator()라는 메서드를 제공한다.

    코틀린의 for 루프에서는 이 메서드를 통해 모든 이터러블 객체를 활용할 수 있다
    * */

    val list = listOf("red", "blue", "green")

    for (item in list) {
        println(item+ " ")
    }

    /*
    자바와 비교할때 코틀린 이터러블의 중요한 특징인 가변 컬렉션과 불변 컬렉션을 구분한다는 점이다.
    가변 이터러블의 기본 타입은 MutableIterable로 표현된다. 이 인터페이스 객체는 MutableIterator 인스턴스를 생성할 수 있다.

    불변 컬렉션 타입에서 유용한 특징으로는 공변성이 있다. 공변성이라는 말은 T가 U의 하위 타입인 경우 Iterable<T>도 Iterable<U>의 하위 타입이라는 뜻이다.
    Iterator, Collection,List,Set,Map 등과 같은 컬렉션 관련 타입의 경우에도 이런 공변성이 성립한다.
    * */

    fun processCollection(c:Iterable<Any>){}

    val listOne = listOf("red", "blue", "green")

    processCollection(listOne) // List<String>을 List<Any>로 전달


    /*
    하지만 가변 컬렉션의 경우 이런 코드가 작동하지 않는다. 만약 가변 컬렉션에 대해 이런 식의 코드를 작성할 수 있다면,
    정수를 문자열 리스트에 추가하는 것과 같은 일이 벌어질 수 있다.

    * */
    fun processCollectionOne(c:MutableCollection<Any>){c.add(123)}

    val listTwo = arrayListOf("a", "b", "c")

    // processCollectionOne(listTwo) // error : 타입이 일치하지 않습니다.


    /* 컬렉션, 리스트, 집합
    이터러블의 하위 분류 중에는 Collection 인터페이스로 표현되는 타입들과 Collection 타입의 하위 타입인 MutableCollection 인터페이스로 표현되는 타입들이 있다.
    Collection을 상속한 클래스는 크게 다음과 같이 분류할 수 있다.

    1.List와 MutableList로 표현되는 리스트는 인덱스를 통하여 접근이 가능하며, 순서가 있는 컬렉션이다. 리스트는 인덱스를 통한 임의 접근이 가능한 ArrayList와 원소를 빠르게 추가하거나 삭제할 수 있지만, 인덱스로 원소에 접근할 때는 O(n)의 시간이 걸리는 LinkedList가 있다.
    2.집합은 유일한 원소들로 이뤄진 컬렉션이다. 원소의 순서는 구현에 따라 다르다.
    HashSet은 해시 테이블을 기반으로 하며, 원소의 해시 코드에 따라 원소 순서가 정해진다.
    LinkedHashSet은 해시 테이블이 기반이지만, 삽입 순서를 유지하기 때문에 이터레이션하면 삽입된 순서대로 원소를 순회할 수 있다.
    TreeSet은 이진 검색 트리가 기반이머, 어떤 비교 규칙에 따라 일관성 있는 원소 순서를 제공한다.

    * */

    /* 시퀀스
    자바의 스트림과 비슷한 코틀린의 시퀀스도 iterator() 메소드를 제공한다.
    이 메서드를 통해 시퀀스의 내용을 순회할 수 있지만 이터러블과는 의도가 조금 다르다.
    Collection 에서 연산은 즉시(eager) 발생하지만, Sequence 에서 연산은 지연(lazy) 처리된다.
    즉 map, filter 등이 호출될 때, Collection 에서는 전체 원소를 순회하지만, Sequence 는 순회하지 않을 수 있다.
    이터러블과 달리 시퀀스는 내부적이므로 외부에서 직접 사용할 수 없다.
    * */

    /* 맵
    맵은 키(key) 와 값(value) 쌍으로 이뤄진 집합이다.
    맵 자체는 Collection의 하위 타입이  아니지만, 맵의 원소들은 컬렉션처럼 사용할 수 있다.
    키-값 쌍은 Map.Entry 와 MutableMap.MutableEntry 인터페이스로 표현된다.
    맵은 두 가지 종류의 원소가 들어있기 때문에 맵의 타입은 두 가지 타입을 파라미터로 받는 제너릭 타입( Map<String, Int> )이다.
    맵은 HashMap, LinkedHashMap, TreeMap 등이 있으며, 이들의 성질은 각각에 대응하는 Set 클래스와 성질이 비슷하다
    * */

    /* 7.1.2 Comparable과 Comparator

    주요 차이점
    정렬 기준:
    Comparable: 객체의 자연 순서를 정의합니다. 객체 자체에 정렬 로직이 포함됩니다.
    Comparator: 객체의 외부에서 정렬 순서를 정의합니다. 여러 가지 정렬 기준을 만들 수 있습니다.
    사용 방법:
    Comparable: 클래스가 Comparable 인터페이스를 구현하고 compareTo 메서드를 오버라이드해야 합니다.
    Comparator: 별도의 클래스를 만들어 Comparator 인터페이스를 구현하고 compare 메서드를 오버라이드해야 합니다.

    자바처럼 코틀린도 Comparable과 Comparator 타입을 지원한다.
    Comparable(비교 가능) 인스턴스는 자연적인 순서를 지원하며, 동일한 타입의 다른 인스턴스와 순서를 비교할 때 쓸 수 있는 compareTo() 메서드를 포함한다
    compareTo() 함수는 수신 객체 인스턴스가 인자로 받은 상대 인스턴스보다 더 크면 양수, 더 작으면 음수, 같으면 0을 반환한다.
    compareTo() 구현은 equals() 함수 구현과 서로 의미가 일치해야 한다.
    * */

    class Person(
        val firstName: String,
        val familyName : String,
        val age: Int
    ): Comparable<Person> {
        val fullName get() = "$firstName $familyName"
        override fun compareTo(other: Person): Int = fullName.compareTo(other.fullName)
    }

    /*
    비교기(comparator)를 사용하여 클래스의 여러 프로퍼티 조합을 기준으로 정렬할 수도 있다.
    Comparator<T> 클래스는 타입 T의 인스턴스 객체를 두 개 인자로 받아 비교한 결과를 반환하는 compare() 함수를 제공한다.
    * */

    val AGE_COMPARATOR = Comparator<Person>{ p1, p2 ->
        p1.age.compareTo(p2.age)
    }

    /*
    람다 비교 함수를 통해 비교기를 간결하게 작성할 수 있으며,
    compareBy()나 compareByDescending() 함수를 통해 대상 객체 대신 사용할 수 있는 비교 가능 객체를 제공하게 함으로써 비교기를 만들 수도 있다.
    * */

    val AGE_COMPARATORONE = compareBy<Person>{ it.age }
    val REVERSE_AGE_COMPARATOR = compareByDescending<Person> { it.age }


    val people = listOf(
        Person("John", "Doe", 30),
        Person("Jane", "Smith", 25),
        Person("Alice", "Johnson", 28)
    )

    val sortedByName = people.sorted()
    val sortedByAge = people.sortedWith(AGE_COMPARATOR)
    val sortedByAgeLambda = people.sortedWith(AGE_COMPARATORONE)
    val sortedByAgeDescending = people.sortedWith(REVERSE_AGE_COMPARATOR)


    /* 7.1.3 컬랙션 생성하기
    자바와 마찬가지로 ArrayList나 LinkedHashSet 같은 클래스의 인스턴스를 생성자를 호출해 생성할 수 있다.
    * */

    val alist = ArrayList<String>()
    alist.add("red")
    alist.add("green")
    println(alist) // [red, green]

    val set = HashSet<Int>()
    set.add(12)
    set.add(21)
    set.add(12)
    println(set) // [12, 21]

    val map = TreeMap<Int, String>()
    map[20] = "Twenty"
    map[10] = "Ten"
    println(map) // {10=Ten, 20=Twenty}

    /*
    arrayOf() 와 비슷하게 가변 길이 인자를 받는 함수를 사용해 몇몇 컬렉션 클래스의 인스턴스를 생성할 수 있다.
    emptyList() / emptySet(): 불변인 리스트/집합 인스턴스를 생성한다.
    listOf() / setOf(): 인자로 제공한 가변 길이 인자에 기반한 불변 리스트/집합 인스턴스를 만든다.
    listOfNotNull(): 널인 값을 걸러내고 남은 원소들로 이뤄진 새 불변 리스트를 만든다.
    mutableListOf() / mutableSetOf(): 가변 리스트/집합의 디폴트 구현 인스턴스를 만든다.
    arrayListOf(): 새로운 ArrayList를 만든다.
    hashSetOf() / linkedSetOf() / sortedSetOf(): HashSet / LinkedHashSet / TreeSet의 새 인스턴스를 만든다.
    * */

    val emptyList = emptyList<String>()
    println(emptyList)    // []
    //emptyList.add("abc")  // Error

    val singletonSet = setOf("abc")
    println(singletonSet)      // [abc]
    //singletonSet.remove("abc") // Error

    val mutableList = mutableListOf("abc")
    println(mutableList)  // [abc]
    mutableList.add("def")
    mutableList[0] = "xyz"
    println(mutableList)  // [xyz, def]

    val sortedSet = sortedSetOf(8, 5, 7, 1, 4)
    println(sortedSet)  // [1, 4, 5, 7, 8]
    sortedSet.add(2)
    println(sortedSet)  // [1, 2, 4, 5, 7, 8]

    /*
    맵을 만들 때도 비슷한 함수를 쓸 수 있다.
    emptyMap(): 빈 불변 맵을 만든다.
    mapOf(): 새 불변 맵을 만든다(내부적으로 LinkedHashMap을 만든다).
    mutableMapOf(): 가변 맵의 디폴트 구현 인스턴스를 만든다(내부적으로 LinkedHashMap을 만든다).
    hashMapOf() / linkedMapOf() / sortedMapOf(): HashMap / LinkedHashMap / TreeMap의 새 인스턴스를 만든다.

    맵 함수들은 Pair 객체들로 이뤄진 가변 인자를 받는다. to 중위 연산자를 사용하면 Pair 객체를 쉽게 만들 수 있다.
    * */

    val emptyMap = emptyMap<Int, String>()
    println(emptyMap) // {}
    // emptyMap[10] = "Ten" // Error

    val singletonMap = mapOf(10 to "Ten")
    println(singletonMap) // {10=Ten}
    // singletonMap.remove("abc") // error

    val mutableMap = mutableMapOf(10 to "Ten")
    println(mutableMap)  // {10=Ten}
    mutableMap[20] = "Twenty"
    mutableMap[100] = "Hundred"
    mutableMap.remove(10)
    println(mutableMap)  // {20=Twenty, 100=Hundred}

    val sortedMap = sortedMapOf(3 to "three", 1 to "one", 2 to "two")
    println(sortedMap)
    sortedMap[0] = "zero"
    println(sortedMap)

    /*
    Pair를 사용하지 않고, 가변 맵을 만들고 set() 메서드나 인덱스 연산자([ ])를 사용해 원소를 추가할 수도 있다.
    배열과 비슷하게 크기를 지정하고 인덱스로부터 값을 만들어주는 함수를 제공함으로써 새 리스트를 만들 수도 있다.
    * */
    println(List(5) { it * it})  // [0, 1, 4, 9, 16]

    val numbers = MutableList(5) { it * 2 }
    println(numbers)  // [0, 2, 4, 6, 8]
    numbers.add(100)
    println(numbers)  // [0, 2, 4, 6, 8, 100]

    /*
    시퀀스는 sequenceOf() 함수를 사용해 구현할 수 있으며, 가변 인자또는 배열, 이터러블, 맵 등의 기존 컬렉션에 대해 asSequence() 함수를 호출해서 시퀀스를 얻을 수도 있다.
    * */
    println(sequenceOf(1, 2, 3).iterator().next())   // 1
    println(listOf(10, 20, 30).asSequence().iterator().next()) // 10
    println(
        mapOf(1 to "One", 2 to "Two").asSequence().iterator().next()
    )  // 1=One

    /*
    마지막으로 배열 원소를 바탕으로 리스트를 만들거나 시퀀스 집합으로 만드는 toSet(), toList(), toMap()과 같은 컬렉션 사이의 변환을 처리하는 함수이다.
    * */
    println(listOf(1, 2, 3, 2, 3).toSet()) // [1, 2, 3]
    println(arrayOf("red", "green", "blue").toSortedSet()) // [blue, green, red]
    println(mapOf(1 to "one", 2 to "two", 3 to "three").toList()) // [(1, one), (2, two), (3, three)]
    println(sequenceOf(1 to "one", 2 to "two", 3 to "three").toMap()) // {1=one, 2=two, 3=three}
}