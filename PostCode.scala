case class PostCode(postcode: String, prefecture: String, city: String, area: String)

object PostCode {
  def fromString(string: String): PostCode = {
    val list = string.split(',').map(s => s.tail.init)
    PostCode(list(2), list(6), list(7), list(8))
  }
}