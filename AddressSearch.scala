import scala.swing._
import scala.swing.event._

class AddressSearch(solver: Solver) extends Frame {
  title = "郵便番号から住所を検索"

  val searchField = new TextField { columns = 32 }
  val searchButton = new Button("検索")
  val searchLine = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("郵便番号（ハイフンなしで入力）")
    contents += searchField
    contents += Swing.HStrut(20)
    contents += searchButton
  }
  val resultField = new TextArea {
    rows = 10
    lineWrap = true
    wordWrap = true
    editable = false
  }
  contents = new BoxPanel(Orientation.Vertical) {
    contents += searchLine
    contents += Swing.VStrut(10)
    contents += new ScrollPane(resultField)
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(searchField)
  listenTo(searchButton)
  reactions += {
    case EditDone(`searchField`) => search()
    case ButtonClicked(`searchButton`) => search()
  }

  def search() {
    val input = searchField.text
    val addresses = solver.findAddress(input)
    if (addresses.length == 0) {
      resultField.text = "no addresses found."
    } else {
      resultField.text = addresses mkString "\n"
      resultField.caret.position = 0
    }
  }
}
