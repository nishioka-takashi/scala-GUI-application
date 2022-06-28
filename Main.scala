import scala.io.Source
import scala.swing._
import scala.swing.event._
import java.awt.{Font,Color}

class Solver {
  private val file = Source.fromFile("./post-code/KEN_ALL.CSV", "Shift_JIS")
  private val postCodes: List[PostCode] = try {
    (for (line <- file.getLines()) yield PostCode.fromString(line)).toList
  } catch {
    case e: Exception => Nil
  }
  file.close

  def findAddress(input: String): List[String] = {
    var result = List[String]()
    for(p <- postCodes) {
      if (p.postcode.startsWith(input)) {
        result = "〒" + p.postcode + ": " + p.prefecture + p.city + p.area :: result
      }
    }
    result.reverse
  }

  def getPrefectures(): List[String] = {
    postCodes.map(p => p.prefecture).distinct
  }

  def getCities(prefecture: String): List[String] = {
    postCodes.collect{
      case p if p.prefecture == prefecture => p.city}.distinct
  }

  def getAreas(prefecture: String, city: String): List[String] = {
    postCodes.collect{
      case p if p.prefecture == prefecture && p.city == city => p.area}.distinct
  }

  def getPostCode(prefecture: String, city: String, area: String): Option[PostCode] = {
    postCodes.find(p => p.prefecture == prefecture && p.city == city && p.area == area)
  }
}

class UI(solver: Solver) extends MainFrame {
  title = "Home"
  preferredSize = new Dimension(500, 300)

  val postToAdd = new Button("郵便番号から")
  val addToPost = new Button("住所から")

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label("郵便番号検索") {
      xLayoutAlignment = 0.5
      foreground = Color.DARK_GRAY
      font = new Font(null, Font.PLAIN, 30)
    }
    contents += Swing.VStrut(20)
    contents += new GridPanel(1, 2) {
      contents += postToAdd
      contents += addToPost
    }
    border = Swing.EmptyBorder(10, 10,10, 10)
  }

  listenTo(postToAdd)
  listenTo(addToPost)
  reactions += {
    case ButtonClicked(`postToAdd`) => {
      val addressSearch = new AddressSearch(solver)
      addressSearch.visible = true
    }
    case ButtonClicked(`addToPost`) => {
      val postCodeSearch = new PostCodeSearch(solver)
      postCodeSearch.visible = true
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val solver = new Solver
    val ui = new UI(solver)
    ui.visible = true
  }
}