package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
    val s6 = singletonSet(6)
    val s12 = union(s1, s2)
    val s23 = union(s2, s3)
    val s123 = union(s1, union(s2, s3))
    val s246 = union(s2, union(s4, s6))
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains only elems in both sets") {
    new TestSets {
      val s1only = intersect(s12, s1)
      assert(contains(s1only, 1), "Intersect s12 and s1")
      val s2only = intersect(s12, s23)
      assert(contains(s2only, 2), "Intersect 1,2 and 2,3")
    }
  }

  test("diff has elems in first set but not second set") {
    new TestSets {
      assert(contains(diff(s12,s23), 1), "Diff 1,2 vs 2,3 has 1")
      assert(!contains(diff(s12,s23), 2), "Diff 1,2 vs 2,3 does NOT have 2")
      assert(!contains(diff(s12,s23), 3), "Diff 1,2 vs 2,3 does NOT have 3")
    }
  }

  test("filter tests by predicate") {
    new TestSets {
      def isOne = (x:Int) => x == 1
      assert(contains(filter(s1, isOne), 1), "=1 filter: s1 has elem 1")
      assert(!contains(filter(s2, isOne), 1), "=1 filter: s2 does not have elem 1")

      val sEven = filter(s123, x => x % 2 == 0)
      assert(contains(sEven, 2), "even num filter: s123 has 2")
      assert(!contains(sEven, 1), "even num filter: s123 has no 1")
      assert(!contains(sEven, 3), "even num filter: s123 has no 3")

    }
  }

  test("forall tests") {
    new TestSets {
      assert(forall(s1, x => x==1), "all elem in s1 is 1")
      assert(!forall(s12, x => x==1), "not all elem in s12 are 1")
      assert(forall(s123, x => x>0), "all elem in s123 are positive")
    }
  }

  test("exists tests") {
    new TestSets {
      assert(exists(s1,x => x==1), "s1 contains a 1")
      assert(!exists(s1,x => x==2), "s1 does not contain a 2")
      assert(exists(s123,x => x==3), "s123 contains a 3")
      assert(!exists(s23,x => x==1), "s23 does not contain a 1")
      assert(!exists(s123,x => x<0), "s123 does not contain negative numbers")
    }
  }

  test("map tests") {
    new TestSets {
      printSet(map(s1, x=>x*2))
      assert(contains(map(s1, x=>x), 1), "map identity function")
      assert(contains(map(s1, x=>x*2), 2), "s1 map 2x = 2")
      assert(contains(map(s12, x=>x-1), 0), "s12 map x-1 has 0")
      assert(contains(map(s3, x=>x+6), 9), "s3 map x+6 has 9")
      assert(contains(map(s23, x=>x*x), 4), "s23 map x*x has 4")
      assert(map(s123, x=>x*2).toString() == s246.toString(), "s123 map 2x is s246")
    }
  }

  test("Printing all sets:") {
    new TestSets {
/*
      printSet(s1)
      printSet(s2)
      printSet(s3)
      printSet(s12)
      printSet(s23)
      printSet(s123)
      */
    }
  }
}
