
val r = new Rational (1,2)
r.neg.toString
r.toString

val x = new Rational (1,3)
val y = new Rational (5,7)
val z = new Rational (3,2)

x.sub(y).sub(z)
y.add(y)
x.less(z)
x.max(y)

class Rational (x: Int, y: Int) {
  private def gcd(a:Int,b:Int):Int = {
    if (b==0) a else gcd(b, a % b)
  }
  private val g = gcd(x,y)
  def n = x / g
  def d = y / g

  def less (that:Rational) = this.n * that.d < that.n * this.d
  def max (that:Rational) = if (this.less(that)) that else this

  def add(i:Rational): Rational = {
    new Rational (n * i.d + i.n * d, d * i.d)
  }

  def neg: Rational = new Rational(-n, d)

  def sub(i:Rational): Rational = add(i.neg)

  override def toString: String = n.toString + "/" +
    d.toString
}